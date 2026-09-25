package edu.infnet.almoxarifado_servicos.messaging;

@FunctionalInterface
public interface EstoqueEventPublisher {
    void publicar(EstoqueAjusteEvent event);
}