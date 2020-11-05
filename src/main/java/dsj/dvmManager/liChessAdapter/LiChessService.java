package dsj.dvmManager.liChessAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.base.Optional;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.game.GameService;
import dsj.dvmManager.player.Player;
import reactor.core.publisher.Mono;

@Service
public class LiChessService {
	
	@Autowired
	private GameService gameService; 
	
	private final Logger logger = LoggerFactory.getLogger(LiChessService.class);
	
	private final WebClient webClient = WebClient.create("https://lichess.org");
	
	private final String clockLimit = "600"; // in seconds
	private final String clockIncrement = "10"; // in seconds
	private final String variant = "standard"; // standard-chess, could also be e.g. Chess960
	
	public LiChessChallenge createChallenge(Game game) {
		
		Player playerWhite = game.getPlayerWhite();
		Player playerBlack = game.getPlayerBlack();
		
		LiChessAccount accountPlayerWhite = readAccount(playerWhite);
		LiChessAccount accountPlayerBlack = readAccount(playerBlack);
		
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
		
		LiChessChallenge challenge = result.block()
				.toEntity(LiChessChallenge.class)
				.block()
				.getBody();
		
		Optional<LiChessChallengeGame> lichessGameOptional = Optional.fromNullable(challenge.getGame());
		if (lichessGameOptional.isPresent()) {
			game.setLiChessGameId(lichessGameOptional.get().getId());
			gameService.save(game);
		}
	
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
	
	public LiChessGame getLiChessGame(Game game) {
		
		String uri = "/game/export/" + game.getLiChessGameId();
		
		Mono<ClientResponse> result = webClient.get()
			      .uri(uriBuilder -> uriBuilder
			    		  .path(uri)
			    		  .queryParam("evals", "false")
			    		  .queryParam("opening", "false")
			    		  .queryParam("pgnInJson", "true")
			    		  .build())
			      .accept(MediaType.APPLICATION_JSON)
			      .exchange();
		
		LiChessGame liChessGame = result.block()
				.toEntity(LiChessGame.class)
				.block()
				.getBody();
		
		return liChessGame; 
	}

}
