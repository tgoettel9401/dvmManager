package dsj.dvmManager.swissChessImport;

import lombok.Data;

import java.util.List;

@Data
public class SwissChessPgnResult {
    private List<SwissChessGame> games;
}
