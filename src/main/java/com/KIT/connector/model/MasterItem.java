package com.KIT.connector.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbMasterItem")
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
