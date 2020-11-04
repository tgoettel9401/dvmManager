package dsj.dvmManager.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dsj.dvmManager.player.Player;

@Service
public class GameService {
	
	private final GameRepository gameRepository; 
	
	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public Game createGame(Player playerWhite, Player playerBlack) {
		Game game = new Game(); 
		game.setPlayerWhite(playerWhite);
		game.setPlayerBlack(playerBlack);
		return game; 
	}
	
	public Game save(Game game) {
		return this.gameRepository.save(game);
	}
	
	public List<Game> getAllGames() {
		return this.gameRepository.findAll();
	}

}
