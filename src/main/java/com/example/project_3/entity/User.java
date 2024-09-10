package com.example.project_3.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    private String username;
    private String password;
    private String nickname;
    private String fullName;
    private Integer ageGroup;
    private String email;
    private String phone;
    private String profileImgUrl;
    private String authorities;

    // 사용자 전환 신청 여부
    private boolean businessApplication;
    @OneToMany(mappedBy = "owner",fetch = FetchType.LAZY)
    @JsonManagedReference
    private final List<Shop> ownedShops = new ArrayList<>();

}




