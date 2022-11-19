package com.example.ch06property.controller;


import com.example.ch06property.model.TacoOrder;
import com.example.ch06property.model.TacoUser;
import com.example.ch06property.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/current")
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors,
                               SessionStatus sessionStatus,
                               @AuthenticationPrincipal TacoUser tacoUser) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        order.setTacoUser(tacoUser);
        orderRepo.save(order);
        sessionStatus.setComplete();

        return "redirect:/";
    }

    @GetMapping
    public String ordersForUser(
            @AuthenticationPrincipal TacoUser tacoUser, Model model) {

        Pageable pageable = PageRequest.of(0, 20);
        model.addAttribute("tacoOrders",
                orderRepo.findByTacoUserOrderByCreatedAtDesc(tacoUser, pageable));
        System.out.println("orders for user");;
        return "orderList";
    }

}
