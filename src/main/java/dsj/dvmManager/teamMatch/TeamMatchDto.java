package dsj.dvmManager.teamMatch;

import java.util.List;

import com.google.common.collect.Lists;

import dsj.dvmManager.game.GameDto;
import lombok.Data;

@Data
public class TeamMatchDto {

	private String teamWhite;
	private String teamBlack;
	
	private List<GameDto> games = Lists.newArrayList();
	
}
