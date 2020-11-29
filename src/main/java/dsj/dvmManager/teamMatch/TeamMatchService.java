package dsj.dvmManager.teamMatch;

import com.google.common.collect.Lists;
import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamMatchService {

    private final GameService gameService;

    private final TeamMatchRepository teamMatchRepository;

    @Autowired
    public TeamMatchService(GameService gameService, TeamMatchRepository teamMatchRepository) {
        this.gameService = gameService;
        this.teamMatchRepository = teamMatchRepository;
    }

    public List<TeamMatch> findAll() {
        return teamMatchRepository.findAll();
    }

    public TeamMatch createNewTeamMatch(Team teamWhite, Team teamBlack, Integer boardNumber) {
        TeamMatch teamMatch = new TeamMatch();
        if (boardNumber % 2 == 0) { // BoardNumber is even, so TeamHome is playing with White.
            teamMatch.setTeamHome(teamWhite);
            teamMatch.setTeamAway(teamBlack);
        } else { // BoardNumber is odd, so TeamHome is playing with Black.
            teamMatch.setTeamHome(teamBlack);
            teamMatch.setTeamAway(teamWhite);
        }
        return save(teamMatch);
    }

    public TeamMatch save(TeamMatch teamMatch) {
        return teamMatchRepository.save(teamMatch);
    }

    public TeamMatch findOrCreateTeamMatch(Team teamWhite, Team teamBlack, Integer boardNumber) {
        Optional<TeamMatch> teamMatchOptional = teamMatchRepository.findByTeamHomeAndTeamAway(teamWhite, teamBlack);
        Optional<TeamMatch> teamMatchOptionalReversed = teamMatchRepository.findByTeamHomeAndTeamAway(teamBlack, teamWhite);
        return teamMatchOptional.orElseGet(
                () -> teamMatchOptionalReversed.orElseGet(
                        () -> createNewTeamMatch(teamWhite, teamBlack, boardNumber))
        );
    }

    public TeamMatchDto createTeamMatchDto(TeamMatch teamMatch) {
        TeamMatchDto dto = new TeamMatchDto();
        dto.setTeamHome(teamMatch.getTeamHome().getName());
        dto.setTeamAway(teamMatch.getTeamAway().getName());
        for (Game game : teamMatch.getGames())
            dto.getGames().add(gameService.createGameDto(teamMatch.getTeamHome(), game));
        return dto;
    }

    public List<TeamMatchDto> findAllDtos() {
        List<TeamMatchDto> dtos = Lists.newArrayList();
        findAll().forEach(teamMatch -> dtos.add(createTeamMatchDto(teamMatch)));
        return dtos;
    }

    public void deleteAll() {
        teamMatchRepository.deleteAll();
        teamMatchRepository.flush();
    }

    public TeamMatch findById(Long teamMatchId) throws TeamMatchNotFoundException {
        return teamMatchRepository.findById(teamMatchId).orElseThrow(TeamMatchNotFoundException::new);
    }
}
