package com.KIT.connector.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Transition {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String ItemCode;
    private String ItemName;
    private int ReceivedQty;
    private int IssuedQty;
    private int StockValue;
    private String RegisterBy;
    private String Location;
}
