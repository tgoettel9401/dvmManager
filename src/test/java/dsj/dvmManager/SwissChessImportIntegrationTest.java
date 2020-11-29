package dsj.dvmManager;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameResult;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerService;
import dsj.dvmManager.team.Team;
import dsj.dvmManager.team.TeamService;
import dsj.dvmManager.teamMatch.TeamMatch;
import dsj.dvmManager.teamMatch.TeamMatchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active:none")
public class SwissChessImportIntegrationTest {

    @Autowired
    private DvmManagementService dvmManagementService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamMatchService teamMatchService;
    @Autowired
    private GameService gameService;

    @Test
    void testImportSwissChessFiles() throws IOException {

        // xxxxxxxxxxxxxxxx LST-IMPORT xxxxxxxxxxxxxxxxxxxxxxxxx

        InputStream lstInputStream = getLstInputStream();

        dvmManagementService.importSwissChessLstFile(lstInputStream);

        // aaaaaaaaaaaaaaa PLAYERS aaaaaaaaaaaaaaaaaaaa

        List<Player> importedPlayers = playerService.findAll();
        assertThat(importedPlayers).hasSize(4);

        // Player 1
        assertThat(importedPlayers.stream().filter(player -> player.getLastName().equals("Player1LastName")).collect(Collectors.toList())).hasSize(1);
        assertThat(importedPlayers.stream().anyMatch(player -> player.getLastName().equals("Player1LastName"))).isTrue();
        importedPlayers.stream().filter(player -> player.getLastName().equals("Player1LastName")).findFirst().ifPresent((player) -> assertThat(player.getTeam().getName()).isEqualTo("Team1"));

        // Player 2
        assertThat(importedPlayers.stream().filter(player -> player.getLastName().equals("Player2LastName")).collect(Collectors.toList())).hasSize(1);
        assertThat(importedPlayers.stream().anyMatch(player -> player.getLastName().equals("Player2LastName"))).isTrue();
        importedPlayers.stream().filter(player -> player.getLastName().equals("Player2LastName")).findFirst().ifPresent((player) -> assertThat(player.getTeam().getName()).isEqualTo("Team2"));

        // Player 3
        assertThat(importedPlayers.stream().filter(player -> player.getLastName().equals("Player3LastName")).collect(Collectors.toList())).hasSize(1);
        assertThat(importedPlayers.stream().anyMatch(player -> player.getLastName().equals("Player3LastName"))).isTrue();
        importedPlayers.stream().filter(player -> player.getLastName().equals("Player3LastName")).findFirst().ifPresent((player) -> assertThat(player.getTeam().getName()).isEqualTo("Team1"));

        // Player 4
        assertThat(importedPlayers.stream().filter(player -> player.getLastName().equals("Player4LastName")).collect(Collectors.toList())).hasSize(1);
        assertThat(importedPlayers.stream().anyMatch(player -> player.getLastName().equals("Player4LastName"))).isTrue();
        importedPlayers.stream().filter(player -> player.getLastName().equals("Player4LastName")).findFirst().ifPresent((player) -> assertThat(player.getTeam().getName()).isEqualTo("Team2"));

        // aaaaaaaaaaaaaaa TEAMS aaaaaaaaaaaaaaaaaaaa
        List<Team> importedTeams = teamService.findAll();
        assertThat(importedTeams).hasSize(2);

        // Team 1
        assertThat(importedTeams.stream().filter(team -> team.getName().equals("Team1")).collect(Collectors.toList())).hasSize(1);
        assertThat(importedTeams.stream().anyMatch(team -> team.getName().equals("Team1"))).isTrue();
        importedTeams.stream().filter(team -> team.getName().equals("Team1")).findFirst().ifPresent(team -> assertThat(team.getName()).isEqualTo("Team1"));

        // Team 2
        assertThat(importedTeams.stream().filter(team -> team.getName().equals("Team2")).collect(Collectors.toList())).hasSize(1);
        assertThat(importedTeams.stream().anyMatch(team -> team.getName().equals("Team2"))).isTrue();
        importedTeams.stream().filter(team -> team.getName().equals("Team2")).findFirst().ifPresent(team -> assertThat(team.getName()).isEqualTo("Team2"));

        // xxxxxxxxxxxxxxxx PGN-IMPORT xxxxxxxxxxxxxxxxxxxxxxxxx

        InputStream pgnInputStream = getPgnInputStream();

        dvmManagementService.importSwissChessPgnFile(pgnInputStream, true);

        // aaaaaaaaaaaaaaa TEAM_MATCHES aaaaaaaaaaaaaaaaaaaa

        List<TeamMatch> teamMatches = teamMatchService.findAll();

        // TeamMatch1
        assertThat(teamMatches.stream().filter(teamMatch -> teamMatch.getTeamAway().getName().equals("Team1")).collect(Collectors.toList())).hasSize(1);
        assertThat(teamMatches.stream().anyMatch(teamMatch -> teamMatch.getTeamAway().getName().equals("Team1"))).isTrue();
        teamMatches.stream().filter(teamMatch -> teamMatch.getTeamAway().getName().equals("Team1")).findFirst().ifPresent(teamMatch -> assertThat(teamMatch.getTeamHome().getName()).isEqualTo("Team2"));
        teamMatches.stream().filter(teamMatch -> teamMatch.getTeamHome().getName().equals("Team2")).findFirst().ifPresent(teamMatch -> assertThat(teamMatch.getTeamAway().getName()).isEqualTo("Team1"));

        // aaaaaaaaaaaaaaa GAMES aaaaaaaaaaaaaaaaaaaa

        List<Game> games = gameService.findAll();

        // Game1
        assertThat(games.stream().filter(game -> game.getPlayerWhite().getLastName().equals("Player1LastName")).collect(Collectors.toList())).hasSize(1);
        assertThat(games.stream().anyMatch(game -> game.getPlayerWhite().getLastName().equals("Player1LastName"))).isTrue();
        games.stream().filter(game -> game.getPlayerWhite().getLastName().equals("Player1LastName")).findFirst().ifPresent(game -> {
            assertThat(game.getPlayerWhite().getLastName()).isEqualTo("Player1LastName");
            assertThat(game.getPlayerBlack().getLastName()).isEqualTo("Player4LastName");
            assertThat(game.getTeamMatch().getTeamAway().getName()).isEqualTo("Team1");
            assertThat(game.getTeamMatch().getTeamHome().getName()).isEqualTo("Team2");
            assertThat(game.getResult()).isEqualTo(GameResult.UNKNOWN);
        });

        // Game2
        assertThat(games.stream().filter(game -> game.getPlayerWhite().getLastName().equals("Player2LastName")).collect(Collectors.toList())).hasSize(1);
        assertThat(games.stream().anyMatch(game -> game.getPlayerWhite().getLastName().equals("Player2LastName"))).isTrue();
        games.stream().filter(game -> game.getPlayerWhite().getLastName().equals("Player2LastName")).findFirst().ifPresent(game -> {
            assertThat(game.getPlayerWhite().getLastName()).isEqualTo("Player2LastName");
            assertThat(game.getPlayerBlack().getLastName()).isEqualTo("Player3LastName");
            assertThat(game.getTeamMatch().getTeamAway().getName()).isEqualTo("Team1");
            assertThat(game.getTeamMatch().getTeamHome().getName()).isEqualTo("Team2");
            assertThat(game.getResult()).isEqualTo(GameResult.UNKNOWN);
        });

    }

    private InputStream getLstInputStream() throws IOException {
        return new ClassPathResource("integrationTest.lst").getInputStream();
    }

    private InputStream getPgnInputStream() throws IOException {
        return new ClassPathResource("integrationTest.pgn").getInputStream();
    }

}
