package dsj.dvmManager.configuration;

public class InvalidInitializationException extends Exception {

    InvalidInitializationException() {
        this("Initialization Data seems to be invalid. Please check the property file.");
    }

    InvalidInitializationException(String errorMessage) {
        super("");
    }
}
