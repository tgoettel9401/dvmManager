package dsj.dvmManager.swissChessImport;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

class SwissChessLstImportServiceTest {

    @Spy
    private SwissChessLstImportService importService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testLstImport() throws IOException {

        InputStream inputStream = getLstImportStream();

        List<SwissChessLstEntry> importedLstEntries = importService.importLstEntries(inputStream);

        int playerNumberCounter = 1;
        for (SwissChessLstEntry lstEntry : importedLstEntries) {
            assertThat(lstEntry.getPlayerName()).isEqualTo("Player" + playerNumberCounter + "LastName,FirstName");
            assertThat(lstEntry.getTeamName()).isEqualTo("Team" + playerNumberCounter);
            assertThat(lstEntry.getAccessToken()).isEqualTo("AccessToken" + playerNumberCounter);
            playerNumberCounter++;
        }

    }

    @Test
    void importSwissChessLst() throws IOException {

        InputStream inputStream = getLstImportStream();

        // LST-Entries
        List<SwissChessLstEntry> lstEntries = Lists.newArrayList();
        for (int i = 1; i <= 5; i++) {
            SwissChessLstEntry lstEntry = mock(SwissChessLstEntry.class);
            lstEntries.add(lstEntry);
        }
        doReturn(lstEntries).when(importService).importLstEntries(inputStream);

        // Players
        List<SwissChessPlayer> correctPlayers = Lists.newArrayList();
        for (int i = 1; i <= 5; i++) {
            SwissChessPlayer swPlayer = mock(SwissChessPlayer.class);
            correctPlayers.add(swPlayer);
        }
        doReturn(correctPlayers).when(importService).getPlayersFromLstEntries(lstEntries);

        // Teams
        List<SwissChessTeam> correctTeams = Lists.newArrayList();
        for (int i = 1; i <= 5; i++) {
            SwissChessTeam swTeam = mock(SwissChessTeam.class);
            correctTeams.add(swTeam);
        }
        doReturn(correctTeams).when(importService).getTeamsFromLstEntries(lstEntries);

        SwissChessLstResult swissChessLstResult = importService.importSwissChessLst(inputStream);
        assertThat(swissChessLstResult.getPlayers()).isEqualTo(correctPlayers);
        assertThat(swissChessLstResult.getTeams()).isEqualTo(correctTeams);

    }

    @Test
    void getTeamsFromLstEntries() {

        List<SwissChessLstEntry> lstEntries = Lists.newArrayList();
        for (int i = 1; i <= 5; i++) {
            SwissChessLstEntry lstEntry = new SwissChessLstEntry();
            lstEntry.setTeamName("Team " + i);
            lstEntries.add(lstEntry);
        }

        List<SwissChessTeam> returnedTeams = importService.getTeamsFromLstEntries(lstEntries);
        assertThat(returnedTeams).hasSize(5);
        for (int i = 1; i <= 5; i++) {
            assertThat(returnedTeams.get(i - 1).getTeamName()).isEqualTo("Team " + i);
        }

    }

    @Test
    void getPlayersFromLstEntries() {

        List<SwissChessLstEntry> lstEntries = Lists.newArrayList();
        for (int i = 1; i <= 5; i++) {
            SwissChessLstEntry lstEntry = new SwissChessLstEntry();
            lstEntry.setPlayerName("Player " + i);
            lstEntry.setTeamName("Team " + i);
            lstEntry.setAccessToken("Access-Token " + i);
            lstEntries.add(lstEntry);
        }

        List<SwissChessPlayer> returnedPlayers = importService.getPlayersFromLstEntries(lstEntries);
        assertThat(returnedPlayers).hasSize(5);
        for (int i = 1; i <= 5; i++) {
            assertThat(returnedPlayers.get(i - 1).getPlayerName()).isEqualTo("Player " + i);
            assertThat(returnedPlayers.get(i - 1).getTeamName()).isEqualTo("Team " + i);
            assertThat(returnedPlayers.get(i - 1).getAccessToken()).isEqualTo("Access-Token " + i);
        }

    }

    private InputStream getLstImportStream() throws IOException {
        return new ClassPathResource("playersAndTeamsFromSwissChess.lst").getInputStream();
    }
}