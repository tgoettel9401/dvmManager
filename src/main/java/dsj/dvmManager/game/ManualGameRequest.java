package dsj.dvmManager.game;

import lombok.Data;

@Data
public class ManualGameRequest {
    private Long playerWhiteId;
    private Long playerBlackId;
    private Long teamMatchId;
    private Integer boardNumber;
}
