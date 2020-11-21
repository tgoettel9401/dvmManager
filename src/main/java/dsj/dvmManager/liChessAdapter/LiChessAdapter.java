package dsj.dvmManager.liChessAdapter;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.player.Player;

public interface LiChessAdapter {
    LiChessChallenge createChallenge(Game game);

    LiChessAccount readAccount(Player player);

    LiChessGame getLiChessGame(Game game);
}
