package com.m2.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Date publicationDate;

    @Column
    private String location;

    @Column
    private String deliveryOption;

    @ManyToOne
    private User user;

    @Column
    private String objectState;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String keywords;

}
