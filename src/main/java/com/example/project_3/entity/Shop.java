package com.example.project_3.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    @JsonManagedReference
    private final List<Product> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User owner;

    @Column(name = "recent_transaction")
    private LocalDateTime recentTransaction;



}
