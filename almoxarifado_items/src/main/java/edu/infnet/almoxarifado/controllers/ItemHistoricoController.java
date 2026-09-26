package edu.infnet.almoxarifado.controllers;

import org.springframework.data.history.Revisions;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.infnet.almoxarifado.models.Item;
import edu.infnet.almoxarifado.service.ItemHistoricoService;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("historico")
public class ItemHistoricoController {

    private ItemHistoricoService service;

    public ItemHistoricoController(ItemHistoricoService service) {
        this.service = service;
    }

    @GetMapping("items/{id}")
    public ResponseEntity<Revisions<Integer, Item>> obterHistoricoItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.obterHistoricoItem(id));
    }

}
