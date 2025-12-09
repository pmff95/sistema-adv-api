package com.example.sistema_adv_api.config;

import com.example.sistema_adv_api.permission.Permission;
import com.example.sistema_adv_api.permission.PermissionRepository;
import com.example.sistema_adv_api.user.Role;
import com.example.sistema_adv_api.user.User;
import com.example.sistema_adv_api.user.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PermissionRepository permissionRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void seed() {
        List<String> defaultPermissions = List.of(
                "CLIENT_CREATE",
                "CLIENT_UPDATE",
                "CLIENT_DELETE",
                "CLIENT_FILE_UPLOAD",
                "CLIENT_FILE_VIEW",
                "PROCESS_VIEW",
                "PROCESS_EDIT"
        );
        defaultPermissions.forEach(name -> {
            if (!permissionRepository.existsByName(name)) {
                permissionRepository.save(new Permission(name, "Permissão padrão"));
            }
        });

        if (userRepository.count() == 0) {
            User admin = new User("Administrador", "admin@sistema.com", passwordEncoder.encode("admin123"), Role.ADMIN);
            admin.setPermissions(new java.util.HashSet<>(permissionRepository.findAll()));
            userRepository.save(admin);
        }
    }
}
