package dsj.dvmManager.teamMatch;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.google.common.collect.Lists;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.team.Team;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
public class TeamMatch {
	
	@Id @GeneratedValue
	private Long id;
	
	@ManyToOne
	private Team teamHome;
	
	@ManyToOne
	private Team teamAway;
	
	@OneToMany(mappedBy = "teamMatch", fetch = FetchType.LAZY)
	@ToString.Exclude
	private List<Game> games = Lists.newArrayList();

}
