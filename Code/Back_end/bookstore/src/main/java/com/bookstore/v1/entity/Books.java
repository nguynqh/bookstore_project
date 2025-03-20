package com.bookstore.v1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;
    private String description;
    private Long publisher_id;
    private Date publication_date;
    private String language;
    private Integer page_count;
    private Double price;
    private Double discount_price;
    private Integer quantity;
    private String image_url;
    private Date created_at;
    private Date updated_at;
    private Boolean active;
}
