package com.demo.order_management_system.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "line") 
@Getter
@Setter
public class LineEntity {
    
    @Id
    @Column(name = "line_id")
    private Long lineId;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "line_status")
    private String lineStatus;

    @Column(name = "order_number")
    private Long orderNumber;

}