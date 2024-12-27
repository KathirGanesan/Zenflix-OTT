package com.zenflix.ott.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password; // For creating/updating users
    private List<String> roles; // Role names like ROLE_USER, ROLE_ADMIN
    private Boolean deleted;
}