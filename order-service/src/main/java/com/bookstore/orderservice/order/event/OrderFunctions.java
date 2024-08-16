package com.bookstore.orderservice.order.event;

import com.bookstore.orderservice.order.domain.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class OrderFunctions {

    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    public Consumer<Flux<OrderNotifiedMessage>> notifyOrder(
            OrderService orderService
    ) {
        return orderNotifiedMessageFlux -> orderService
                .consumeOrderNotifiedEvent(orderNotifiedMessageFlux)
                .doOnNext(order -> log.info("The order with id {} is notified", order.id()))
                .subscribe();
    }
}