package com.bookstore.v1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user_id;
    private Date order_date;
    private Double total_amount;
    private String status;
    private String shipping_address;
    private String shipping_city;
    private String shipping_state;
    private String shipping_postal_code;
    private String shipping_country;
    private String shipping_method;
    private String tracking_number;
    private Date created_at;
    private Date updated_at;
}
