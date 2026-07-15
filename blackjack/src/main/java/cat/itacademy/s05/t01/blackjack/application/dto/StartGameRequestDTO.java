package cat.itacademy.s05.t01.blackjack.application.dto;

import jakarta.validation.constraints.Size;

public record StartGameRequestDTO(
        @Size(max = 100, message = "Player name must not exceed 100 characters")
        String playerName
) {
}