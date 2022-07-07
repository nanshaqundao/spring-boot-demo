package com.example.ch05basic.repository;

import com.example.ch05basic.model.TacoUser;
import org.springframework.data.repository.CrudRepository;

public interface TacoUserRepository extends CrudRepository<TacoUser,Long> {
    TacoUser findByUsername(String username);
}
