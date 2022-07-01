package com.example.ch03.repo;

import com.example.ch03.model.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}
