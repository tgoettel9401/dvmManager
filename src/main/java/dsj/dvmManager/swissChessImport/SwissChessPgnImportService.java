package dsj.dvmManager.swissChessImport;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import dsj.dvmManager.pgnParser.PgnGame;
import dsj.dvmManager.pgnParser.PgnParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class SwissChessPgnImportService {

    private final PgnParserService pgnParserService;

    @Autowired
    public SwissChessPgnImportService(PgnParserService pgnParserService) {
        this.pgnParserService = pgnParserService;
    }

    public SwissChessPgnResult importSwissChessPgn(InputStream inputStream) throws IOException {
        SwissChessPgnResult swissChessPgnResult = new SwissChessPgnResult();
        swissChessPgnResult.setGames(createGames(inputStream));
        return swissChessPgnResult;
    }

    List<SwissChessGame> createGames(InputStream inputStream) throws IOException {
        List<SwissChessGame> swissChessGames = Lists.newArrayList();
        List<PgnGame> pgnGames = pgnParserService.createPgnListFromString(convertInputStreamToString(inputStream));
        pgnGames.forEach(pgnGame -> swissChessGames.add(getGameFromPgnGame(pgnGame)));
        return swissChessGames;
    }

    SwissChessGame getGameFromPgnGame(PgnGame pgnGame) {
        SwissChessGame swissChessGame = new SwissChessGame();
        swissChessGame.setResult(pgnGame.getResult());
        swissChessGame.setPlayerNameWhite(pgnGame.getPlayerNameWhite());
        swissChessGame.setPlayerNameBlack(pgnGame.getPlayerNameBlack());
        swissChessGame.setTeamNameWhite(pgnGame.getTeamNameWhite());
        swissChessGame.setTeamNameBlack(pgnGame.getTeamNameBlack());
        swissChessGame.setBoardNumber(pgnGame.getBoardNumber());
        return swissChessGame;
    }

    String convertInputStreamToString(InputStream inputStream) throws IOException {
        return new ByteSource() {
            @Override
            public InputStream openStream() {
                return inputStream;
            }
        }.asCharSource(Charsets.UTF_8).read();
    }
}
