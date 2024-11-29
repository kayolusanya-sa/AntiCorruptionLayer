package com.cubelogic.anticorruptionlayer.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long traderId;

    private double price;

    private double volume;

    @Enumerated(EnumType.STRING)
    private Side side;

    @Column(name = "timeStamp", columnDefinition = "TIMESTAMP")
    private LocalDateTime timeStamp;

    public Transaction(long traderId, double price, double volume, Side side) {
        this.traderId = traderId;
        this.price = price;
        this.volume = volume;
        this.side = side;
        this.timeStamp = LocalDateTime.now();
    }

    public Transaction() {
        this.timeStamp = LocalDateTime.now();
    }
}
