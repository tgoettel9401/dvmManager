package dsj.dvmManager.pgnParser;

import lombok.Getter;

@Getter
public enum PgnField {
    RESULT("[Result "), PLAYER_WHITE("[White "), PLAYER_BLACK("[Black "),
    TEAM_WHITE("[WhiteTeam "), TEAM_BLACK("[BlackTeam "), EVENT("[Event "),
    BOARD("[Board ");

    private String fieldString;

    PgnField(String fieldString) {
        this.fieldString = fieldString;
    }

}
