package dsj.dvmManager.swissChessImport;

import dsj.dvmManager.pgnParser.PgnResult;
import lombok.Data;

@Data
public class SwissChessGame {
    private PgnResult result;
    private String playerNameWhite;
    private String playerNameBlack;
    private String teamNameWhite;
    private String teamNameBlack;
}
