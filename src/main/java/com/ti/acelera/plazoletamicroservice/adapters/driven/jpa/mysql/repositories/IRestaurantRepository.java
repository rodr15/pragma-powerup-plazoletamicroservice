package com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.repositories;


import com.ti.acelera.plazoletamicroservice.adapters.driven.jpa.mysql.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    @Query("SELECT r FROM RestaurantEntity r WHERE FIND_IN_SET(:employeeId, r.employees) >= 0")
    Optional<RestaurantEntity> findRestaurantsByEmployeeId(@Param("employeeId") String employeeId);
}
