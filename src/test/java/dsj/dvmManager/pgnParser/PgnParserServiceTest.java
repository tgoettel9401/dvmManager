package dsj.dvmManager.pgnParser;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PgnParserServiceTest {

    private PgnParserService pgnParserService = new PgnParserService();

    @Test
    void createPgnFromString_withOnlyOneGame() throws IOException {
        String gamePgnString = getPgnString_withOnlyOneGame();
        List<PgnGame> returnedPgns = pgnParserService.createPgnListFromString(gamePgnString);
        assertThat(returnedPgns).hasSize(1);
        assertThat(returnedPgns.get(0).getResult()).isEqualTo(PgnResult.BLACK_WINS);
    }

    @Test
    void createPgnFromString_withMultipleGames() throws IOException {
        String gamePgnString = getPgnString_withMultipleGames();
        List<PgnGame> pgns = pgnParserService.createPgnListFromString(gamePgnString);
        assertThat(pgns).hasSize(3);
        int pgnNumberCounter = 1;
        for (PgnGame pgn : pgns) {
            assertThat(pgn.getResult()).isEqualTo(PgnResult.UNKNOWN);
            assertThat(pgn.getPlayerNameWhite()).isEqualTo("PlayerWhite" + pgnNumberCounter);
            assertThat(pgn.getPlayerNameBlack()).isEqualTo("PlayerBlack" + pgnNumberCounter);
            assertThat(pgn.getTeamNameWhite()).isEqualTo("TeamWhite" + pgnNumberCounter);
            assertThat(pgn.getTeamNameBlack()).isEqualTo("TeamBlack" + pgnNumberCounter);
            pgnNumberCounter++;
        }
    }

    @Test
    void createPgnRawFromString_withOnlyOneGame() throws IOException {
        String gamePgnString = getPgnString_withOnlyOneGame();
        List<String> gamePgnStringDividedIntoGames = pgnParserService.divideIntoLines(gamePgnString);
        PgnGameRaw returnedPgnGameRaw = pgnParserService.createPgnRawFromString(gamePgnStringDividedIntoGames);
        assertThat(returnedPgnGameRaw.getResult()).isEqualTo("0-1");
        assertThat(returnedPgnGameRaw.getPlayerNameWhite()).isEqualTo("kasparow94");
        assertThat(returnedPgnGameRaw.getPlayerNameBlack()).isEqualTo("opponent28");
        assertThat(returnedPgnGameRaw.getTeamNameWhite()).isNull();
        assertThat(returnedPgnGameRaw.getTeamNameBlack()).isNull();
    }

    @Test
    void testDivideIntoGames_withOnlyOneGame() throws IOException {
        String gamePgnString = getPgnString_withOnlyOneGame();
        List<String> gamePgnStringDividedIntoGames = pgnParserService.divideIntoLines(gamePgnString);
        List<List<String>> returnedGameString = pgnParserService.divideIntoGames(gamePgnStringDividedIntoGames);
        assertThat(returnedGameString).hasSize(1);
        returnedGameString.forEach(gameString -> assertThat(gameString.get(0).startsWith("[Event ")));
    }

    @Test
    void testDivideIntoGames_withMultipleGames() throws IOException {
        String gamePgnString = getPgnString_withMultipleGames();
        List<String> gamePgnStringDividedIntoGames = pgnParserService.divideIntoLines(gamePgnString);
        List<List<String>> returnedGameString = pgnParserService.divideIntoGames(gamePgnStringDividedIntoGames);
        assertThat(returnedGameString).hasSize(3);
        returnedGameString.forEach(gameString -> assertThat(gameString.get(0).startsWith("[Event ")));
    }

    @Test
    void testDivideIntoLines() throws IOException {
        String gamePgnString = getPgnString_withOnlyOneGame();
        List<String> returnedLines = pgnParserService.divideIntoLines(gamePgnString);
        assertThat(returnedLines).hasSize(17);
        assertThat(returnedLines).contains("[Result \"0-1\"]\r");
    }

    private String getPgnString_withOnlyOneGame() throws IOException {
        InputStream inputStream = new ClassPathResource("gameFromLiChess.pgn").getInputStream();
        String returnString;
        try (Reader reader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
            returnString = CharStreams.toString(reader);
        }
        return returnString;
    }

    private String getPgnString_withMultipleGames() throws IOException {
        InputStream inputStream = new ClassPathResource("gamesFromSwissChess.pgn").getInputStream();
        String returnString;
        try (Reader reader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
            returnString = CharStreams.toString(reader);
        }
        return returnString;
    }

}