package com.bookstore.notificationservice;

public record OrderAcceptedMessage (
        Long orderId
){}