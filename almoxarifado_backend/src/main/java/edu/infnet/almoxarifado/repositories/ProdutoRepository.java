package edu.infnet.almoxarifado.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.infnet.almoxarifado.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigo(String codigo);
}