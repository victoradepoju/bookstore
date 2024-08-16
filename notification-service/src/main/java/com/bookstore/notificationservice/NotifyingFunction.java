package com.bookstore.notificationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class NotifyingFunction {

    private static final Logger log = LoggerFactory.getLogger(NotifyingFunction.class);

    @Bean
    public Function<OrderAcceptedMessage, Long> pack() {
        return orderAcceptedMessage -> {
            log.info("Packing order {}", orderAcceptedMessage.orderId());
            return orderAcceptedMessage.orderId();
        };
    }

    @Bean
    public Function<Flux<Long>, Flux<OrderNotifiedMessage>> label() {
        return orderFluxId -> orderFluxId.map(
                orderId -> {
                    log.info("The order with id {} has been labeled", orderId);
                    return new OrderNotifiedMessage(orderId);
                }
        );
    }
}
