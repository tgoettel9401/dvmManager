package dsj.dvmManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dsj.dvmManager.liChessAdapter.LiChessChallenge;
import dsj.dvmManager.teamMatch.TeamMatchDto;

@RestController
public class DvmManagementController {
	
	@Autowired
	private DvmManagementService dvmManagementService;
	
	@GetMapping("api/createChallenges")
	public List<LiChessChallenge> createChallenges() {
		List<LiChessChallenge> challenges = dvmManagementService.createChallenges();
		return challenges;
	}
	
	@GetMapping("api/getTeamMatches")
	public List<TeamMatchDto> getTeamMatches() {
		return dvmManagementService.findAllTeamMatchDtos();
	}

}
