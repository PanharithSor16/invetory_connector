package com.KIT.connector.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@AllArgsConstructor
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

    // Default constructor
    public Transition() {
    }


    public Transition(String username, String itemCode, String itemName, int receivedQty, int issuedQty, String location) {
        this.ItemCode = itemCode;
        this.ItemName = itemName;
        this.RegisterBy = username;
        this.ReceivedQty = receivedQty;
        this.IssuedQty = issuedQty;
        this.Location = location;
        this.StockValue = receivedQty - issuedQty;
    }

}
