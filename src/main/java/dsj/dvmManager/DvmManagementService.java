package dsj.dvmManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.liChessAdapter.Challenge;
import dsj.dvmManager.liChessAdapter.LiChessService;

@Service
public class DvmManagementService {
	
	@Autowired
	private GameService gameService; 
	
	@Autowired
	private LiChessService liChessService;
	
	public List<Challenge> createChallenges() {
		List<Game> games = this.gameService.getAllGames();
		List<Challenge> challenges = Lists.newArrayList();
		for (Game game: games) {
			challenges.add(liChessService.createChallenge(game.getPlayerWhite(), game.getPlayerBlack()));
		}
		return challenges; 
	}

}
