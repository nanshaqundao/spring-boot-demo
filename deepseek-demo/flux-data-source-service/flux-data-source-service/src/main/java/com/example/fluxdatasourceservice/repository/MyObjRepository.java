package com.example.fluxdatasourceservice.repository;

import com.example.fluxdatasourceservice.model.MyObj;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MyObjRepository extends JpaRepository<MyObj, Long> {

    List<MyObj> findByName(String name, Pageable pageable);

    @Query("SELECT COUNT(o) FROM MyObj o WHERE o.name = :name")
    long countByName(String name);
}