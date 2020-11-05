package dsj.dvmManager.game;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class GameDto {
	
	private String playerWhite;
	private String playerBlack;
	
	private String liChessGameId; 
	private String liChessGameStatus;
	private String liChessGameMoves; 
	private ZonedDateTime liChessGameCreatedAt;
	private ZonedDateTime liChessGameLastMoveAt;

}
