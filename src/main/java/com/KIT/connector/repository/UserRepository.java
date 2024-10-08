package com.KIT.connector.repository;

import com.KIT.connector.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String Username);
    boolean existsByUsername(String username);

}
