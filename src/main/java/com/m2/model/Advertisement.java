package com.m2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column
    private String objectState;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    private String keywords;
    @OneToMany(mappedBy = "advertisement", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();
}
