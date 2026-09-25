package edu.infnet.almoxarifado_servicos.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.infnet.almoxarifado_servicos.domain.Item;
import edu.infnet.almoxarifado_servicos.domain.Servico;
import edu.infnet.almoxarifado_servicos.infrastructure.ServicoRepository;
import edu.infnet.almoxarifado_servicos.interfaces.ItemClient;
import edu.infnet.almoxarifado_servicos.messaging.EstoqueAjusteEvent;
import edu.infnet.almoxarifado_servicos.messaging.EstoqueEventPublisher;

@Service
public class ServicoUseCase {

    private final ServicoRepository repository;
    private final ItemClient itemClient;
    private final EstoqueEventPublisher estoqueEventPublisher;

    public ServicoUseCase(ServicoRepository repository, ItemClient itemClient) {
        this(repository, itemClient, event -> { });
    }

    @Autowired
    public ServicoUseCase(ServicoRepository repository, ItemClient itemClient,
            EstoqueEventPublisher estoqueEventPublisher) {
        this.repository = repository;
        this.itemClient = itemClient;
        this.estoqueEventPublisher = estoqueEventPublisher;
    }

    @Transactional
    public Servico criar(String identificador, String descricao, List<Item> items, List<Item> itemsCadastrados) {
        Servico servico = Servico.builder()
                .identificador(identificador)
                .descricao(descricao)
                .items(new ArrayList<>(items))
                .build();

        Servico salvo = repository.save(servico);
        items.forEach(item -> estoqueEventPublisher.publicar(
            new EstoqueAjusteEvent(salvo.getId(), item.getId(), -item.getQuantidade())));
        return salvo;
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
        List<Item> itemsAnteriores = new ArrayList<>(atual.getItems());
        atual.setItems(new ArrayList<>(itemsAtualizados));
        Servico salvo = repository.save(atual);
        itemsAnteriores.forEach(item -> {
            Item atualizado = itemsAtualizados.stream()
                .filter(itemAtualizado -> itemAtualizado.getId().equals(item.getId()))
                .findFirst().orElse(null);
            int novaQuantidade = atualizado == null ? 0 : atualizado.getQuantidade();
            estoqueEventPublisher.publicar(new EstoqueAjusteEvent(
                salvo.getId(), item.getId(), item.getQuantidade() - novaQuantidade));
        });
        itemsAtualizados.stream()
            .filter(item -> itemsAnteriores.stream().noneMatch(anterior -> anterior.getId().equals(item.getId())))
            .forEach(item -> estoqueEventPublisher.publicar(
                new EstoqueAjusteEvent(salvo.getId(), item.getId(), -item.getQuantidade())));
        return salvo;
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
