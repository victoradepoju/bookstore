package com.bookstore.orderservice.order.domain;

import com.bookstore.orderservice.book.Book;
import com.bookstore.orderservice.book.BookClient;
import com.bookstore.orderservice.order.event.OrderAcceptedMessage;
import com.bookstore.orderservice.order.event.OrderNotifiedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final BookClient bookClient;
    private final OrderRepository orderRepository;

    private final StreamBridge streamBridge;

    public OrderService(BookClient bookClient, OrderRepository orderRepository, StreamBridge streamBridge) {
        this.bookClient = bookClient;
        this.orderRepository = orderRepository;
        this.streamBridge = streamBridge;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Mono<Order> submitOrder(String isbn, int quantity) {
        return bookClient.getBookByIsbn(isbn)
                .map(book -> buildAcceptedOrder(book, quantity))
                .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
                .flatMap(orderRepository::save)
                .doOnNext(this::publishOrderAcceptedEvent);
    }

    public static Order buildAcceptedOrder(Book book, int quantity) {
        return Order.of(book.isbn(), book.title() + " - " + book.author(),
                book.price(), quantity, OrderStatus.ACCEPTED, LocalDateTime.now());
    }

    public static Order buildRejectedOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED, LocalDateTime.now());
    }

    public Flux<Order> consumeOrderNotifiedEvent(
            Flux<OrderNotifiedMessage> orderNotifiedMessageFlux
    ) {
        return orderNotifiedMessageFlux.flatMap(
                message -> orderRepository.findById(message.orderId())
                        .map(this::buildNotifiedOrder)
                        .flatMap(orderRepository::save)
        );
    }

    private Order buildNotifiedOrder(Order existingOrder) {
        return new Order(
                existingOrder.id(),
                existingOrder.bookIsbn(),
                existingOrder.bookName(),
                existingOrder.bookPrice(),
                existingOrder.quantity(),
                OrderStatus.DISPATCHED,
                existingOrder.createdDate(),
                existingOrder.lastModifiedDate(),
                existingOrder.version()
        );
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.status().equals(OrderStatus.ACCEPTED)) {
            return;
        }
        var orderAcceptedMessage =
                new OrderAcceptedMessage(order.id());
        log.info("Sending order accepted event with id: {}", order.id());
        var result = streamBridge.send("acceptOrder-out-0",
                orderAcceptedMessage);
        log.info("Result of sending data for order with id {}: {}",
                order.id(), result);
    }
}
