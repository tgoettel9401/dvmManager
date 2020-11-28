package dsj.dvmManager.player;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException() {
        super("Player does not exist, please check the players");
    }
}
