package dsj.dvmManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.liChessAdapter.LiChessChallenge;
import dsj.dvmManager.liChessAdapter.LiChessService;
import dsj.dvmManager.teamMatch.TeamMatchDto;
import dsj.dvmManager.teamMatch.TeamMatchService;

@Service
public class DvmManagementService {
	
	@Autowired
	private GameService gameService; 
	
	@Autowired TeamMatchService teamMatchService;
	
	@Autowired
	private LiChessService liChessService;
	
	public List<LiChessChallenge> createChallenges() {
		List<Game> games = this.gameService.getAllGames();
		List<LiChessChallenge> challenges = Lists.newArrayList();
		for (Game game: games) {
			challenges.add(liChessService.createChallenge(game));
		}
		return challenges; 
	}
	
	public List<TeamMatchDto> findAllTeamMatchDtos() {
		return teamMatchService.findAllDtos();
	}

}
