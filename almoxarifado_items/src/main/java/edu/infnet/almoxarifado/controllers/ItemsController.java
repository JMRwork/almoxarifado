package edu.infnet.almoxarifado.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.infnet.almoxarifado.dtos.ItemsRequestDTO;
import edu.infnet.almoxarifado.dtos.ItemsResponseDTO;
import edu.infnet.almoxarifado.service.ItemsService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/items")
public class ItemsController {

    private final ItemsService itemService;

    private ItemsController(ItemsService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemsResponseDTO> criar(@Valid @RequestBody ItemsRequestDTO request) {
        ItemsResponseDTO response = itemService.criarItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemsResponseDTO>> listarTodos() {
        return ResponseEntity.ok(itemService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemsResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemsResponseDTO> atualizar(@PathVariable Long id,
            @Valid @RequestBody ItemsRequestDTO request) {
        ItemsResponseDTO response = itemService.atualizarItem(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}