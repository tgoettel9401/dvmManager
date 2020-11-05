package dsj.dvmManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class DvmManagementScheduler {
	
	private final DvmManagementService dvmManagementService;
	
	@Autowired
	public DvmManagementScheduler(DvmManagementService dvmManagementService) {
		this.dvmManagementService = dvmManagementService;
	}
	
	@Scheduled(fixedDelay = 15000) // 15 seconds
	public void scheduleUpdateGames() {
		dvmManagementService.updateGames();
	}

}
