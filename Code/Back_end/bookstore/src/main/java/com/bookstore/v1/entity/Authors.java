package com.bookstore.v1.entity;

import lombok.Setter;
import lombok.Getter;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "authors")
public class Authors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String biography;
    private Date birth_date;
    private String country;
    private Date created_at;
    private Date updated_at;
}
