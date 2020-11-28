package dsj.dvmManager;

import com.google.common.collect.Lists;
import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.liChessAdapter.*;
import dsj.dvmManager.pgnParser.PgnGame;
import dsj.dvmManager.pgnParser.PgnParserService;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerNotFoundException;
import dsj.dvmManager.player.PlayerService;
import dsj.dvmManager.swissChessImport.*;
import dsj.dvmManager.team.Team;
import dsj.dvmManager.team.TeamNotFoundException;
import dsj.dvmManager.team.TeamService;
import dsj.dvmManager.teamMatch.TeamMatch;
import dsj.dvmManager.teamMatch.TeamMatchDto;
import dsj.dvmManager.teamMatch.TeamMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DvmManagementService {

    private final Logger logger = LoggerFactory.getLogger(DvmManagementService.class);

    private final SwissChessLstImportService swissChessLstImportService;
    private final SwissChessPgnImportService swissChessPgnImportService;

    private final TeamService teamService;
    private final PlayerService playerService;

    private final GameService gameService;
    private final TeamMatchService teamMatchService;
    private final LiChessAdapter liChessService;
    private final PgnParserService pgnParserService;

    @Autowired
    public DvmManagementService(GameService gameService, TeamMatchService teamMatchService,
                                LiChessAdapter liChessService, PgnParserService pgnParserService,
                                SwissChessPgnImportService swissChessPgnImportService,
                                SwissChessLstImportService swissChessLstImportService,
                                TeamService teamService, PlayerService playerService) {
        this.gameService = gameService;
        this.teamMatchService = teamMatchService;
        this.liChessService = liChessService;
        this.pgnParserService = pgnParserService;
        this.swissChessLstImportService = swissChessLstImportService;
        this.swissChessPgnImportService = swissChessPgnImportService;
        this.teamService = teamService;
        this.playerService = playerService;
    }

    public List<LiChessChallenge> createChallenges() {
        List<Game> games = this.gameService.getAllGames();
        List<LiChessChallenge> challenges = Lists.newArrayList();
        for (Game game : games) {
            try {
                challenges.add(liChessService.createChallenge(game));
            }
            catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
        return challenges;
    }

    public List<String> getPlayersWithInvalidTokens() {

        List<String> playersWithInvalidTokens = Lists.newArrayList();

        List<Player> players = playerService.findAll();

        for (Player player : players) {
            try {
                liChessService.readAccount(player);
            }
            catch(LiChessAccountNotFoundException ex) {
                playersWithInvalidTokens.add(player.getName());
            }
            catch(Exception ex) {
                logger.error(ex.getMessage());
            }
        }

        return playersWithInvalidTokens;

    }

    public List<TeamMatchDto> findAllTeamMatchDtos() {
        return teamMatchService.findAllDtos();
    }

    public void updateGames() {
        List<Game> games = gameService.findAll();
        int updatedGameCounter = 0;
        for (Game game : games) {
            if (game.getLiChessGameId() != null) {
                LiChessGame liChessGame = liChessService.getLiChessGame(game);
                game.setLiChessGameStatus(liChessGame.getStatus());
                game.setLiChessGameMoves(liChessGame.getMoves());
                game.setLiChessGameCreatedAt(liChessGame.getCreatedAt());
                game.setLiChessGameLastMoveAt(liChessGame.getLastMoveAt());

                List<PgnGame> pgns = pgnParserService.createPgnListFromString(liChessGame.getPgn());
                if (pgns.stream().findFirst().isPresent())
                    game.setResult(gameService.getGameResultFromPgnResult(pgns.stream().findFirst().get().getResult()));

                gameService.save(game);
                updatedGameCounter++;
            }
        }
        logger.info("Updated " + updatedGameCounter + " games");
    }

    public List<Player> importSwissChessLstFile(InputStream inputStream) {

        gameService.deleteAll();
        teamMatchService.deleteAll();
        playerService.deleteAll();
        teamService.deleteAll();

        // Get result for import.
        SwissChessLstResult swissChessLstResult = swissChessLstImportService.importSwissChessLst(inputStream);

        // Import and save teams first
        List<SwissChessTeam> swissChessTeams = swissChessLstResult.getTeams();
        List<Team> teams = Lists.newArrayList();
        swissChessTeams.forEach(swissChessTeam -> teams.add(teamService.createNewTeamForSwissChessTeam(swissChessTeam)));

        // Then save and import Players. Find the correct team for every player.
        List<SwissChessPlayer> swissChessPlayers = swissChessLstResult.getPlayers();
        List<Player> players = Lists.newArrayList();
        swissChessPlayers.forEach(swissChessPlayer -> {
            Team teamOfPlayer = teams.stream()
                    .filter(
                            team -> team.getName().equals(swissChessPlayer.getTeamName()))
                    .findFirst()
                    .orElse(null);
            players.add(playerService.createPlayerForSwissChessPlayer(swissChessPlayer, teamOfPlayer));
        });

        return players;

    }

    public List<Game> importSwissChessPgnFile(InputStream inputStream, boolean withDelete) throws IOException {

        if (withDelete) {
            gameService.deleteAll();
            teamMatchService.deleteAll();
        }

        // Get result for import.
        SwissChessPgnResult swissChessPgnResult = swissChessPgnImportService.importSwissChessPgn(inputStream);

        // Import, save and return games.
        List<SwissChessGame> swissChessGames = swissChessPgnResult.getGames();
        List<Game> games = Lists.newArrayList();

        for (SwissChessGame swissChessGame : swissChessGames) {
            try {
                Team teamWhite = teamService.findByName(swissChessGame.getTeamNameWhite());
                Team teamBlack = teamService.findByName(swissChessGame.getTeamNameBlack());
                TeamMatch teamMatch = teamMatchService.findOrCreateTeamMatch(teamWhite, teamBlack);
                games.add(gameService.createGameForSwissChessGame(swissChessGame, teamMatch));
            } catch (TeamNotFoundException | PlayerNotFoundException exception) {
                exception.printStackTrace();
            }
        }

        return games;

    }

}
