package dsj.dvmManager.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
	
	private PlayerRepository playerRepository;
	
	@Autowired
	public PlayerService(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}
	
	public Player createNewPlayer(String firstName, String lastName, String accessToken) {
		Player player = new Player();
		player.setFirstName(firstName);
		player.setLastName(lastName);
		player.setAccessToken(accessToken);
		return player;
	}
	
	public Player save(Player player) {
		return this.playerRepository.save(player);
	}

}
