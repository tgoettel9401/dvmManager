package dsj.dvmManager;

import java.util.List;

import dsj.dvmManager.pgnParser.Pgn;
import dsj.dvmManager.pgnParser.PgnParserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.liChessAdapter.LiChessChallenge;
import dsj.dvmManager.liChessAdapter.LiChessGame;
import dsj.dvmManager.liChessAdapter.LiChessService;
import dsj.dvmManager.teamMatch.TeamMatchDto;
import dsj.dvmManager.teamMatch.TeamMatchService;

@Service
public class DvmManagementService {

	private final Logger logger = LoggerFactory.getLogger(DvmManagementService.class);
	
	@Autowired
	private GameService gameService; 
	
	@Autowired
	TeamMatchService teamMatchService;
	
	@Autowired
	private LiChessService liChessService;

	@Autowired
	private PgnParserService pgnParserService;
	
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
	
	public void updateGames() {
		List<Game> games = gameService.findAll();
		int updatedGameCounter = 0;
		for (Game game : games) {
			if (game.getLiChessGameId() != null) {
				LiChessGame liChessGame = liChessService.getLiChessGame(game);
				game.setLiChessGameStatus(liChessGame.getStatus());
				game.setLiChessGameMoves(liChessGame.getMoves());
				game.setLiChessGameCreatedAt(liChessGame.getCreatedAt());
				game.setLiChessGameLastMoveAt(liChessGame.getLastMoveAt());

				Pgn pgn = pgnParserService.createPgnFromString(liChessGame.getPgn());
				game.setResult(pgn.getResult().getResultString());

				gameService.save(game);
				updatedGameCounter++;
			}
		}
		logger.info("Updated " + updatedGameCounter + " games");
	}

}
