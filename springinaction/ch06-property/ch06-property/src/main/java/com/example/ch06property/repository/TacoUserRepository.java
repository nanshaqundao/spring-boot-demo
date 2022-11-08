package com.example.ch06property.repository;

import com.example.ch06property.model.TacoUser;
import org.springframework.data.repository.CrudRepository;

public interface TacoUserRepository extends CrudRepository<TacoUser,Long> {
    TacoUser findByUsername(String username);
}
