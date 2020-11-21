package dsj.dvmManager.player;

import dsj.dvmManager.swissChessImport.SwissChessPlayer;
import dsj.dvmManager.team.Team;
import dsj.dvmManager.team.TeamNotFoundException;
import dsj.dvmManager.team.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;
    private TeamService teamService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.teamService = teamService;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    public Player findByNameAndTeamName(String playerName, String teamName) throws TeamNotFoundException, PlayerNotFoundException {
        String firstName = getFirstNameFromName(playerName);
        String lastName = getLastNameFromName(playerName);
        Team team = teamService.findByName(teamName);
        Optional<Player> playerOptional = playerRepository.findByFirstNameAndLastNameAndTeam(firstName, lastName, team);
        if (playerOptional.isPresent()) {
            return playerOptional.get();
        } else {
            throw new PlayerNotFoundException();
        }
    }

    public Player createNewPlayer(String firstName, String lastName, String accessToken, Team team) {
        Player player = new Player();
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setAccessToken(accessToken);
        player.setTeam(team);
        return save(player);
    }

    public Player save(Player player) {
        return this.playerRepository.save(player);
    }

    public List<Player> saveAll(List<Player> players) {
        return this.playerRepository.saveAll(players);
    }

    public Player createPlayerForSwissChessPlayer(SwissChessPlayer swissChessPlayer, Team team) {
        Player player = new Player();
        player.setFirstName(getFirstNameFromName(swissChessPlayer.getPlayerName()));
        player.setLastName(getLastNameFromName(swissChessPlayer.getPlayerName()));
        player.setTeam(team);
        player.setAccessToken(swissChessPlayer.getAccessToken());
        return save(player);
    }

    // Returns a firstName if the name consists of two parts, divided by a comma.
    // Hence format of name is always [{LastName}, {FirstName}]
    private String getFirstNameFromName(String name) {

        if (name.contains(",")) {
            // Second part is always the firstName, usually starting with a space.
            // Therefore remove the space if it starts with it.
            List<String> nameParts = Arrays.asList(name.split(","));
            String firstName = nameParts.get(1);
            if (firstName.startsWith(" ")) {
                firstName = firstName.substring(1);
            }
            return firstName;
        } else {
            // If the name does not consist of two parts, the name is considered as LastName and therefore the FirstName
            // is empty.
            return "";
        }
    }

    private String getLastNameFromName(String name) {

        if (name.contains(",")) {
            // First part is always the lastName. Therefore use get(0).
            List<String> nameParts = Arrays.asList(name.split(","));
            return nameParts.get(0);
        } else {
            // If the name does not consist of two parts, the name is considered as LastName.
            return name;
        }

    }

}
