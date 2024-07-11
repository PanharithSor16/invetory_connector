package com.KIT.connector.repository;

import com.KIT.connector.model.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcBalanceRepository implements BalanceRepository{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Balance> findAll() {
        return null;
    }
}
