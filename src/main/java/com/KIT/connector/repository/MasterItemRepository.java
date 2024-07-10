package com.KIT.connector.repository;

import com.KIT.connector.model.MasterItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterItemRepository extends JpaRepository<MasterItem,Long> {

}
