package com.example.sistema_adv_api.user;

import com.example.sistema_adv_api.permission.PermissionService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PermissionService permissionService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionService = permissionService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        User user = new User(request.name(), request.email(), passwordEncoder.encode(request.password()), request.role());
        if (request.permissions() != null) {
            user.setPermissions(permissionService.findOrCreatePermissions(request.permissions()));
        }
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public UserResponse updatePermissions(UUID userId, UpdatePermissionsRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        Set<String> names = request.permissions().stream().collect(Collectors.toSet());
        user.setPermissions(permissionService.findOrCreatePermissions(names.stream().toList()));
        return toResponse(user);
    }

    private UserResponse toResponse(User user) {
        Set<String> permissions = user.getPermissions().stream()
                .map(permission -> permission.getName())
                .collect(Collectors.toSet());
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), permissions);
    }
}
