package com.example.ch07.repository;

import com.example.ch07.model.Taco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TacoRepository
        extends PagingAndSortingRepository<Taco, Long>, CrudRepository<Taco, Long> {

}
