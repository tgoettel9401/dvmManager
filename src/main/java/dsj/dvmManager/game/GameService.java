package dsj.dvmManager.game;

import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerNotFoundException;
import dsj.dvmManager.player.PlayerService;
import dsj.dvmManager.swissChessImport.SwissChessGame;
import dsj.dvmManager.team.TeamNotFoundException;
import dsj.dvmManager.teamMatch.TeamMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerService playerService;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.playerService = playerService;
    }

    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game createGame(Player playerWhite, Player playerBlack, TeamMatch teamMatch) {
        Game game = new Game();
        game.setPlayerWhite(playerWhite);
        game.setPlayerBlack(playerBlack);
        game.setTeamMatch(teamMatch);
        game.setResult("");
        return save(game);
    }

    public Game save(Game game) {
        return this.gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return this.gameRepository.findAll();
    }

    public GameDto createGameDto(Game game) {
        GameDto dto = new GameDto();
        dto.setPlayerWhite(game.getPlayerWhite().getName());
        dto.setPlayerBlack(game.getPlayerBlack().getName());
        dto.setResult(game.getResult());
        dto.setLiChessGameId(game.getLiChessGameId());
        dto.setLiChessGameStatus(game.getLiChessGameStatus());
        dto.setLiChessGameMoves(game.getLiChessGameMoves());
        dto.setLiChessGameCreatedAt(game.getLiChessGameCreatedAt());
        dto.setLiChessGameLastMoveAt(game.getLiChessGameLastMoveAt());
        return dto;
    }

    public Game createGameForSwissChessGame(SwissChessGame swissChessGame, TeamMatch teamMatch) throws TeamNotFoundException, PlayerNotFoundException {

        // Find PlayerWhite and PlayerBlack first.
        Player playerWhite = playerService.findByNameAndTeamName(swissChessGame.getPlayerNameWhite(), swissChessGame.getTeamNameWhite());
        Player playerBlack = playerService.findByNameAndTeamName(swissChessGame.getPlayerNameBlack(), swissChessGame.getTeamNameBlack());

        // Create and return Game.
        return createGame(playerWhite, playerBlack, teamMatch);

    }

}
