package com.marketplace.controller;

import com.marketplace.dto.ApiResponse;
import com.marketplace.model.ServiceOrder;
import com.marketplace.model.User;
import com.marketplace.service.AuthService;
import com.marketplace.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthService authService;

    @PostMapping("/accept/{offerId}")
    public ResponseEntity<ApiResponse<ServiceOrder>> acceptOffer(
            @PathVariable Long offerId,
            Authentication authentication) {
        User customer = authService.getUserByEmail(authentication.getName());
        ServiceOrder order = orderService.acceptOfferAndCreateOrder(offerId, customer);
        return ResponseEntity.ok(ApiResponse.ok("Offer accepted and order created successfully!", order));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<ServiceOrder>> completeOrder(
            @PathVariable Long id,
            Authentication authentication) {
        User customer = authService.getUserByEmail(authentication.getName());
        ServiceOrder order = orderService.completeOrder(id, customer);
        return ResponseEntity.ok(ApiResponse.ok("Order marked as completed!", order));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ServiceOrder>>> getMyOrders(Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        List<ServiceOrder> orders = orderService.getMyOrders(user);
        return ResponseEntity.ok(ApiResponse.ok("My orders retrieved", orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceOrder>> getOrderById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = authService.getUserByEmail(authentication.getName());
        ServiceOrder order = orderService.getOrderById(id, user);
        return ResponseEntity.ok(ApiResponse.ok("Order details retrieved", order));
    }
}
