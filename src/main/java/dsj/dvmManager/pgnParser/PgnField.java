package dsj.dvmManager.pgnParser;

import lombok.Getter;

@Getter
public enum PgnField {
    RESULT("Result");

    private String fieldString;

    PgnField(String fieldString) {
        this.fieldString = fieldString;
    }

}
