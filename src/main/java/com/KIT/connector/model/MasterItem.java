package com.KIT.connector.model;

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
    private int id ;

    private int code;
}
