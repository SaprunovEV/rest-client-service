package by.sapra.restclientservice.web.v1;

import by.sapra.restclientservice.mapper.v1.ClientMapper;
import by.sapra.restclientservice.service.ClintService;
import by.sapra.restclientservice.web.model.ClientListResponse;
import by.sapra.restclientservice.web.model.ClintResponse;
import by.sapra.restclientservice.web.model.UpsertClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClintService clintService;
    private final ClientMapper clientMapper;

    @GetMapping()
    public ResponseEntity<ClientListResponse> findAll() {
        return ResponseEntity.ok().body(
                clientMapper.clientListToClientListResponse(clintService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClintResponse> findBiId(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok(
                clientMapper.clientToResponse(clintService.findById(clientId))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClintResponse> updateClient(
            @PathVariable("id") Long clientId,
            @RequestBody UpsertClientRequest request) {
        return ResponseEntity.ok(
                clientMapper.clientToResponse(
                        clintService.update(clientMapper.requestToClient(clientId, request))
                )
        );
    }

    @PostMapping
    public ResponseEntity<ClintResponse> create(@RequestBody UpsertClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientMapper.clientToResponse(clintService.save(clientMapper.requestToClient(request)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long clientId) {
        clintService.deleteById(clientId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
