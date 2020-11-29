package dsj.dvmManager.game;

import lombok.Getter;

@Getter
public enum GameResult {
    WHITE_WINS("1.0 - 0.0"), BLACK_WINS("0.0 - 1.0"), DRAW("0.5 - 0.5"), UNKNOWN(" - ");

    private String resultString;

    GameResult(String resultString) {
        this.resultString = resultString;
    }

    public GameResult getOppositeResult() {
        if (this.equals(WHITE_WINS))
            return BLACK_WINS;
        if (this.equals(BLACK_WINS))
            return WHITE_WINS;
        else
            return this;
    }

    public double getPointsWhite() {
        if (this.equals(WHITE_WINS))
            return 1.0;
        if (this.equals(DRAW))
            return 0.5;
        else
            return 0.0;
    }

    public double getPointsBlack() {
        if (this.equals(BLACK_WINS))
            return 1.0;
        if (this.equals(DRAW))
            return 0.5;
        else
            return 0.0;
    }

    static GameResult fromResultString(String resultString) {
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
