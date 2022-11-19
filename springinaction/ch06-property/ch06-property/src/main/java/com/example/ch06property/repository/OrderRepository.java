package com.example.ch06property.repository;

import com.example.ch06property.model.TacoOrder;
import com.example.ch06property.model.TacoUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);
    List<TacoOrder> findByTacoUserOrderByCreatedAtDesc(
            TacoUser tacoUser, Pageable pageable);
}
