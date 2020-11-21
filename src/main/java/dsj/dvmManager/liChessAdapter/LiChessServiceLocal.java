package dsj.dvmManager.liChessAdapter;

import com.github.javafaker.Faker;
import dsj.dvmManager.game.Game;
import dsj.dvmManager.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class LiChessServiceLocal implements LiChessAdapter {

    Logger logger = LoggerFactory.getLogger(LiChessServiceLocal.class);

    public LiChessChallenge createChallenge(Game game) {

        Player playerWhite = game.getPlayerWhite();
        Player playerBlack = game.getPlayerBlack();

        LiChessAccount accountPlayerWhite = readAccount(playerWhite);
        LiChessAccount accountPlayerBlack = readAccount(playerBlack);

        String logMessage = "LOCAL!! Creating challenge for players " +
                playerWhite.getLastName() +
                " (LiChess: " +
                accountPlayerWhite.getUsername() +
                "), " +
                playerBlack.getLastName() +
                " (LiChess: " +
                accountPlayerBlack.getUsername() +
                ") ";
        logger.info(logMessage);

        LiChessChallengeGame liChessGame = new LiChessChallengeGame();
        LiChessChallenge challenge = new LiChessChallenge();
        challenge.setGame(liChessGame);

        return challenge;
    }

    public LiChessAccount readAccount(Player player) {

        Faker faker = new Faker();

        LiChessAccount liChessAccount = new LiChessAccount();
        liChessAccount.setOnline(true);
        liChessAccount.setUsername(faker.name().username());

        return liChessAccount;

    }

    public LiChessGame getLiChessGame(Game game) {
        return new LiChessGame();
    }

}
