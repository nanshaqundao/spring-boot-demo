package com.example.ch07.repository;

import com.example.ch07.model.TacoUser;
import org.springframework.data.repository.CrudRepository;

public interface TacoUserRepository extends CrudRepository<TacoUser,Long> {
    TacoUser findByUsername(String username);
}
