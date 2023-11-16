package by.sapra.restclientservice.web.controller.v1;

import by.sapra.restclientservice.mapper.v1.ClientMapper;
import by.sapra.restclientservice.service.ClintService;
import by.sapra.restclientservice.web.model.ClientListResponse;
import by.sapra.restclientservice.web.model.ClintResponse;
import by.sapra.restclientservice.web.model.ErrorResponse;
import by.sapra.restclientservice.web.model.UpsertClientRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@Tag(name = "Client V1", description = "Client API version V1")
public class ClientController {
    private final ClintService inMemoryClintService;
    private final ClientMapper clientMapper;

    @Operation(
            summary = "Get all client.",
            description = "Get all client. Return list of id, name and orders.",
            tags = {"client"}
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = ClientListResponse.class))
    )
    @GetMapping()
    public ResponseEntity<ClientListResponse> findAll() {
        return ResponseEntity.ok().body(
                clientMapper.clientListToClientListResponse(inMemoryClintService.findAll())
        );
    }

    @Operation(
            summary = "Get client by ID.",
            description = "Get client by ID. Return id, name and orders.",
            tags = {"client", "id"}
    )
    @ApiResponses({
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = ClintResponse.class), mediaType = "application/json")
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = {
                                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                            }
                    )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClintResponse> findBiId(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok(
                clientMapper.clientToResponse(inMemoryClintService.findById(clientId))
        );
    }

    @Operation(
            summary = "Update client by ID",
            description = "Update client by ID. Return id, name and orders.",
            tags = {"client", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = ClintResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Not correct client request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClintResponse> updateClient(
            @PathVariable("id") Long clientId,
            @RequestBody @Valid UpsertClientRequest request) {
        return ResponseEntity.ok(
                clientMapper.clientToResponse(
                        inMemoryClintService.update(clientMapper.requestToClient(clientId, request))
                )
        );
    }

    @Operation(
            summary = "Save new client",
            description = "Save new client. Return id, name and orders.",
            tags = {"client"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = ClintResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Not correct client request",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )
    })
    @PostMapping
    public ResponseEntity<ClintResponse> create(@RequestBody @Valid UpsertClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clientMapper.clientToResponse(inMemoryClintService.save(clientMapper.requestToClient(request)))
        );
    }

    @Operation(
            summary = "Delete client by ID",
            description = "Delete client by Id. Return no content",
            tags = {"client", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(schema = @Schema(
                            implementation = ErrorResponse.class),
                            mediaType = "application/json"
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long clientId) {
        inMemoryClintService.deleteById(clientId);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
