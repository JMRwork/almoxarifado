package edu.infnet.almoxarifado.messaging;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoProcessadoRepository extends JpaRepository<EventoProcessado, UUID> {
}