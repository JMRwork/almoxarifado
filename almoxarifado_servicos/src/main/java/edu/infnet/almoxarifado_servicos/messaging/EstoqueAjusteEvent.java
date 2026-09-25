package edu.infnet.almoxarifado_servicos.messaging;

import java.util.UUID;

public record EstoqueAjusteEvent(UUID eventId, Long servicoId, Long itemId, Integer delta) {
    public EstoqueAjusteEvent(Long servicoId, Long itemId, Integer delta) {
        this(UUID.randomUUID(), servicoId, itemId, delta);
    }
}