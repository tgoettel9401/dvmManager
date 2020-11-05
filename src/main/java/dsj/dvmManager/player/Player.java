package dsj.dvmManager.player;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import dsj.dvmManager.team.Team;
import lombok.Data;

@Data
@Entity
public class Player {
	
	@Id @GeneratedValue
	private Long id;
	
	private String firstName;
	private String lastName;
	
	private String accessToken;
	
	@ManyToOne
	private Team team; 
	
	public String getName() {
		return lastName + ", " + firstName;
	}

}
