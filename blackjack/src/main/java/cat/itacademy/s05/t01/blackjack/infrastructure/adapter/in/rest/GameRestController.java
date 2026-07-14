package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.in.rest;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;
import cat.itacademy.s05.t01.blackjack.application.dto.PlayerRenameRequestDTO;
import cat.itacademy.s05.t01.blackjack.application.usecase.GetGameUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.PlayerHitUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.PlayerStandUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.RenamePlayerUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.StartGameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Blackjack Games", description = "Endpoints for managing the core Blackjack game loop")
public class GameRestController {

    private final StartGameUseCase startGameUseCase;
    private final PlayerHitUseCase playerHitUseCase;
    private final PlayerStandUseCase playerStandUseCase;
    private final GetGameUseCase getGameUseCase;
    private final RenamePlayerUseCase renamePlayerUseCase;

    public GameRestController(
            StartGameUseCase startGameUseCase,
            PlayerHitUseCase playerHitUseCase,
            PlayerStandUseCase playerStandUseCase,
            GetGameUseCase getGameUseCase,
            RenamePlayerUseCase renamePlayerUseCase
    ) {
        this.startGameUseCase = startGameUseCase;
        this.playerHitUseCase = playerHitUseCase;
        this.playerStandUseCase = playerStandUseCase;
        this.getGameUseCase = getGameUseCase;
        this.renamePlayerUseCase = renamePlayerUseCase;
    }

    @PostMapping
    @Operation(summary = "Start a new game")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Game successfully created",
                    content = @Content(schema = @Schema(implementation = GameResponseDTO.class)))
    })
    public ResponseEntity<GameResponseDTO> createGame() {
        GameResponseDTO response = startGameUseCase.execute();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.gameId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get game state")
    public ResponseEntity<GameResponseDTO> getGame(
            @Parameter(description = "The unique identifier of the game") @PathVariable String id) {
        return ResponseEntity.ok(getGameUseCase.execute(id));
    }

    @PostMapping("/{id}/hit")
    @Operation(summary = "Player hits")
    public ResponseEntity<GameResponseDTO> playerHit(
            @Parameter(description = "The unique identifier of the game") @PathVariable String id) {
        return ResponseEntity.ok(playerHitUseCase.execute(id));
    }

    @PostMapping("/{id}/stand")
    @Operation(summary = "Player stands")
    public ResponseEntity<GameResponseDTO> playerStand(
            @Parameter(description = "The unique identifier of the game") @PathVariable String id) {
        return ResponseEntity.ok(playerStandUseCase.execute(id));
    }

    @PutMapping("/{id}/player")
    @Operation(summary = "Rename the player of a game")
    public ResponseEntity<GameResponseDTO> renamePlayer(
            @Parameter(description = "The unique identifier of the game") @PathVariable String id,
            @Valid @RequestBody PlayerRenameRequestDTO request) {
        return ResponseEntity.ok(renamePlayerUseCase.execute(id, request.playerName()));
    }
}