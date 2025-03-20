package com.bookstore.v1.entity;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "categories")
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Long parent_id;
    private Date created_at;
    private Date updated_at;
}
