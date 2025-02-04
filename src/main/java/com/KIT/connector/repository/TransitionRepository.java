package com.KIT.connector.repository;

import com.KIT.connector.dto.BalanceDto;
import com.KIT.connector.dto.TransitionDto;
import com.KIT.connector.model.Transition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@EnableJpaRepositories
@Repository
public interface TransitionRepository extends JpaRepository<Transition, Long> {
    @Query("SELECT u FROM Transition u WHERE (u.ItemCode LIKE CONCAT('%', :NameCode, '%') OR u.ItemName LIKE CONCAT('%', :NameCode, '%')) AND  u.RegisterBy = :username And u.Status = true")
    List<Transition> getAllItemByUser(@Param("NameCode") String NameCode,@Param("username") String username);
    @Query("SELECT u FROM Transition u WHERE u.RegisterBy = ':username' And u.Status = true ")
    List<Transition> getAllItemUser(@Param("username") String username);

    // For Transition
    @Query("SELECT u FROM Transition u WHERE (u.ItemCode LIKE CONCAT('%', :NameCode, '%') OR u.ItemName LIKE CONCAT('%', :NameCode, '%')) And u.Status = true")
    List<Transition> getAllItem(@Param("NameCode") String NameCode);

    @Query(value = "SELECT COALESCE(a.ItemCode, b.ItemCode) as ItemCode, " +
            "COALESCE(a.ItemName, b.ItemName) as ItemName, " +
            "b.Type as Type, b.Maker as Maker, b.Location as Location, " +
            "b.MOQ as MOQ, SUM(CASE WHEN a.Status = 1 THEN a.StockValue ELSE 0 END) as balance " +
            "FROM tbTransition a " +
            "FULL JOIN tbMasterItem b ON a.ItemCode = b.ItemCode WHERE b.ItemCode LIKE CONCAT('%', :NameCode,'%') OR b.ItemName LIKE CONCAT('%', :NameCode,'%') " +
            "GROUP BY COALESCE(a.ItemCode, b.ItemCode), COALESCE(a.ItemName, b.ItemName), " +
            "b.Type, b.Maker, b.Location, b.MOQ " +
            "ORDER BY COALESCE(a.ItemCode, b.ItemCode) ASC",  nativeQuery = true)
    List<Object[]> findBalance(@Param("NameCode") String NameCode);

    default List<BalanceDto> getTransitionBalance(String NameCode){
        List<Object[]> results = findBalance(NameCode);
        return (List<BalanceDto>) results.stream().map(objects -> new BalanceDto(
                (String) objects[0],
                (String) objects[1],
                (String) objects[2],
                (String) objects[3],
                (String) objects[4],
                objects[5] != null ? (Integer) objects[5] : 0,  // Set default value 0 for Integer
                objects[6] != null ? (Integer) objects[6] : 0
        )).collect(Collectors.toList());
    }
    @Query(value = "SELECT u.ItemCode, u.ItemName, SUM(CASE WHEN u.Status = 1 THEN u.StockValue ELSE 0 END) AS StockValue " +
            "FROM tbTransition u " +
            "WHERE u.ItemCode = :itemCode AND u.ItemName = :itemName " +
            "GROUP BY u.ItemCode, u.ItemName", nativeQuery = true)
    List<Object[]> findTransitionCompare(@Param("itemCode") String itemCode, @Param("itemName") String itemName);
    default TransitionDto getTransitionCompare(String itemCode, String itemName) {
        List<Object[]> results = findTransitionCompare(itemCode, itemName);
        if (results.isEmpty()) {
            String code = itemCode;
            String name = itemName;
            Double stockValue = 0.0;
            return new TransitionDto(code, name, stockValue);
        }
        Object[] result = results.get(0);
        String code = (String) result[0];
        String name = (String) result[1];
        Double stockValue = result[2] != null ? ((Number) result[2]).doubleValue() : 0.0;
        return new TransitionDto(code, name, stockValue);
    }
}
