package com.example.sistema_adv_api.client;

import com.example.sistema_adv_api.storage.CloudflareStorageService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientFileRepository clientFileRepository;
    private final CloudflareStorageService storageService;

    public ClientService(ClientRepository clientRepository,
                         ClientFileRepository clientFileRepository,
                         CloudflareStorageService storageService) {
        this.clientRepository = clientRepository;
        this.clientFileRepository = clientFileRepository;
        this.storageService = storageService;
    }

    @Transactional
    public ClientResponse create(ClientRequest request) {
        if (clientRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Cliente com este e-mail já existe");
        }
        Client client = new Client(request.name(), request.email(), request.phone());
        Client saved = clientRepository.save(client);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> listAll() {
        return clientRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClientResponse findById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return toResponse(client);
    }

    @Transactional
    public ClientResponse update(UUID id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        client.setName(request.name());
        client.setEmail(request.email());
        client.setPhone(request.phone());
        return toResponse(client);
    }

    @Transactional
    public void delete(UUID id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
        clientRepository.deleteById(id);
    }

    @Transactional
    public ClientFileResponse uploadFile(UUID clientId, MultipartFile file) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        var uploaded = storageService.uploadClientFile(clientId, file);
        ClientFile saved = clientFileRepository.save(new ClientFile(
                client,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                uploaded.key(),
                uploaded.publicUrl()
        ));

        return toFileResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ClientFileResponse> listFiles(UUID clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Cliente não encontrado");
        }
        return clientFileRepository.findByClientId(clientId).stream().map(this::toFileResponse).toList();
    }

    private ClientResponse toResponse(Client client) {
        return new ClientResponse(client.getId(), client.getName(), client.getEmail(), client.getPhone(), client.getCreatedAt());
    }

    private ClientFileResponse toFileResponse(ClientFile clientFile) {
        return new ClientFileResponse(
                clientFile.getId(),
                clientFile.getFileName(),
                clientFile.getContentType(),
                clientFile.getSize(),
                clientFile.getUrl(),
                clientFile.getUploadedAt()
        );
    }
}
