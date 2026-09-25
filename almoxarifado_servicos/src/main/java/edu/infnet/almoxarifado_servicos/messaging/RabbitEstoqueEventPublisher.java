package edu.infnet.almoxarifado_servicos.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RabbitEstoqueEventPublisher implements EstoqueEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final String exchange;

    public RabbitEstoqueEventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper,
            @Value("${app.rabbitmq.exchange:almoxarifado.events}") String exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.exchange = exchange;
    }

    @Override
    public void publicar(EstoqueAjusteEvent event) {
        try {
            rabbitTemplate.convertAndSend(exchange, "estoque.ajuste", objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Não foi possível serializar o evento de estoque", exception);
        }
    }
}