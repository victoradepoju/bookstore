package com.bookstore.orderservice.order.event;

public record OrderNotifiedMessage (
        Long orderId
) {
}
