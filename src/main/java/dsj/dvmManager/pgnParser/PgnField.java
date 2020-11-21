package dsj.dvmManager.pgnParser;

import lombok.Getter;

@Getter
public enum PgnField {
    RESULT("[Result "), PLAYER_WHITE("[White "), PLAYER_BLACK("[Black "),
    TEAM_WHITE("[WhiteTeam "), TEAM_BLACK("[BlackTeam "), EVENT("[Event ");

    private String fieldString;

    PgnField(String fieldString) {
        this.fieldString = fieldString;
    }

}
