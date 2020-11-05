package dsj.dvmManager.liChessAdapter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class LiChessGame {
	
	private String id; 
	private String status;
	private String moves; 
	private String pgn; 
	
	private Long createdAt;
	private Long lastMoveAt;
	
	public ZonedDateTime getCreatedAt() {
		return Instant.ofEpochMilli(createdAt).atZone(ZoneId.of("CET"));
	}
	
	public ZonedDateTime getLastMoveAt() {
		return Instant.ofEpochMilli(lastMoveAt).atZone(ZoneId.of("CET"));
	}

}
