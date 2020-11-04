package dsj.dvmManager.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerService;

@Component
public class Initialization implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(Initialization.class);

    @Autowired
    private Environment environment;

    @Autowired
    private PlayerService playerService;
    
    @Autowired
    private GameService gameService;

    @Override
    @Transactional
    public void afterPropertiesSet() {
    	
    	logger.info("Initializing Database with sample values");
    	
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

        if (activeProfiles.contains("db-init")) {
        	List<Player> players = initializePlayers();
        	initializeGames(players);
        }
    }

	private List<Player> initializePlayers() {
    	List<Player> players = Lists.newArrayList();
    	
    	Player player1 = playerService.createNewPlayer("Player1", "Player2", "XGllByWFymyka82M");
    	player1 = playerService.save(player1);
    	players.add(player1);
    	
    	Player player2 = playerService.createNewPlayer("Player2", "Player3", "kagj7nENb3AluEue");
    	player2 = playerService.save(player2);
    	players.add(player2);
    	
		return players;
	}
	
	 private void initializeGames(List<Player> players) {
		 
		List<Game> games = Lists.newArrayList();

		int counter = 1; 
		Player lastPlayer = null; 
	 	for (Player player : players) {
	 		int counterMod2 = counter % 2;
	 		
	 		if (counterMod2 == 1) {
	 			lastPlayer = player; 
	 		}
	 		else { // counterMod2 == 0, means second player
	 			Player secondPlayer = player; 
	 			Game game = gameService.createGame(lastPlayer, secondPlayer);
	 			game = gameService.save(game);
	 			games.add(game);
	 		}
	 
	 		counter++;
	 	}
		
	}

	
}
