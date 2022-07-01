package com.example.ch03springdatajdbc.repository;


import com.example.ch03springdatajdbc.model.TacoOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {

}
