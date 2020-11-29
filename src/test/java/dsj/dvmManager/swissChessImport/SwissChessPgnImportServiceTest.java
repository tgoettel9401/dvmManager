package dsj.dvmManager.swissChessImport;

import com.google.common.collect.Lists;
import dsj.dvmManager.pgnParser.PgnGame;
import dsj.dvmManager.pgnParser.PgnParserService;
import dsj.dvmManager.pgnParser.PgnResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class SwissChessPgnImportServiceTest {

    @Spy
    @InjectMocks
    private SwissChessPgnImportService swissChessPgnImportService;

    @Mock
    private PgnParserService pgnParserService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void importSwissChessPgn() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        List<SwissChessGame> correctGames = Lists.newArrayList();
        correctGames.add(mock(SwissChessGame.class));
        correctGames.add(mock(SwissChessGame.class));
        doReturn(correctGames).when(swissChessPgnImportService).createGames(inputStream);
        SwissChessPgnResult returnedResult = swissChessPgnImportService.importSwissChessPgn(inputStream);
        assertThat(returnedResult.getGames()).isNotEmpty();
        assertThat(returnedResult.getGames()).hasSize(2);
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
    @Test
    void getGamesFromInputStream() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        String string = "String";
        doReturn(string).when(swissChessPgnImportService).convertInputStreamToString(inputStream);

        List<PgnGame> pgnGames = Lists.newArrayList();
        PgnGame pgnGame1 = mock(PgnGame.class);
        pgnGames.add(pgnGame1);
        PgnGame pgnGame2 = mock(PgnGame.class);
        pgnGames.add(pgnGame2);
        when(pgnParserService.createPgnListFromString(string)).thenReturn(pgnGames);

        List<SwissChessGame> correctSwissChessGames = Lists.newArrayList();
        SwissChessGame swGame1 = mock(SwissChessGame.class);
        doReturn(swGame1).when(swissChessPgnImportService).getGameFromPgnGame(pgnGame1);
        correctSwissChessGames.add(swGame1);
        SwissChessGame swGame2 = mock(SwissChessGame.class);
        doReturn(swGame2).when(swissChessPgnImportService).getGameFromPgnGame(pgnGame2);
        correctSwissChessGames.add(swGame2);

        List<SwissChessGame> returnedGames = swissChessPgnImportService.createGames(inputStream);

        assertThat(returnedGames).hasSize(2);
        assertThat(returnedGames).hasSize(correctSwissChessGames.size());
        assertThat(returnedGames.get(0)).isEqualTo(swGame1);
        assertThat(returnedGames.get(1)).isEqualTo(swGame2);
    }

    @Test
    void getGameFromPgnGame() {

        PgnGame pgnGame = mock(PgnGame.class);
        when(pgnGame.getResult()).thenReturn(PgnResult.WHITE_WINS);
        when(pgnGame.getPlayerNameWhite()).thenReturn("Player-White");
        when(pgnGame.getPlayerNameBlack()).thenReturn("Player-Black");
        when(pgnGame.getTeamNameWhite()).thenReturn("Team-White");
        when(pgnGame.getTeamNameBlack()).thenReturn("Team-Black");
        when(pgnGame.getBoardNumber()).thenReturn(1);

        SwissChessGame correctSwissChessGame = swissChessPgnImportService.getGameFromPgnGame(pgnGame);

        assertThat(correctSwissChessGame.getResult()).isEqualTo(pgnGame.getResult());
        assertThat(correctSwissChessGame.getPlayerNameWhite()).isEqualTo(pgnGame.getPlayerNameWhite());
        assertThat(correctSwissChessGame.getPlayerNameBlack()).isEqualTo(pgnGame.getPlayerNameBlack());
        assertThat(correctSwissChessGame.getTeamNameWhite()).isEqualTo(pgnGame.getTeamNameWhite());
        assertThat(correctSwissChessGame.getTeamNameBlack()).isEqualTo(pgnGame.getTeamNameBlack());
        assertThat(correctSwissChessGame.getBoardNumber()).isEqualTo(1);

    }
}