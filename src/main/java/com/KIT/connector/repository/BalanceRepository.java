package com.KIT.connector.repository;

import com.KIT.connector.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceRepository  {
    List<Balance> findAll();



}
