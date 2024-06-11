package com.random.teams.models;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private User user;
}
