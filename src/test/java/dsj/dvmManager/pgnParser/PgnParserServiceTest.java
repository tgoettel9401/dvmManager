package dsj.dvmManager.pgnParser;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PgnParserServiceTest {

    private PgnParserService pgnParserService = new PgnParserService();

    @Test
    void createPgnFromString() throws IOException {
        String gamePgnString = getPgnString();
        Pgn pgn = pgnParserService.createPgnFromString(gamePgnString);
        assertThat(pgn.getResult()).isEqualTo(PgnResult.BLACK_WINS);
    }

    @Test
    void createPgnRawFromString() throws IOException {
        String gamePgnString = getPgnString();
        PgnRaw returnedPgnRaw = pgnParserService.createPgnRawFromString(gamePgnString);
        assertThat(returnedPgnRaw.getResult()).isEqualTo("0-1");
    }

    @Test
    void testDivideIntoLines() throws IOException {
        String gamePgnString = getPgnString();
        List<String> returnedLines = pgnParserService.divideIntoLines(gamePgnString);
        assertThat(returnedLines).hasSize(17);
        assertThat(returnedLines).contains("[Result \"0-1\"]");
    }

    private String getPgnString() throws IOException {
        return new BufferedReader(new InputStreamReader(new ClassPathResource("game.pgn").getInputStream()))
                .lines()
                .parallel()
                .collect(
                        Collectors.joining("\n"));
    }

}