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
import dsj.dvmManager.team.Team;
import dsj.dvmManager.team.TeamService;
import dsj.dvmManager.teamMatch.TeamMatch;
import dsj.dvmManager.teamMatch.TeamMatchService;

@Component
public class Initialization implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(Initialization.class);

    @Autowired
    private Environment environment;
    
    @Autowired
    private TeamService teamService;
    
    @Autowired
    private TeamMatchService teamMatchService;

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
        	List<Team> teams = initializeTeams();
        	List<TeamMatch> teamMatches = initializeTeamMatches(teams);
        	List<Player> players = initializePlayers(teams);
        	initializeGames(players, teamMatches);
        }
    }
    
    private List<Team> initializeTeams() {
    	List<Team> teams = Lists.newArrayList();
    	teams.add(teamService.createNewTeam("Team1"));
    	teams.add(teamService.createNewTeam("Team2"));
    	return teams; 
    }
    
    private List<TeamMatch> initializeTeamMatches(List<Team> teams) {
    	List<TeamMatch> teamMatches = Lists.newArrayList();
    	teamMatches.add(teamMatchService.createNewTeamMatch(teams.get(0), teams.get(1)));
    	return teamMatches;
    }

	private List<Player> initializePlayers(List<Team> teams) {
    	List<Player> players = Lists.newArrayList();
    	players.add(playerService.createNewPlayer("Kristin", "Wodzinski", "XGllByWFymyka82M", teams.get(0)));
    	players.add(playerService.createNewPlayer("Lennart", "Quante", "kagj7nENb3AluEue", teams.get(1)));
    	players.add(playerService.createNewPlayer("Harald", "Koppen", "", teams.get(0)));
    	players.add(playerService.createNewPlayer("Tobias", "Göttel", "", teams.get(1)));
		return players;
	}
	
	 private void initializeGames(List<Player> players, List<TeamMatch> teamMatches) {
		 
		List<Game> games = Lists.newArrayList();

		int counter = 1; 
		int matchCounter = 0; // Starts with 0 because teamMatches.get(0) is first element. 
		Player lastPlayer = null; 
	 	for (Player player : players) {
	 		int counterMod2 = counter % 2;
	 		
	 		if (counterMod2 == 1) {
	 			lastPlayer = player; 
	 		}
	 		else { // counterMod2 == 0, means second player
	 			Player secondPlayer = player; 
	 			Game game = gameService.createGame(lastPlayer, secondPlayer, teamMatches.get(matchCounter));
	 			game = gameService.save(game);
	 			games.add(game);
	 		}
	 
	 		counter++;
	 	}
		
	}

	
}
