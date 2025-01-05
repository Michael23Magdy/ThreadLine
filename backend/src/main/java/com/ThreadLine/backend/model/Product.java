package com.ThreadLine.backend.model;

import lombok.Data;
import lombok.Getter;

import java.util.Random;
import java.util.UUID;

@Data
public class Product implements Cloneable {
    private String id;
    private String color;

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

    @Override
    public Product clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Failed to clone Product", e);
        }
    }
}
