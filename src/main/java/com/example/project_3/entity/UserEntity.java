package com.example.project_3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "user_table")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String username;
    @Setter
    private String password;

    @Setter
    // ROLE_USER,READ,WRITE 등으로 권한을 관리한다.
    private String authorities;
}
