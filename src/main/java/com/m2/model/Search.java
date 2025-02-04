package com.m2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String keywords;
    private String location;
    private String categoryName;
    private String objectState;
    private String title;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "search")
    @JsonManagedReference
    private List<Notification> notifications = new ArrayList<>();
}
