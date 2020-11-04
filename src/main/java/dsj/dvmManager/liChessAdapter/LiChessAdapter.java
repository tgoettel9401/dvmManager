package dsj.dvmManager.liChessAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import dsj.dvmManager.player.Player;
import reactor.core.publisher.Mono;

@Service
public class LiChessAdapter {
	
	private final Logger logger = LoggerFactory.getLogger(LiChessAdapter.class);
	
	private final WebClient webClient = WebClient.create("https://lichess.org");
	
	private final String clockLimit = "600"; // in seconds
	private final String clockIncrement = "10"; // in seconds
	private final String variant = "standard"; // standard-chess, could also be e.g. Chess960
	
	public Challenge createChallenge(Player playerWhite, Player playerBlack) {
		
		LiChessAccount accountPlayerWhite = readAccount(playerWhite);
		LiChessAccount accountPlayerBlack = readAccount(playerBlack);
		
		Challenge challenge = new Challenge();
		
		String logMessage = new StringBuilder()
				.append("Creating challenge for players ")
				.append(playerWhite.getLastName())
				.append(" (LiChess: ")
				.append(accountPlayerWhite.getUsername())
				.append("), ")
				.append(playerBlack.getLastName())
				.append(" (LiChess: ")
				.append(accountPlayerBlack.getUsername())
				.append(") ")
				.toString();
		logger.info(logMessage);
		
		// Challenge is always sent from playerWhite (using accessToken of accountPlayerWhite). 
		// Challenged is playerBlack. AcceptByToken is used from PlayerBlack as well.
		String uri = "/api/challenge/" + accountPlayerBlack.getUsername();
		String accessToken = playerWhite.getAccessToken();
		String acceptByToken = playerBlack.getAccessToken();
		
		Mono<ClientResponse> result = webClient.post()
			      .uri(uri)
			      .headers(h -> h.setBearerAuth(accessToken))
			      .body(BodyInserters.fromFormData("clock.limit", clockLimit)
			    		  .with("clock.increment", clockIncrement)
			    		  .with("color", "black")
			    		  .with("variant", variant)
			    		  .with("acceptByToken", acceptByToken))
			      .accept(MediaType.APPLICATION_JSON)
			      .exchange();
		
		String resultString = result.flatMap(response -> response.bodyToMono(String.class)).block();
		
		LiChessChallenge lichessChallenge = result.block()
				.toEntity(LiChessChallenge.class)
				.block()
				.getBody();
	
		return challenge; 
	}
	
	public LiChessAccount readAccount(Player player) {
		
		Mono<ClientResponse> result = webClient.get()
			      .uri("/api/account")
			      .headers(h -> h.setBearerAuth(player.getAccessToken()))
			      .accept(MediaType.APPLICATION_JSON)
			      .exchange();
		
		LiChessAccount account = result.block()
				.toEntity(LiChessAccount.class)
				.block()
				.getBody();
		
		return account;
		
	}
	
}
