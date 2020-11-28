package dsj.dvmManager.liChessAdapter;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.player.Player;

public interface LiChessAdapter {
    LiChessChallenge createChallenge(Game game) throws LiChessAccountNotFoundException;

    LiChessAccount readAccount(Player player) throws LiChessAccountNotFoundException;

    LiChessGame getLiChessGame(Game game);
}
