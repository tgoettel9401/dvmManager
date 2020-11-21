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

    public TeamMatch createNewTeamMatch(Team teamWhite, Team teamBlack) {
        TeamMatch teamMatch = new TeamMatch();
        teamMatch.setTeamWhite(teamWhite);
        teamMatch.setTeamBlack(teamBlack);
        return save(teamMatch);
    }

    public TeamMatch save(TeamMatch teamMatch) {
        return teamMatchRepository.save(teamMatch);
    }

    public TeamMatch findOrCreateTeamMatch(Team teamWhite, Team teamBlack) {
        Optional<TeamMatch> teamMatchOptional = teamMatchRepository.findByTeamWhiteAndTeamBlack(teamWhite, teamBlack);
        Optional<TeamMatch> teamMatchOptionalReversed = teamMatchRepository.findByTeamWhiteAndTeamBlack(teamBlack, teamWhite);
        return teamMatchOptional.orElseGet(() -> teamMatchOptionalReversed.orElseGet(() -> createNewTeamMatch(teamWhite, teamBlack)));
    }

    public TeamMatchDto createTeamMatchDto(TeamMatch teamMatch) {
        TeamMatchDto dto = new TeamMatchDto();
        dto.setTeamWhite(teamMatch.getTeamWhite().getName());
        dto.setTeamBlack(teamMatch.getTeamBlack().getName());
        for (Game game : teamMatch.getGames())
            dto.getGames().add(gameService.createGameDto(game));
        return dto;
    }

    public List<TeamMatchDto> findAllDtos() {
        List<TeamMatchDto> dtos = Lists.newArrayList();
        findAll().forEach(teamMatch -> dtos.add(createTeamMatchDto(teamMatch)));
        return dtos;
    }

}
