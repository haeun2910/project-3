package com.example.project_3.entity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Shop extends BaseEntity{
    private String name;
    private String description;


}
