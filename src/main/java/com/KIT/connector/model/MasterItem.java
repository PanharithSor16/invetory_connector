package com.KIT.connector.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "tbMasterItem")
public class MasterItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @NonNull
    private String ItemCode;
    @NonNull
    private String ItemName;
    private String Type;
    private String Maker;
    private int MOQ;
    private String Location;
    public MasterItem(){}

    public MasterItem(String itemName, String code, String location, String maker, int moq, String type) {
        this.ItemName = itemName;
        this.Maker = maker;
        this.ItemCode = code;
        this.Location = location;
        this.MOQ = moq;
        this.Type = type;
    }
}
