package com.example.project_3.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Shop extends BaseEntity{
    private String name;
    private String description;
    private String category;
    private boolean openStatus;
    private boolean applicationSubmitted;
    private boolean closeRequested;
    private String closeReason;
    @OneToMany(mappedBy = "shopItem", fetch = FetchType.LAZY)
    private final List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;



}
