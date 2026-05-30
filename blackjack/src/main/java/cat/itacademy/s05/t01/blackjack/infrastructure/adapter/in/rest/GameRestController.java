package cat.itacademy.s05.t01.blackjack.infrastructure.adapter.in.rest;

import cat.itacademy.s05.t01.blackjack.application.dto.GameResponseDTO;

import cat.itacademy.s05.t01.blackjack.application.usecase.GetGameUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.PlayerHitUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.PlayerStandUseCase;
import cat.itacademy.s05.t01.blackjack.application.usecase.StartGameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    public GameRestController(
            StartGameUseCase startGameUseCase,
            PlayerHitUseCase playerHitUseCase,
            PlayerStandUseCase playerStandUseCase,
            GetGameUseCase getGameUseCase
    ) {
        this.startGameUseCase = startGameUseCase;
        this.playerHitUseCase = playerHitUseCase;
        this.playerStandUseCase = playerStandUseCase;
        this.getGameUseCase = getGameUseCase;
    }

    @PostMapping
    @Operation(summary = "Start a new game", description = "Initializes a new Blackjack game, deals initial cards, and returns the starting state.")
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
    @Operation(summary = "Get game state", description = "Retrieves the current state of a specific game by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Game retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })

    public ResponseEntity<GameResponseDTO> getGame(
            @Parameter(description = "The unique identifier of the game")
            @PathVariable String id) {

        GameResponseDTO response = getGameUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/hit")
    @Operation(summary = "Player hits", description = "Deals an additional card to the player. Automatically evaluates for bust or 21.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card dealt successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid action (e.g., game already finished)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })

    public ResponseEntity<GameResponseDTO> playerHit(
            @Parameter(description = "The unique identifier of the game ") @PathVariable String id) {
        GameResponseDTO response = playerHitUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/stand")
    @Operation(summary = "Player stands", description = "Ends the player's turn, trigger the dealer's automated play, and resolves the game outcome.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player stood successfully; dealer played and game resolved"),
            @ApiResponse(responseCode = "400", description = "Invalid action (e.g., game already finished", content = @Content),
            @ApiResponse(responseCode = "404", description = "Game not found", content = @Content)
    })

    public ResponseEntity<GameResponseDTO> playerStand(
            @Parameter(description = "The unique identifier of the game") @PathVariable String id) {
        GameResponseDTO response = playerStandUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}