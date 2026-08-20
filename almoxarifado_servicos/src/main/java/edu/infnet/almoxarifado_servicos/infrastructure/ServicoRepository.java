package edu.infnet.almoxarifado_servicos.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.infnet.almoxarifado_servicos.domain.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
