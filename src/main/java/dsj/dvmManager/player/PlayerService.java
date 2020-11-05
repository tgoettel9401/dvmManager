package dsj.dvmManager.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dsj.dvmManager.team.Team;

@Service
public class PlayerService {
	
	private PlayerRepository playerRepository;
	
	@Autowired
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
	public Player createNewPlayer(String firstName, String lastName, String accessToken, Team team) {
		Player player = new Player();
		player.setFirstName(firstName);
		player.setLastName(lastName);
		player.setAccessToken(accessToken);
		player.setTeam(team);
		return save(player);
	}
	
	public Player save(Player player) {
		return this.playerRepository.save(player);
	}

}
