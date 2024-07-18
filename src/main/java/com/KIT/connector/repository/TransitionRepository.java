package com.KIT.connector.repository;

import com.KIT.connector.model.MasterItem;
import com.KIT.connector.model.Transition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransitionRepository extends JpaRepository<Transition, Long> {
    @Query("SELECT u FROM Transition u WHERE u.ItemCode LIKE CONCAT('%', :NameCode, '%') OR u.ItemName LIKE CONCAT('%', :NameCode, '%') AND  u.RegisterBy = :username")
    List<Transition> getAllItemByUser(@Param("NameCode") String NameCode, String username);
    @Query("SELECT u FROM Transition u WHERE u.RegisterBy = :username")
    List<Transition> getAllItemUser(String username);

    @Query("SELECT u FROM Transition u WHERE u.ItemCode LIKE CONCAT('%', :NameCode, '%') OR u.ItemName LIKE CONCAT('%', :NameCode, '%')")
    List<Transition> getAllItem(@Param("NameCode") String NameCode);
}
