package dsj.dvmManager.game;

import dsj.dvmManager.pgnParser.PgnResult;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerNotFoundException;
import dsj.dvmManager.player.PlayerService;
import dsj.dvmManager.swissChessImport.SwissChessGame;
import dsj.dvmManager.team.Team;
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

    public Game createGame(Player playerWhite, Player playerBlack, TeamMatch teamMatch, Integer boardNumber) {
        Game game = new Game();
        game.setPlayerWhite(playerWhite);
        game.setPlayerBlack(playerBlack);
        game.setTeamMatch(teamMatch);
        game.setResult(GameResult.UNKNOWN);
        game.setBoardNumber(boardNumber);
        return save(game);
    }

    public Game save(Game game) {
        return this.gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return this.gameRepository.findAll();
    }

    public GameDto createGameDto(Team teamHome, Game game) {
        GameDto dto = new GameDto();

        // Player Home is playing with white.
        if (game.getPlayerWhite().getTeam().equals(teamHome)) {
            dto.setPlayerHome(game.getPlayerWhite().getName());
            dto.setPlayerHomeColor(GameColor.WHITE);
            dto.setPlayerAway(game.getPlayerBlack().getName());
            dto.setPlayerAwayColor(GameColor.BLACK);
            dto.setResult(game.getResult());
        }

        // Player Home is playing with black.
        else {
            dto.setPlayerHome(game.getPlayerBlack().getName());
            dto.setPlayerHomeColor(GameColor.BLACK);
            dto.setPlayerAway(game.getPlayerWhite().getName());
            dto.setPlayerAwayColor(GameColor.WHITE);
            dto.setResult(game.getResult().getOppositeResult());
        }

        dto.setBoardNumber(game.getBoardNumber());
        dto.setLiChessGameId(game.getLiChessGameId());
        dto.setLiChessGameStatus(game.getLiChessGameStatus());
        dto.setLiChessGameMoves(game.getLiChessGameMoves());
        dto.setLiChessGameCreatedAt(game.getLiChessGameCreatedAt());
        dto.setLiChessGameLastMoveAt(game.getLiChessGameLastMoveAt());
        dto.setGame(game);
        return dto;
    }

    public Game createGameForSwissChessGame(SwissChessGame swissChessGame, TeamMatch teamMatch) throws TeamNotFoundException, PlayerNotFoundException {

        // Find PlayerWhite and PlayerBlack first.
        Player playerWhite = playerService.findByNameAndTeamName(swissChessGame.getPlayerNameWhite(), swissChessGame.getTeamNameWhite());
        Player playerBlack = playerService.findByNameAndTeamName(swissChessGame.getPlayerNameBlack(), swissChessGame.getTeamNameBlack());

        // Create and return Game.
        return createGame(playerWhite, playerBlack, teamMatch, swissChessGame.getBoardNumber());

    }

    public GameResult getGameResultFromPgnResult(PgnResult pgnResult) {
        if (pgnResult.equals(PgnResult.WHITE_WINS))
            return GameResult.WHITE_WINS;
        if (pgnResult.equals(PgnResult.BLACK_WINS))
            return GameResult.BLACK_WINS;
        if (pgnResult.equals(PgnResult.DRAW))
            return GameResult.DRAW;
        return GameResult.UNKNOWN;
    }

    public void deleteAll() {
        gameRepository.deleteAll();
        gameRepository.flush();
    }

}
