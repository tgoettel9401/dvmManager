package dsj.dvmManager.game;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class GameDto {
	
	private String playerHome;
	private String playerAway;
	private GameResult result;

	@JsonIgnore private GameColor playerHomeColor;
	@JsonIgnore private GameColor playerAwayColor;
	
	private String liChessGameId; 
	private String liChessGameStatus;
	private String liChessGameMoves; 
	private ZonedDateTime liChessGameCreatedAt;
	private ZonedDateTime liChessGameLastMoveAt;

	@JsonIgnore
	private Game game;

	public String getResult() {
		return result.getResultString();
	}

	public Double getPointsPlayerHome() {
		return result.getPointsWhite();
	}

	public Double getPointsPlayerAway() {
		return result.getPointsBlack();
	}

}
