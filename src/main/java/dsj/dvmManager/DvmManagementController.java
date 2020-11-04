package dsj.dvmManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dsj.dvmManager.liChessAdapter.Challenge;

@RestController
public class DvmManagementController {
	
	@Autowired
	private DvmManagementService dvmManagementService;
	
	@GetMapping("api/createChallenges")
	public List<Challenge> createChallenges() {
		List<Challenge> challenges = dvmManagementService.createChallenges();
		return challenges;
	}

}
