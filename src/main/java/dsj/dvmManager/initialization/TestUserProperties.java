package dsj.dvmManager.initialization;

import com.google.common.collect.Lists;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.team.Team;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:testUser.properties")
public class TestUserProperties {

    @Value("#{'${teams}'.split(',')}")
    List<String> teamsFromProp;

    @Value("#{'${user.firstNames}'.split(',')}")
    List<String> firstNamesFromProp;

    @Value("#{'${user.lastNames}'.split(',')}")
    List<String> lastNamesFromProp;

    @Value("#{'${user.accessTokens}'.split(',')}")
    List<String> accessTokensFromProp;

    @Value("#{'${user.teamNames}'.split(',')}")
    List<String> teamNamesFromProp;

    public List<Team> getTeams() {
        List<Team> teams = Lists.newArrayList();
        for (String teamString : teamsFromProp) {
            Team team = new Team();
            team.setName(teamString);
            teams.add(team);
        }
        return teams;
    }

    public List<Player> getPlayers(List<Team> teams) throws InvalidInitializationException {
        List<Player> players = Lists.newArrayList();
        for (TestUser user : getUsersFromProperty()) {
            Player player = new Player();
            player.setFirstName(user.getFirstName());
            player.setLastName(user.getLastName());
            player.setAccessToken(user.getAccessToken());
            player.setTeam(teams.stream()
                    .filter(team -> team.getName().equals(user.getTeamName()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidInitializationException(
                            "Team " + user.getTeamName() + " (user: " + user.getLastName() + " ) does not exist"))
            );
            players.add(player);
        }
        return players;
    }

    private List<TestUser> getUsersFromProperty() {
        List<TestUser> testUsers = Lists.newArrayList();

        for (String firstName : firstNamesFromProp) {
            TestUser user = new TestUser();
            user.setFirstName(firstName);
            testUsers.add(user);
        }

        int lastNameCounter = 0;
        for (String lastName : lastNamesFromProp) {
            testUsers.get(lastNameCounter).setLastName(lastName);
            lastNameCounter++;
        }

        int accessTokenCounter = 0;
        for (String accessToken : accessTokensFromProp) {
            testUsers.get(accessTokenCounter).setAccessToken(accessToken);
            accessTokenCounter++;
        }

        int teamNameCounter = 0;
        for (String teamName : teamNamesFromProp) {
            testUsers.get(teamNameCounter).setTeamName(teamName);
            teamNameCounter++;
        }

        return testUsers;
    }

}
