package dsj.dvmManager.player;

import dsj.dvmManager.swissChessImport.SwissChessPlayer;
import dsj.dvmManager.team.Team;
import dsj.dvmManager.team.TeamNotFoundException;
import dsj.dvmManager.team.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void createPlayerForSwissChessPlayer() {
        SwissChessPlayer swissChessPlayer = mock(SwissChessPlayer.class);
        when(swissChessPlayer.getAccessToken()).thenReturn("Access-Token");
        when(swissChessPlayer.getPlayerName()).thenReturn("Last-Name, First-Name");
        when(swissChessPlayer.getTeamName()).thenReturn("Team-Name");

        Team teamMock = mock(Team.class);
        when(teamMock.getName()).thenReturn("Team-Name");
        Player correctPlayer = new Player();
        correctPlayer.setFirstName("First-Name");
        correctPlayer.setLastName("Last-Name");
        correctPlayer.setAccessToken("Access-Token");
        correctPlayer.setTeam(teamMock);

        when(playerRepository.save(correctPlayer)).thenReturn(correctPlayer);

        Player returnedPlayer = playerService.createPlayerForSwissChessPlayer(swissChessPlayer, teamMock);

        assertThat(returnedPlayer.getFirstName()).isEqualTo("First-Name");
        assertThat(returnedPlayer.getLastName()).isEqualTo("Last-Name");
        assertThat(returnedPlayer.getAccessToken()).isEqualTo("Access-Token");
        assertThat(returnedPlayer.getTeam()).isEqualTo(teamMock);
    }

    @Test
    void findByNameAndTeamName() throws TeamNotFoundException, PlayerNotFoundException {
        String playerName = "Player, Name";
        String teamName = "Team-Name";

        Team team = mock(Team.class);
        when(teamService.findByName(teamName)).thenReturn(team);
        Player correctPlayer = mock(Player.class);

        when(playerRepository.findByFirstNameAndLastNameAndTeam("Name", "Player", team)).thenReturn(Optional.of(correctPlayer));

        Player player = playerService.findByNameAndTeamName(playerName, teamName);
        assertThat(player).isEqualTo(correctPlayer);
    }
}