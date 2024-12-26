package com.demo.order_management_system.controller;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.order_management_system.repository.LineEntity;
import com.demo.order_management_system.repository.OrderEntity;
import com.demo.order_management_system.service.OrderService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Mono<List<OrderEntity>> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderNumber}")
    public Mono<OrderEntity> getOrderByOrderNumber(@PathVariable Long orderNumber){
        return orderService.findOrderByOrderNumber(orderNumber);
            
    }

    @GetMapping("/status/{orderStatus}")
    public Mono<List<OrderEntity>> getAllOrdersByStatus(@PathVariable String orderStatus){
        return orderService.findAllOrdersByStatus(orderStatus);
    }

    @PutMapping("/{orderNumber}")
    public Mono<OrderEntity> updateOrder(
            @PathVariable Long orderNumber, @RequestBody OrderEntity updatedOrder) {
        return orderService.updateOrder(orderNumber, updatedOrder);
    }

    @PostMapping
    public Mono<OrderEntity> createOrder(@RequestBody OrderEntity order) {
        return orderService.createOrder(order);
    }

    @DeleteMapping("/{orderNumber}")
    public Mono<Void> deleteOrderByOrderNumber(@PathVariable Long orderNumber) {
        return orderService.deleteOrderByOrderNumber(orderNumber);
    }   
    
    @DeleteMapping("/{orderId}/lines/{lineId}")
    public Mono<Void> deleteLine(@PathVariable Long orderId, @PathVariable Long lineId) {
        return orderService.deleteLine(orderId, lineId);
    }

    @PutMapping("/{orderId}/lines/{lineId}")
    public Mono<LineEntity> updateLine(@PathVariable Long orderId, @PathVariable Long lineId, @RequestBody LineEntity lineEntity) {
       return orderService.updateLine(orderId, lineId, lineEntity);
    }
}