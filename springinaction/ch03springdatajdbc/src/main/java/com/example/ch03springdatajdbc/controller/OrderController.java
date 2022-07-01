package com.example.ch03springdatajdbc.controller;

import com.example.ch03springdatajdbc.model.TacoOrder;
import com.example.ch03springdatajdbc.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(
            @Valid TacoOrder tacoOrder, Errors errors,
            SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        tacoOrder.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        orderRepository.save(tacoOrder);

        log.info("Order Submitted: {}", tacoOrder);
        sessionStatus.setComplete();

        return "redirect:/";
    }
}
