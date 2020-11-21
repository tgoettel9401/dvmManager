package dsj.dvmManager.game;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import dsj.dvmManager.player.Player;
import dsj.dvmManager.teamMatch.TeamMatch;
import lombok.Data;

@Data
@Entity
public class Game {
	
	@Id @GeneratedValue
	private Long id; 

	@ManyToOne
	private Player playerWhite; 
	
	@ManyToOne
	private Player playerBlack;
	
	@ManyToOne
	private TeamMatch teamMatch;
	
	// LiChessGameInformation
	private String liChessGameId;
	private String liChessGameStatus;
	private String liChessGameMoves; 
	private ZonedDateTime liChessGameCreatedAt;
	private ZonedDateTime liChessGameLastMoveAt;
	private GameResult result;
	
	public String getPairingString() {
		return playerWhite.getName() + " - " + playerBlack.getName();
	}
	
}
