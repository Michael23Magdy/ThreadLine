package com.ThreadLine.backend.model;

import lombok.Data;

import java.util.Random;
import java.util.UUID;

@Data
public class Product {
    private String id;
    private String color;

    public String getId() {
        return id;
    }


    public Product() {
        this.id = UUID.randomUUID().toString();
        this.color = generateRandomColor();
    }
    public Product(String id) {
        this.id = id;
        this.color = generateRandomColor();
    }

    private String generateRandomColor() {
        Random random = new Random();
        return String.format("#%06x", random.nextInt(0xFFFFFF + 1));
    }
}
