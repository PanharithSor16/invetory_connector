package com.KIT.connector.repository;

import com.KIT.connector.model.Transition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransitionRepository extends JpaRepository<Transition, Long> {
}
