package com.example.project_3;

import com.example.project_3.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class UserDto {
    private Long id;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private String passCheck;
    @Setter
    private String nickname;
    @Setter
    private String fullName;
    @Setter
    private Integer ageGroup;
    @Setter
    private String email;
    @Setter
    private String phone;
    @Setter
    private String profileImgUrl;
    @Setter
    private String authorities;
    @Setter
    private boolean active;
    @Setter
    private boolean businessApplication;

    public static UserDto fromEntity(User entity) {
        UserDto dto = new UserDto();
        dto.id = entity.getId();
        dto.username = entity.getUsername();
        dto.password = "********";
        dto.nickname = entity.getNickname();
        dto.fullName = entity.getFullName();
        dto.ageGroup = entity.getAgeGroup();
        dto.email = entity.getEmail();
        dto.phone = entity.getPhone();
        dto.profileImgUrl = entity.getProfileImgUrl();
        dto.authorities = entity.getAuthorities();
        dto.active = entity.isActive();
        dto.businessApplication = entity.isBusinessApplication();
        return dto;

    }


}
