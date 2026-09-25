package edu.infnet.almoxarifado.messaging;

import java.util.UUID;

public record EstoqueAjusteEvento(UUID eventId, Long servicoId, Long itemId, Integer delta) {
}