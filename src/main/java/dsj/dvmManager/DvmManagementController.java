package dsj.dvmManager;

import dsj.dvmManager.game.Game;
import dsj.dvmManager.liChessAdapter.LiChessChallenge;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.teamMatch.TeamMatchDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class DvmManagementController {

    private final DvmManagementService dvmManagementService;

    public DvmManagementController(DvmManagementService dvmManagementService) {
        this.dvmManagementService = dvmManagementService;
    }

    @GetMapping("api/createChallenges")
    public List<LiChessChallenge> createChallenges() {
        return dvmManagementService.createChallenges();
    }

    @GetMapping("api/getTeamMatches")
    public List<TeamMatchDto> getTeamMatches() {
        return dvmManagementService.findAllTeamMatchDtos();
    }

    @GetMapping("api/updateGames")
    public void updateGames() {
        dvmManagementService.updateGames();
    }

    @PostMapping("api/importSwissChessLstFile")
    public List<Player> importSwissChessLstFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return dvmManagementService.importSwissChessLstFile(multipartFile.getInputStream());
    }

    @PostMapping("api/importSwissChessPgnFile")
    public List<Game> importSwissChessPgnFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return dvmManagementService.importSwissChessPgnFile(multipartFile.getInputStream());
    }

}
