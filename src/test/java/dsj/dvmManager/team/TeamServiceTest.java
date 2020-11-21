package dsj.dvmManager.team;

import dsj.dvmManager.swissChessImport.SwissChessTeam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class TeamServiceTest {

    @InjectMocks
    @Spy
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void createTeamForSwissChessTeam() {
        SwissChessTeam swissChessTeam = new SwissChessTeam();
        swissChessTeam.setTeamName("Team-Name");

        Team correctTeam = new Team();
        correctTeam.setName("Team-Name");
        doReturn(correctTeam).when(teamService).createNewTeam("Team-Name");

        Team team = teamService.createNewTeamForSwissChessTeam(swissChessTeam);
        assertThat(team).isEqualTo(correctTeam);
    }

    @Test
    void findByName() throws TeamNotFoundException {

        String name = "Team-Name";

        Team correctTeam = mock(Team.class);
        when(teamRepository.findByName(name)).thenReturn(Optional.of(correctTeam));

        Team team = teamService.findByName(name);
        assertThat(team).isEqualTo(correctTeam);

    }

    @Test
    void findByName_throwsExceptionForInvalidTeam() {

        String name = "Not-existing-team-name";

        when(teamRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(TeamNotFoundException.class, () -> teamService.findByName(name));

    }
}