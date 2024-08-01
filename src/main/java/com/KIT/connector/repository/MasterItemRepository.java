package com.KIT.connector.repository;

import com.KIT.connector.model.MasterItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterItemRepository extends JpaRepository<MasterItem, Long> {
    @Query("SELECT u FROM MasterItem u WHERE u.ItemCode LIKE CONCAT('%', :NameCode, '%') OR u.ItemName LIKE CONCAT('%', :NameCode, '%')")
    List<MasterItem> getItemByNameCode(@Param("NameCode") String NameCode);


    MasterItem findAllById(Long id);
@Query("SELECT u FROM MasterItem u WHERE u.ItemCode = :ItemCode ")
    MasterItem findItemCode(@Param("ItemCode") String ItemCode);
}
