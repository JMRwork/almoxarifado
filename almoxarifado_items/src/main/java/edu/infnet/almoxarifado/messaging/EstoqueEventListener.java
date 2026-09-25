package edu.infnet.almoxarifado.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.infnet.almoxarifado.service.ItemsService;

@Component
public class EstoqueEventListener {
    private final ItemsService itemsService;
    private final EventoProcessadoRepository processedEventRepository;
    private final ObjectMapper objectMapper;

    public EstoqueEventListener(ItemsService itemsService, EventoProcessadoRepository processedEventRepository,
            ObjectMapper objectMapper) {
        this.itemsService = itemsService;
        this.processedEventRepository = processedEventRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "items.stock.adjustment")
    @Transactional
    public void receber(String payload) {
        try {
            EstoqueAjusteEvento event = objectMapper.readValue(payload, EstoqueAjusteEvento.class);
            if (processedEventRepository.existsById(event.eventId())) {
                return;
            }
            itemsService.ajustarQuantidade(event.itemId(), event.delta());
            processedEventRepository.save(new EventoProcessado(event.eventId()));
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Evento de estoque inválido", exception);
        }
    }
}