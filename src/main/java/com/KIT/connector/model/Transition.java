package com.KIT.connector.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbTransition")
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
