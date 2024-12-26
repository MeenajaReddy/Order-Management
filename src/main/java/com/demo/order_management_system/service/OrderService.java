package com.demo.order_management_system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.demo.order_management_system.repository.LineEntity;
import com.demo.order_management_system.repository.LineJpa;
import com.demo.order_management_system.repository.OrderEntity;
import com.demo.order_management_system.repository.OrderJpa;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

    private final OrderJpa orderRepository;
    private final LineJpa lineRepository;

    public OrderService(OrderJpa orderRepository, LineJpa lineRepository){
        this.orderRepository = orderRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Mono<OrderEntity> createOrder(OrderEntity order) {
        List<LineEntity> lines = new ArrayList<>();
        for(LineEntity val : order.getLines()){
            LineEntity entity = new LineEntity();
            entity.setLineId(val.getLineId());
            entity.setLineStatus(val.getLineStatus());
            entity.setOrderType(val.getOrderType());
            entity.setOrderNumber(order.getOrderNumber());
            lineRepository.save(entity);
            lines.add(entity);
        }
        order.setLines(lines);

        OrderEntity savedOrder = orderRepository.save(order);
        return Mono.just(savedOrder);
    }

    @Transactional
    public Mono<OrderEntity> updateOrder(Long orderNumber, OrderEntity updatedOrder) {
        return Mono.justOrEmpty(orderRepository.findById(orderNumber))
            .map(existingOrder -> {
                existingOrder.setEffectiveDate(updatedOrder.getEffectiveDate());
                existingOrder.setStatus(updatedOrder.getStatus());
                List<LineEntity> updatedLines = new ArrayList<>();
                for (LineEntity updatedLine : updatedOrder.getLines()) {
                    LineEntity existingLine = existingOrder.getLines().stream()
                            .filter(line -> line.getLineId().equals(updatedLine.getLineId()))
                            .findFirst()
                            .orElse(new LineEntity());
    
                    existingLine.setLineId(updatedLine.getLineId());
                    existingLine.setLineStatus(updatedLine.getLineStatus());
                    existingLine.setOrderType(updatedLine.getOrderType());
                    existingLine.setOrderNumber(existingOrder.getOrderNumber());
                    lineRepository.save(existingLine);
                updatedLines.add(existingLine);
                }
                existingOrder.setLines(updatedLines);

                existingOrder.setLines(updatedLines);

                return orderRepository.save(existingOrder);
            }) .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found with orderNumber: " + orderNumber)));
    }

    @Transactional
    public Mono<List<OrderEntity>> getAllOrders(){
        return Mono.just( orderRepository.findAll()).
            map(orderList -> {
                orderList.forEach(order -> {
                    List<LineEntity> lines = findAllListsByOrderNumber(order.getOrderNumber());
                    order.setLines(lines);
                });
                return orderList;
            });
    }

    @Transactional
    public Mono<List<OrderEntity>> findAllOrdersByStatus(String status){
        return Mono.justOrEmpty(orderRepository.findAllByStatus(status)) 
        .map(orderList -> {
            orderList.forEach(order -> {
                List<LineEntity> lines = findAllListsByOrderNumber(order.getOrderNumber());
                order.setLines(lines);
            });
            return orderList;
        })
        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found with orderNumber: " + status)));
    }

    @Transactional
    public Mono<OrderEntity> findOrderByOrderNumber(long orderNumber){
        return Mono.justOrEmpty(orderRepository.findById(orderNumber))
            .map(entity -> {
                entity.setLines(findAllListsByOrderNumber(orderNumber));
                return entity;
            })
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found with orderNumber: " + orderNumber)));
    }

    @Transactional
    public Mono<Void> deleteOrderByOrderNumber(Long orderNumber) {
        return Mono.just(orderRepository.existsById(orderNumber))
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found with orderNumber: " + orderNumber));
                }
                orderRepository.deleteByOrderNumber(orderNumber);
                return Mono.empty();
            });
    }

    public List<LineEntity> findAllListsByOrderNumber(long orderNumber) {
        return lineRepository.findAllByOrderNumber(orderNumber);
    }

    @Transactional
    public Mono<Void> deleteLine(Long orderId, Long lineId) {
        return Mono.just(lineRepository.existsById(lineId))
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "line doesn't exist: " + lineId));
                }
                lineRepository.deleteByOrderNumberAndLineId(orderId, lineId);
                return Mono.empty();
            });
    }

    @Transactional
    public Mono<LineEntity> updateLine(Long orderId, Long lineId, LineEntity lineEntity) {
        return Mono.justOrEmpty(lineRepository.findByOrderNumberAndLineId(orderId, lineId))
            .map(existingLine -> {
                existingLine.setOrderType(lineEntity.getOrderType());
                existingLine.setLineStatus(lineEntity.getLineStatus());
                return lineRepository.save(existingLine); 
            })
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Line not found")));
    }
}