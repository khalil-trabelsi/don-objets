package com.m2.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
    private Date publication_date;

    @Column
    private String location;

    @Column
    private String delivery_option;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String object_state;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


}
