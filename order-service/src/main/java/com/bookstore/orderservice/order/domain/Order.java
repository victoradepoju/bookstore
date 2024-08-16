package com.bookstore.orderservice.order.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;

@Table("orders")
public record Order (

        @Id
        Long id,

        String bookIsbn,
        String bookName,
        Double bookPrice,
        Integer quantity,
        OrderStatus status,

        @CreatedDate
        LocalDateTime createdDate,

        @LastModifiedDate
        LocalDateTime lastModifiedDate,

        @Version
        int version
){

    public static Order of(String bookIsbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status, LocalDateTime createdDate) {
        return new Order(null, bookIsbn, bookName, bookPrice, quantity, status, createdDate, null, 0);
    }

}