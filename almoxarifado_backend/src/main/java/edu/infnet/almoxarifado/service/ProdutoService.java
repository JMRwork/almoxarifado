package edu.infnet.almoxarifado.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import edu.infnet.almoxarifado.dtos.ProdutoRequestDTO;
import edu.infnet.almoxarifado.dtos.ProdutoResponseDTO;
import edu.infnet.almoxarifado.exceptions.ResourceNotFoundException;
import edu.infnet.almoxarifado.models.Produto;
import edu.infnet.almoxarifado.repositories.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO request) {
        // Validação extra: código único
        if (produtoRepository.findByCodigo(request.getCodigo()).isPresent()) {
            throw new RuntimeException("Já existe um produto com o código: " + request.getCodigo());
        }

        Produto produto = Produto.builder()
                .nome(request.getNome())
                .codigo(request.getCodigo())
                .quantidade(request.getQuantidade())
                .localizacao(request.getLocalizacao())
                .build();

        Produto salvo = produtoRepository.save(produto);
        return toResponseDTO(salvo);
    }

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
        return toResponseDTO(produto);
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO request) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));

        // Verifica se o código já existe em outro produto
        produtoRepository.findByCodigo(request.getCodigo())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new RuntimeException("Código já está em uso por outro produto.");
                    }
                });

        produto.setNome(request.getNome());
        produto.setCodigo(request.getCodigo());
        produto.setQuantidade(request.getQuantidade());
        produto.setLocalizacao(request.getLocalizacao());

        Produto atualizado = produtoRepository.save(produto);
        return toResponseDTO(atualizado);
    }

    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado com id: " + id);
        }
        produtoRepository.deleteById(id);
    }

    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .codigo(produto.getCodigo())
                .quantidade(produto.getQuantidade())
                .localizacao(produto.getLocalizacao())
                .dataCriacao(produto.getDataCriacao())
                .build();
    }
}