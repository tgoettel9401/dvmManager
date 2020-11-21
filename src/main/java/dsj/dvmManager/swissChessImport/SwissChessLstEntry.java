package dsj.dvmManager.swissChessImport;

import lombok.Data;

@Data
public class SwissChessLstEntry {
    private String playerName;
    private String teamName;
    private String ignored1;
    private Integer eloRating;
    private Integer dwzRating;
    private String fideTitle;
    private String birthDay;
    private String ignored2;
    private String ignored3;
    private String ignored4;
    private String gender;
    private String ignored5;
    private String ignored6;
    private String ignored7;
    private String ignored8;
    private String accessToken; // is set to field info_1 of SwissChess
    private String ignored10;
    private String ignored11;
    private String ignored12;
    private String ignored13;
    private String ignored14;
}
