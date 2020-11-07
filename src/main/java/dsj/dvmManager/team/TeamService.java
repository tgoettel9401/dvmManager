package dsj.dvmManager.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
	
	private final TeamRepository teamRepository; 
	
	@Autowired
	public TeamService (TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
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
	
}
