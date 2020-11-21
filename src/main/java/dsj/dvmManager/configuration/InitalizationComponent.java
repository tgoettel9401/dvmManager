package dsj.dvmManager.configuration;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameResult;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerService;
import dsj.dvmManager.team.Team;
import dsj.dvmManager.team.TeamService;
import dsj.dvmManager.teamMatch.TeamMatch;
import dsj.dvmManager.teamMatch.TeamMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Profile("local")
@Component
public class InitalizationComponent implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(InitalizationComponent.class);

	private final boolean useTestProperties = false;
	private final Faker faker = new Faker();
	@Autowired
	TestUserProperties testUserProperties;
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
    public void afterPropertiesSet() throws InvalidInitializationException {

		logger.info("Initializing Database with sample values");

		List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());

		if (activeProfiles.contains("db-init")) {
			List<Team> teams = initializeTeams();
			List<TeamMatch> teamMatches = initializeTeamMatches(teams);
			initializePlayers(teams);
			initializeGames(teamMatches);
		}
	}

	private List<Team> initializeTeams() {
		if (useTestProperties)
			return teamService.saveAll(testUserProperties.getTeams());
		else
			return teamService.saveAll(fakeTeamsWithSize(10));
	}

	private List<Team> fakeTeamsWithSize(int size) {
		List<Team> teams = Lists.newArrayList();
		for (int i = 1; i <= size; i++) {
			Team team = new Team();
			team.setName(faker.team().name());
			teams.add(team);
		}
		return teams;
	}

	private List<TeamMatch> initializeTeamMatches(List<Team> teams) {
		List<TeamMatch> teamMatches = Lists.newArrayList();

		int counter = 1;
		Team firstTeam = null;
		for (Team team : teams) {
			int counterMod2 = counter % 2;

			if (counterMod2 == 1) {
				firstTeam = team;
			} else { // counterMod2 == 0, means second player
				TeamMatch match = teamMatchService.createNewTeamMatch(firstTeam, team);
				match = teamMatchService.save(match);
				teamMatches.add(match);
			}

			counter++;
		}

		return teamMatches;
	}

	private void initializePlayers(List<Team> teams) throws InvalidInitializationException {
		if (useTestProperties)
			playerService.saveAll(testUserProperties.getPlayers(teams));
		else
			playerService.saveAll(fakePlayersWithSize(teams, 5));
	}

	private List<Player> fakePlayersWithSize(List<Team> teams, int playerSize) {
		List<Player> players = Lists.newArrayList();
		for (Team team : teams) {
			for (int i = 1; i <= playerSize; i++) {
				Player player = new Player();
				player.setFirstName(faker.name().firstName());
				player.setLastName(faker.name().lastName());
				player.setAccessToken(faker.number().digits(18));
				player.setTeam(team);
				players.add(player);
			}
		}
		return players;
	}

	private void initializeGames(List<TeamMatch> teamMatches) {

		List<Player> players = playerService.findAll();

		for (TeamMatch teamMatch : teamMatches) {
			Team teamWhite = teamMatch.getTeamHome();
			Team teamBlack = teamMatch.getTeamAway();

			List<Player> playersTeamWhite = players.stream()
					.filter(player -> player.getTeam().getId().equals(teamWhite.getId()))
					.collect(Collectors.toList());
			List<Player> playersTeamBlack = players.stream()
					.filter(player -> player.getTeam().getId().equals(teamBlack.getId()))
					.collect(Collectors.toList());

			int playerSize = Math.min(playersTeamWhite.size(), playersTeamBlack.size());

			for (int i = 1; i <= playerSize; i++) {
				Player playerTeam1 = playersTeamWhite.get(i - 1);
				Player playerTeam2 = playersTeamBlack.get(i - 1);

				boolean team1HasWhite = Math.abs(i) % 2 == 1;

				Game game = new Game();
				if (team1HasWhite) {
					game.setPlayerWhite(playerTeam1);
					game.setPlayerBlack(playerTeam2);
				} else {
					game.setPlayerWhite(playerTeam2);
					game.setPlayerBlack(playerTeam1);
				}
				game.setResult(GameResult.BLACK_WINS);
				game.setTeamMatch(teamMatch);
				gameService.save(game);
			}
		}
	}
}
