package dsj.dvmManager;

import com.google.common.collect.Lists;
import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.pgnParser.PgnResult;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerNotFoundException;
import dsj.dvmManager.player.PlayerService;
import dsj.dvmManager.swissChessImport.*;
import dsj.dvmManager.team.Team;
import dsj.dvmManager.team.TeamNotFoundException;
import dsj.dvmManager.team.TeamService;
import dsj.dvmManager.teamMatch.TeamMatch;
import dsj.dvmManager.teamMatch.TeamMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class DvmManagementServiceTest {

    @InjectMocks
    private DvmManagementService dvmManagementService;

    @Mock
    private SwissChessPgnImportService swissChessPgnImportService;

    @Mock
    private SwissChessLstImportService swissChessLstImportService;

    @Mock
    private TeamService teamService;

    @Mock
    private TeamMatchService teamMatchService;

    @Mock
    private PlayerService playerService;

    @Mock
    private GameService gameService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void importSwissChessLstFile() {

        List<Team> correctTeams = Lists.newArrayList();
        List<SwissChessTeam> swTeams = Lists.newArrayList();
        for (int i = 0; i <= 4; i++) {
            String teamName = "Team " + i;
            SwissChessTeam swTeam = mock(SwissChessTeam.class);
            when(swTeam.getTeamName()).thenReturn(teamName);
            swTeams.add(swTeam);

            Team team = mock(Team.class);
            when(team.getName()).thenReturn(teamName);
            correctTeams.add(team);

            when(teamService.createNewTeamForSwissChessTeam(swTeam)).thenReturn(team);
        }

        List<Player> correctPlayers = Lists.newArrayList();
        List<SwissChessPlayer> swPlayers = Lists.newArrayList();

        for (int i = 0; i <= 15; i++) {
            String playerName = "Player " + i;
            Team team = correctTeams.get(i % 4);
            String teamName = team.getName();
            SwissChessPlayer swPlayer = mock(SwissChessPlayer.class);
            when(swPlayer.getPlayerName()).thenReturn(playerName);
            when(swPlayer.getTeamName()).thenReturn(teamName);
            swPlayers.add(swPlayer);

            Player player = mock(Player.class);
            when(player.getName()).thenReturn(playerName);
            when(player.getTeam()).thenReturn(team);
            correctPlayers.add(player);

            when(playerService.createPlayerForSwissChessPlayer(swPlayer, team)).thenReturn(player);
        }

        InputStream inputStream = mock(InputStream.class);
        SwissChessLstResult swissChessLstResult = new SwissChessLstResult();
        swissChessLstResult.setTeams(swTeams);
        swissChessLstResult.setPlayers(swPlayers);
        when(swissChessLstImportService.importSwissChessLst(inputStream)).thenReturn(swissChessLstResult);

        List<Player> returnedPlayers = dvmManagementService.importSwissChessLstFile(inputStream);

        assertThat(returnedPlayers).hasSize(correctPlayers.size());

        for (int i = 0; i <= 15; i++) {
            Player returnedPlayer = returnedPlayers.get(i);
            Player correctPlayer = correctPlayers.get(i);
            assertThat(returnedPlayer.getName()).isEqualTo(correctPlayer.getName());
            assertThat(returnedPlayer.getTeam()).isEqualTo(correctPlayer.getTeam());
        }

    }

    @Test
    void importSwissChessPgnFile() throws TeamNotFoundException, PlayerNotFoundException, IOException {

        List<SwissChessGame> swGames = Lists.newArrayList();
        List<Game> correctGames = Lists.newArrayList();
        for (int i = 0; i <= 5; i++) {
            SwissChessGame swGame = new SwissChessGame();
            swGame.setResult(PgnResult.DRAW);
            swGame.setPlayerNameWhite(i + " - White");
            swGame.setPlayerNameBlack(i + " - Black");
            swGame.setTeamNameWhite(i + " - Team White");
            swGame.setTeamNameBlack(i + " - Team Black");
            swGames.add(swGame);

            Game game = new Game();
            game.setResult(swGame.getResult().getResultString());
            Player playerWhite = mock(Player.class);
            when(playerWhite.getName()).thenReturn(swGame.getPlayerNameWhite());
            game.setPlayerWhite(playerWhite);
            Player playerBlack = mock(Player.class);
            when(playerBlack.getName()).thenReturn(swGame.getPlayerNameBlack());
            game.setPlayerBlack(playerBlack);
            correctGames.add(game);
            Team teamWhite = mock(Team.class);
            when(teamService.findByName(i + " - Team White")).thenReturn(teamWhite);
            Team teamBlack = mock(Team.class);
            when(teamService.findByName(i + " - Team Black")).thenReturn(teamBlack);
            TeamMatch teamMatch = mock(TeamMatch.class);
            when(teamMatchService.findOrCreateTeamMatch(teamWhite, teamBlack)).thenReturn(teamMatch);
            when(gameService.createGameForSwissChessGame(swGame, teamMatch)).thenReturn(game);
        }

        SwissChessPgnResult pgnResult = mock(SwissChessPgnResult.class);
        when(pgnResult.getGames()).thenReturn(swGames);

        InputStream inputStream = mock(InputStream.class);
        when(swissChessPgnImportService.importSwissChessPgn(inputStream)).thenReturn(pgnResult);
        List<Game> returnedGames = dvmManagementService.importSwissChessPgnFile(inputStream);

        for (int i = 0; i <= 5; i++) {
            Game returnedGame = returnedGames.get(i);
            SwissChessGame correctGame = swGames.get(i);
            assertThat(returnedGame.getResult()).isEqualTo(correctGame.getResult().getResultString());
            assertThat(returnedGame.getPlayerWhite().getName()).isEqualTo(correctGame.getPlayerNameWhite());
            assertThat(returnedGame.getPlayerBlack().getName()).isEqualTo(correctGame.getPlayerNameBlack());
        }

    }

}