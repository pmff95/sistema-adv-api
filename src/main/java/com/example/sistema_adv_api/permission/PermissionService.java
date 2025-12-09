package com.example.sistema_adv_api.permission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional
    public Set<Permission> findOrCreatePermissions(List<String> names) {
        Set<Permission> permissions = new HashSet<>();
        for (String name : names) {
            Permission permission = permissionRepository.findByName(name)
                    .orElseGet(() -> permissionRepository.save(new Permission(name, null)));
            permissions.add(permission);
        }
        return permissions;
    }

    public List<Permission> listAll() {
        return permissionRepository.findAll();
    }
}
