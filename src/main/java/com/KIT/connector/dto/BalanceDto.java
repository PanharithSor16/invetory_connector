package com.KIT.connector.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class BalanceDto {
    @NonNull
    private String ItemCode;
    @NonNull
    private String ItemName;
    private String Type;
    private String Maker;
    private String Location;
    private Integer MOQ;
    private Integer Balance;

    public BalanceDto(String object, String object1, String object2, String object3, String object4, Integer object5, Integer object6) {
        this.ItemCode = object;
        this.ItemName = object1;
        this.Type = object2;
        this.Maker = object3;
        this.Location = object4;
        this.MOQ = object5;
        this.Balance = object6;
    }
}
