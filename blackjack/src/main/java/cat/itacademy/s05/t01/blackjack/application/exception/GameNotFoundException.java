package cat.itacademy.s05.t01.blackjack.application.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String gameId) {
        super("Execution aborted: Game session with ID " + gameId + " does not exist.");
    }
}