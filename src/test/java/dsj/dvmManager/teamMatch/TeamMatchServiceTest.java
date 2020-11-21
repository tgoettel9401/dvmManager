package dsj.dvmManager.teamMatch;

import dsj.dvmManager.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class TeamMatchServiceTest {

    @InjectMocks
    private TeamMatchService teamMatchService;

    @Mock
    private TeamMatchRepository teamMatchRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void findOrCreateTeamMatch_returnsTeamMatchWhenFoundAndNewMatchWhenNotFound() {

        Team teamWhite = mock(Team.class);
        Team teamBlack = mock(Team.class);

        TeamMatch teamMatch = new TeamMatch();
        teamMatch.setTeamWhite(teamWhite);
        teamMatch.setTeamBlack(teamBlack);
        when(teamMatchRepository.findByTeamWhiteAndTeamBlack(teamWhite, teamBlack)).thenReturn(Optional.of(teamMatch));

        TeamMatch returnedTeamMatch = teamMatchService.findOrCreateTeamMatch(teamWhite, teamBlack);
        assertThat(returnedTeamMatch).isEqualTo(teamMatch);

        Team newTeamWhite = mock(Team.class);
        Team newTeamBlack = mock(Team.class);
        TeamMatch newTeamMatch = mock(TeamMatch.class);
        when(teamMatchRepository.findByTeamWhiteAndTeamBlack(newTeamWhite, newTeamBlack)).thenReturn(Optional.empty());
        when(teamMatchRepository.save(any())).thenReturn(newTeamMatch);

        TeamMatch newReturnedTeamMatch = teamMatchService.findOrCreateTeamMatch(newTeamWhite, newTeamBlack);
        verify(teamMatchRepository, times(1)).save(any());
        assertThat(newReturnedTeamMatch).isEqualTo(newTeamMatch);

    }

}