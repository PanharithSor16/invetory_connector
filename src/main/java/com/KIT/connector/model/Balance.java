package com.KIT.connector.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Balance {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String ItemName;
    @NonNull
    private String ItemCode;
    private int Location;
    private String Type;
    private String Maker;
    private int MOQ;
    private int BalanceStock;
    private boolean Status;
    public Balance(){}
}
