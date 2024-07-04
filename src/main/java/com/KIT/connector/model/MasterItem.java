package com.KIT.connector.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class MasterItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @JsonFormat(pattern = "0000")

    private int Code;
    private String ItemName;
    private String Type;
    private String Maker;
    private int MOQ;
    private String Location;

}
