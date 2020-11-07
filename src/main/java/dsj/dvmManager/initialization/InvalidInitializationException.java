package dsj.dvmManager.initialization;

import lombok.NoArgsConstructor;

public class InvalidInitializationException extends Exception {

    InvalidInitializationException() {
        this("Initialization Data seems to be invalid. Please check the property file.");
    }

    InvalidInitializationException(String errorMessage) {
        super("");
    }
}
