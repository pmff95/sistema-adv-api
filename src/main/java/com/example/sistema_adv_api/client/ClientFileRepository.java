package com.example.sistema_adv_api.client;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientFileRepository extends JpaRepository<ClientFile, UUID> {
    List<ClientFile> findByClientId(UUID clientId);
}
