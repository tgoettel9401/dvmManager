package dsj.dvmManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class DvmManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DvmManagerApplication.class, args);
		openHomePage();
	}

	private static void openHomePage() {
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("cmd /c start chrome.exe http://localhost:8081");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
