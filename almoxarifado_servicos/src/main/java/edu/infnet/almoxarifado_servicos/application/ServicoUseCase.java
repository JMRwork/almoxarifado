package edu.infnet.almoxarifado_servicos.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.infnet.almoxarifado_servicos.domain.Item;
import edu.infnet.almoxarifado_servicos.domain.Servico;
import edu.infnet.almoxarifado_servicos.infrastructure.ServicoRepository;
import edu.infnet.almoxarifado_servicos.interfaces.ItemClient;

@Service
public class ServicoUseCase {

    private final ServicoRepository repository;
    private final ItemClient itemClient;

    public ServicoUseCase(ServicoRepository repository, ItemClient itemClient) {
        this.repository = repository;
        this.itemClient = itemClient;
    }

    @Transactional
    public Servico criar(String identificador, String descricao, List<Item> items, List<Item> itemsCadastrados) {
        Servico servico = Servico.builder()
                .identificador(identificador)
                .descricao(descricao)
                .items(new ArrayList<>(items))
                .build();

        repository.save(servico);
        items.stream().forEach(item -> {
            Item itemCadastrado = itemsCadastrados.stream()
                    .filter(itemCadastrado1 -> itemCadastrado1.getId().equals(item.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Item não encontrado: " + item.getId()));
            itemClient.atualizar(item.getId(), new Item(
                    itemCadastrado.getId(),
                    itemCadastrado.getNome(),
                    itemCadastrado.getCodigo(),
                    itemCadastrado.getQuantidade() - item.getQuantidade()));
        });
        return servico;
    }

    @Transactional(readOnly = true)
    public List<Servico> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado: " + id));
    }

    @Transactional
    public Servico atualizar(Long id, String identificador, String descricao, List<Item> itemsAtualizados,
            List<Item> itemsCadastrados) {
        Servico atual = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado: " + id));
        atual.setIdentificador(identificador);
        atual.setDescricao(descricao);
        List<Item> itemsServico = atual.getItems();
        List<Item> itemsExistentes = new ArrayList<>();

        for (Item itemServico : itemsServico) {
            Item itemCadatrado = itemsCadastrados.stream()
                    .filter(item -> item.getId().equals(itemServico.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Item não encontrado: " + itemServico.getId()));

            itemsExistentes.add(new Item(
                    itemCadatrado.getId(),
                    itemCadatrado.getNome(),
                    itemCadatrado.getCodigo(),
                    itemServico.getQuantidade() + itemCadatrado.getQuantidade()));
        }
        atual.setItems(new ArrayList<>(itemsAtualizados));
        repository.save(atual);
        atual.setItems(new ArrayList<>(itemsAtualizados));
        repository.save(atual);
        itemsExistentes.forEach(item -> {
            itemsAtualizados.stream()
                    .filter(itemAtualizado -> itemAtualizado.getId().equals(item.getId()))
                    .findFirst()
                    .ifPresentOrElse(itemsAtualizado -> {
                        itemClient.atualizar(item.getId(), new Item(
                                itemsAtualizado.getId(),
                                itemsAtualizado.getNome(),
                                itemsAtualizado.getCodigo(),
                                item.getQuantidade() - itemsAtualizado.getQuantidade()));
                    }, () -> {
                        throw new IllegalArgumentException("Item não encontrado: " + item.getId());
                    });
        });
        ;
        return atual;
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Serviço não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    public List<Item> validarItems(List<Item> items) {
        List<Item> itemsCadastrados = itemClient.listarTodos();
        items.forEach(item -> {
            Boolean quantidadeInsuficiente = itemsCadastrados.stream()
                    .filter(itemCadastrado -> itemCadastrado.getId().equals(item.getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Item não encontrado: " + item.getId()))
                    .getQuantidade() < item.getQuantidade();
            if (quantidadeInsuficiente) {
                throw new IllegalArgumentException("Quantidade insuficiente para o item: " + item.getId());
            }
        });
        return itemsCadastrados;
    }
}
