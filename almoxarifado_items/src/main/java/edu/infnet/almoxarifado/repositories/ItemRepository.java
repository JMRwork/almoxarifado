package edu.infnet.almoxarifado.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import edu.infnet.almoxarifado.models.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, RevisionRepository<Item, Long, Integer> {
    Optional<Item> findByCodigo(String codigo);

    @Query("SELECT p FROM Item p WHERE p.quantidade < p.quantidadeMinima")
    List<Item> findItensComEstoqueBaixo();
}