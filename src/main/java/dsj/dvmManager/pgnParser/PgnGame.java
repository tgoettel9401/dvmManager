package dsj.dvmManager.pgnParser;

import lombok.Data;

@Data
public class PgnGame {

    // PGN consists of many more information, but only provided are saved here
    private PgnResult result;
    private String playerNameWhite;
    private String playerNameBlack;
    private String teamNameWhite;
    private String teamNameBlack;

}
