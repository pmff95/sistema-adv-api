package com.example.sistema_adv_api.auth;

import com.example.sistema_adv_api.user.User;
import com.example.sistema_adv_api.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        var token = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(token);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getPermissions().stream().map(p -> p.getName()).collect(java.util.stream.Collectors.toSet())
        );
    }
}
