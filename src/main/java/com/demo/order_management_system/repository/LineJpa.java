package com.demo.order_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineJpa  extends JpaRepository<LineEntity, Long>{
    List<LineEntity> findAllByOrderNumber(Long orderNumber);
    
    void deleteByOrderNumberAndLineId(Long orderId, Long lineId);

    LineEntity findByOrderNumberAndLineId(Long orderId, Long lineId);
}
