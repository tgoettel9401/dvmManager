package dsj.dvmManager.game;

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
	
	private String liChessGameId; 
	
	public String getPairingString() {
		return playerWhite.getName() + " - " + playerBlack.getName();
	}
	
}
