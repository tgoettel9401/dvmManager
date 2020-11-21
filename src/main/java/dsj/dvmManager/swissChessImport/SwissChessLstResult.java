package dsj.dvmManager.swissChessImport;

import lombok.Data;

import java.util.List;

@Data
public class SwissChessLstResult {
    private List<SwissChessPlayer> players;
    private List<SwissChessTeam> teams;
}
