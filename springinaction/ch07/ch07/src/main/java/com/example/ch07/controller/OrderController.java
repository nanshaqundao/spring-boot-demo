package com.example.ch07.controller;


import com.example.ch07.config.TacoOrderConfig;
import com.example.ch07.model.TacoOrder;
import com.example.ch07.model.TacoUser;
import com.example.ch07.repository.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    private final TacoOrderConfig tacoOrderConfig;

    private final OrderRepository orderRepo;

    public OrderController(TacoOrderConfig tacoOrderConfig, OrderRepository orderRepo) {
        this.tacoOrderConfig = tacoOrderConfig;
        this.orderRepo = orderRepo;
    }

    @GetMapping("/current")
    public String orderForm(@AuthenticationPrincipal TacoUser tacoUser, @ModelAttribute TacoOrder tacoOrder) {
        if (tacoOrder.getDeliveryName() == null) {
            tacoOrder.setDeliveryName(tacoUser.getFullname());
        }
        if (tacoOrder.getDeliveryStreet() == null) {
            tacoOrder.setDeliveryStreet(tacoUser.getStreet());
        }
        if (tacoOrder.getDeliveryCity() == null) {
            tacoOrder.setDeliveryCity(tacoUser.getCity());
        }
        if (tacoOrder.getDeliveryState() == null) {
            tacoOrder.setDeliveryState(tacoUser.getState());
        }
        if (tacoOrder.getDeliveryZip() == null) {
            tacoOrder.setDeliveryZip(tacoUser.getZip());
        }
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

        Pageable pageable = PageRequest.of(0, tacoOrderConfig.getPageSize());
        model.addAttribute("tacoOrders",
                orderRepo.findByTacoUserOrderByCreatedAtDesc(tacoUser, pageable));
        System.out.println("orders for user, pagesize: " + tacoOrderConfig.getPageSize());
        ;
        return "orderList";
    }

}
