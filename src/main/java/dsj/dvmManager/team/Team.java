package dsj.dvmManager.team;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Lists;

import dsj.dvmManager.player.Player;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class Team {
	
	@Id @GeneratedValue
	private Long id;
	
	private String name; 
	
	@OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
	@ToString.Exclude
	@JsonBackReference
	private List<Player> players = Lists.newArrayList();

}
