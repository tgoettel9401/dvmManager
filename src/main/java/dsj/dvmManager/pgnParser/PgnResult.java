package dsj.dvmManager.pgnParser;

import lombok.Getter;

@Getter
public enum PgnResult {
    WHITE_WINS("1-0"), BLACK_WINS("0-1"), DRAW("1/2-1/2"), UNKNOWN("");

    private String resultString;

    PgnResult(String resultString) {
        this.resultString = resultString;
    }

    static PgnResult fromResultString(String resultString) {
        switch (resultString) {
            case "1-0":
                return WHITE_WINS;
            case "0-1":
                return BLACK_WINS;
            case "0.5-0.5":
                return DRAW;
            default:
                return UNKNOWN;
        }
    }
}
