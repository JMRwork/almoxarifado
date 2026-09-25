package edu.infnet.almoxarifado.messaging;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {
    @Bean
    DirectExchange almoxarifadoEventsExchange() {
        return new DirectExchange("almoxarifado.events", true, false);
    }

    @Bean
    DirectExchange almoxarifadoDeadLetterExchange() {
        return new DirectExchange("almoxarifado.events.dlx", true, false);
    }

    @Bean
    Queue itemsStockAdjustmentQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "almoxarifado.events.dlx");
        arguments.put("x-dead-letter-routing-key", "estoque.ajuste.dead-letter");
        return new Queue("items.stock.adjustment", true, false, false, arguments);
    }

    @Bean
    Queue itemsStockAdjustmentDeadLetterQueue() {
        return new Queue("items.stock.adjustment.dead-letter", true);
    }

    @Bean
    Binding itemsStockAdjustmentBinding(Queue itemsStockAdjustmentQueue, DirectExchange almoxarifadoEventsExchange) {
        return BindingBuilder.bind(itemsStockAdjustmentQueue)
                .to(almoxarifadoEventsExchange).with("estoque.ajuste");
    }

    @Bean
    Binding itemsStockAdjustmentDeadLetterBinding(Queue itemsStockAdjustmentDeadLetterQueue,
            DirectExchange almoxarifadoDeadLetterExchange) {
        return BindingBuilder.bind(itemsStockAdjustmentDeadLetterQueue)
                .to(almoxarifadoDeadLetterExchange).with("estoque.ajuste.dead-letter");
    }
}