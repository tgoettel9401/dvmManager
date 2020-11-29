package dsj.dvmManager.teamMatch;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import dsj.dvmManager.game.GameDto;
import lombok.Data;

@Data
public class TeamMatchDto {

	private String teamHome;
	private String teamAway;
	
	private List<GameDto> games = Lists.newArrayList();

	@JsonIgnore
	public double getPointsTeamHome() {
		double points = 0.0;
		for (GameDto game : games)
			points += game.getPointsPlayerHome();
		return points;
	}

	@JsonIgnore
	public double getPointsTeamAway() {
		double points = 0.0;
		for (GameDto game : games)
			points += game.getPointsPlayerAway();
		return points;
	}

	public String getResult() {
		return "" + getPointsTeamHome() + " - " + getPointsTeamAway();
	}
	
}
