package com.demo.order_management_system.repository;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders") 
@Getter
@Setter
public class OrderEntity {
    
    @Id
    @Column(name = "order_number") 
    private Long orderNumber;
    
    @Column(name = "effective_date")  
    private String effectiveDate;

    @Column(name = "status") 
    private String status;
    
    @OneToMany(mappedBy = "orderNumber", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineEntity> lines;

}
