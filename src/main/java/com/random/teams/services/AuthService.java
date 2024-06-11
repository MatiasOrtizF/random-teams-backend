package com.random.teams.services;

import com.random.teams.models.LoginRequest;
import com.random.teams.models.LoginResponse;
import com.random.teams.models.User;
import com.random.teams.repositories.UserRepository;
import com.random.teams.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    @Autowired
    public AuthService(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public boolean validationToken(String token) {
        String userId = jwtUtil.getKey(token);
        return (userId != null);
    }

    public User validationEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public LoginResponse validationCredentials(LoginRequest loginRequest) {
        User userLogged = validationEmail(loginRequest.getEmail());
        if (userLogged != null) {
            String passwordHashed = userLogged.getPassword();

            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            if (argon2.verify(passwordHashed, loginRequest.getPassword())) {
                String tokenJWT = jwtUtil.create(userLogged.getId().toString(), userLogged.getEmail());

                LoginResponse response = new LoginResponse();
                response.setToken(tokenJWT);
                response.setUser(userLogged);

                return response;
            }
        }
        throw new RuntimeException("Dni or password is incorrect");
    }

    public Long getUserId(String token) {
        String userId = jwtUtil.getKey(token);
        return Long.valueOf(userId);
    }
}
