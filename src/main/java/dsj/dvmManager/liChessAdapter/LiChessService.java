package dsj.dvmManager.liChessAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dsj.dvmManager.player.Player;

@Service
public class LiChessService {
	
	private final LiChessAdapter liChessAdapter; 
	
	@Autowired
	public LiChessService(LiChessAdapter liChessAdapter) {
		this.liChessAdapter = liChessAdapter; 
	}
	
	public Challenge createChallenge(Player player1, Player player2) {
		Challenge challenge = liChessAdapter.createChallenge(player1, player2);
		return challenge; 
	}

}
