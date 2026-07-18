package edu.infnet.almoxarifado.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.infnet.almoxarifado.dtos.ItemsRequestDTO;
import edu.infnet.almoxarifado.dtos.ItemsResponseDTO;
import edu.infnet.almoxarifado.exceptions.ResourceNotFoundException;
import edu.infnet.almoxarifado.models.Item;
import edu.infnet.almoxarifado.repositories.ItemRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ItemsService {

    private final ItemRepository itemRepository;

    public ItemsService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemsResponseDTO criarItem(ItemsRequestDTO request) {

        if (itemRepository.findByCodigo(request.getCodigo()).isPresent()) {
            throw new RuntimeException("Já existe um item com o código: " + request.getCodigo());
        }

        Item produto = Item.builder()
                .nome(request.getNome())
                .codigo(request.getCodigo())
                .quantidade(request.getQuantidade())
                .localizacao(request.getLocalizacao())
                .build();

        Item salvo = itemRepository.save(produto);
        return toResponseDTO(salvo);
    }

    public List<ItemsResponseDTO> listarTodos() {
        return itemRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ItemsResponseDTO buscarPorId(Long id) {
        Item produto = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com id: " + id));
        return toResponseDTO(produto);
    }

    public ItemsResponseDTO atualizarItem(Long id, ItemsRequestDTO request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado com id: " + id));

        // Verifica se o código já existe em outro produto
        itemRepository.findByCodigo(request.getCodigo())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new RuntimeException("Código já está em uso por outro item.");
                    }
                });

        item.setNome(request.getNome());
        item.setCodigo(request.getCodigo());
        item.setQuantidade(request.getQuantidade());
        item.setLocalizacao(request.getLocalizacao());

        Item atualizado = itemRepository.save(item);
        return toResponseDTO(atualizado);
    }

    public void deletarProduto(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("Item não encontrado com id: " + id);
        }
        itemRepository.deleteById(id);
    }

    private ItemsResponseDTO toResponseDTO(Item item) {
        return ItemsResponseDTO.builder()
                .id(item.getId())
                .nome(item.getNome())
                .codigo(item.getCodigo())
                .quantidade(item.getQuantidade())
                .localizacao(item.getLocalizacao())
                .dataCriacao(item.getDataCriacao())
                .build();
    }
}