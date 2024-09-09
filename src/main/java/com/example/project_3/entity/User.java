package com.example.project_3.entity;

import jakarta.persistence.*;
import lombok.*;

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

    public boolean isActiveUser(){
        return this.authorities != "ROLE_DEFAULT";
    }
}




