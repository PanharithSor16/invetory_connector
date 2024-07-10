package com.KIT.connector.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Balance {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;



}
