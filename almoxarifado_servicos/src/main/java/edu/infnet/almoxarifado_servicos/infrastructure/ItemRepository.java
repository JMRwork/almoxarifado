package edu.infnet.almoxarifado_servicos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.infnet.almoxarifado_servicos.domain.Servico;

public interface ItemRepository extends JpaRepository<Servico, Long> {

}
