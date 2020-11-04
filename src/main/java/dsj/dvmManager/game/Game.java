package dsj.dvmManager.game;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import dsj.dvmManager.player.Player;
import lombok.Data;

@Data
@Entity
public class Game {
	
	@Id @GeneratedValue
	private Long id; 

	@ManyToOne (fetch = FetchType.LAZY)
	private Player playerWhite; 
	
	@ManyToOne (fetch = FetchType.LAZY)
	private Player playerBlack;
	
	private String challengeId; 
	
}
