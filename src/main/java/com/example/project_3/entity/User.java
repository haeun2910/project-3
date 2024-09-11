package com.example.project_3.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "user_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

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

    private boolean active;
    @OneToMany(mappedBy = "owner",fetch = FetchType.LAZY)
    @JsonManagedReference
    private final List<Shop> ownedShops = new ArrayList<>();

    @PostLoad
    public void updateActiveStatus() {
        // Check the role and set the 'active' field accordingly
        if (this.authorities.contains("ROLE_DEFAULT") || !this.authorities.isEmpty()) {
            this.active = true;
        } else {
            this.active = false;
        }
    }
}




