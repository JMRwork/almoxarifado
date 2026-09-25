package edu.infnet.almoxarifado_servicos.interfaces;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.infnet.almoxarifado_servicos.domain.Item;

@FeignClient(name = "almoxarifado", url = "${almoxarifado.items.url:http://localhost:8080/items-service}")
public interface ItemClient {

    @GetMapping("/items")
    public List<Item> listarTodos();

    @GetMapping("/items/{id}")
    public Item buscarPorId(@PathVariable("id") Long id);

}
