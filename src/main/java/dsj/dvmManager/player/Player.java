package dsj.dvmManager.player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Player {
	
	@Id @GeneratedValue
	private Long id;
	
	private String firstName;
	private String lastName;
	
	private String accessToken;
}
