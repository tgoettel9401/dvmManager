package dsj.dvmManager.swissChessImport;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
public class SwissChessLstImportService {

    public SwissChessLstResult importSwissChessLst(InputStream inputStream) {
        List<SwissChessLstEntry> lstEntries = importLstEntries(inputStream);
        List<SwissChessPlayer> players = getPlayersFromLstEntries(lstEntries);
        List<SwissChessTeam> teams = getTeamsFromLstEntries(lstEntries);
        SwissChessLstResult swissChessLstResult = new SwissChessLstResult();
        swissChessLstResult.setPlayers(players);
        swissChessLstResult.setTeams(teams);
        return swissChessLstResult;
    }

    List<SwissChessTeam> getTeamsFromLstEntries(List<SwissChessLstEntry> lstEntries) {
        List<SwissChessTeam> teams = Lists.newArrayList();

        lstEntries.forEach(lstEntry -> {
            Optional<SwissChessTeam> teamOptional = teams.stream().filter(team -> team.getTeamName().equals(lstEntry.getTeamName())).findFirst();
            if (teamOptional.isEmpty()) {
                SwissChessTeam team = new SwissChessTeam();
                team.setTeamName(lstEntry.getTeamName());
                teams.add(team);
            }
        });

        return teams;
    }

    List<SwissChessPlayer> getPlayersFromLstEntries(List<SwissChessLstEntry> lstEntries) {
        List<SwissChessPlayer> players = Lists.newArrayList();

        lstEntries.forEach(lstEntry -> {
            SwissChessPlayer player = new SwissChessPlayer();
            player.setPlayerName(lstEntry.getPlayerName());
            player.setTeamName(lstEntry.getTeamName());
            player.setAccessToken(lstEntry.getAccessToken());
            players.add(player);
        });

        return players;
    }

    List<SwissChessLstEntry> importLstEntries(InputStream lstInputStream) {

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.builder()
                .setColumnSeparator(';')
                .addColumn("playerName")
                .addColumn("teamName")
                .addColumn("ignored1")
                .addColumn("eloRating")
                .addColumn("dwzRating")
                .addColumn("fideTitle")
                .addColumn("birthDay")
                .addColumn("ignored2")
                .addColumn("ignored3")
                .addColumn("ignored4")
                .addColumn("gender")
                .addColumn("ignored5")
                .addColumn("ignored6")
                .addColumn("ignored7")
                .addColumn("ignored8")
                .addColumn("accessToken")
                .addColumn("ignored10")
                .addColumn("ignored11")
                .addColumn("ignored12")
                .addColumn("ignored13")
                .addColumn("ignored14")
                .build();

        List<SwissChessLstEntry> csvEntries = Lists.newArrayList();
        try {
            csvEntries = csvMapper
                    .readerFor(SwissChessLstEntry.class)
                    .with(csvSchema)
                    .<SwissChessLstEntry>readValues(lstInputStream)
                    .readAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvEntries;

    }

}
