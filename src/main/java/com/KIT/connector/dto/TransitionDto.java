package com.KIT.connector.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransitionDto {
    private String itemCode;
    private String itemName;
    private Double stockValue;

    // Constructor
    public TransitionDto(String itemCode, String itemName, Double stockValue) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.stockValue = stockValue;
    }
}
