package dsj.dvmManager.game;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dsj.dvmManager.player.Player;
import dsj.dvmManager.teamMatch.TeamMatch;

@Service
public class GameService {
	
	private final GameRepository gameRepository; 
	
	@Autowired
	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public List<Game> findAll() {
		return gameRepository.findAll();
	}
	
	public Game createGame(Player playerWhite, Player playerBlack, TeamMatch teamMatch) {
		Game game = new Game(); 
		game.setPlayerWhite(playerWhite);
		game.setPlayerBlack(playerBlack);
		game.setTeamMatch(teamMatch);
		return game; 
	}
	
	public Game save(Game game) {
		return this.gameRepository.save(game);
	}
	
	public List<Game> getAllGames() {
		return this.gameRepository.findAll();
	}
	
	public GameDto createGameDto(Game game) {
		GameDto dto = new GameDto();
		dto.setPlayerWhite(game.getPlayerWhite().getName());
		dto.setPlayerBlack(game.getPlayerBlack().getName());
		dto.setLiChessGameId(game.getLiChessGameId());
		dto.setLiChessGameStatus(game.getLiChessGameStatus());
		dto.setLiChessGameMoves(game.getLiChessGameMoves());
		dto.setLiChessGameCreatedAt(game.getLiChessGameCreatedAt());
		dto.setLiChessGameLastMoveAt(game.getLiChessGameLastMoveAt());
		return dto;
	}
	
}
