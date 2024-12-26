package com.demo.order_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpa extends JpaRepository<OrderEntity, Long> {
    void deleteByOrderNumber(Long orderNumber);

    List<OrderEntity> findAllByStatus(String status);

}
