package dsj.dvmManager.team;

import dsj.dvmManager.swissChessImport.SwissChessTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findByName(String name) throws TeamNotFoundException {
        Optional<Team> teamOptional = teamRepository.findByName(name);
        if (teamOptional.isPresent()) {
            return teamOptional.get();
        } else {
            throw new TeamNotFoundException();
        }
    }

    public Team createNewTeam(String name) {
        Team team = new Team();
        team.setName(name);
        return save(team);
    }

    public Team save(Team team) {
        return this.teamRepository.save(team);
    }

    public List<Team> saveAll(List<Team> teams) {
        return this.teamRepository.saveAll(teams);
    }

    public Team createNewTeamForSwissChessTeam(SwissChessTeam swissChessTeam) {
        return createNewTeam(swissChessTeam.getTeamName());
    }

    public void deleteAll() {
        teamRepository.deleteAll();
        teamRepository.flush();
    }


}
