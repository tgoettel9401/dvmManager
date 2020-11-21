package dsj.dvmManager.game;

import dsj.dvmManager.pgnParser.PgnResult;
import dsj.dvmManager.player.Player;
import dsj.dvmManager.player.PlayerNotFoundException;
import dsj.dvmManager.player.PlayerService;
import dsj.dvmManager.swissChessImport.SwissChessGame;
import dsj.dvmManager.team.TeamNotFoundException;
import dsj.dvmManager.teamMatch.TeamMatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class GameServiceTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private TeamMatchService teamMatchService;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void createGameForSwissChessGame() throws TeamNotFoundException, PlayerNotFoundException {

        SwissChessGame swGame = mock(SwissChessGame.class);
        when(swGame.getResult()).thenReturn(PgnResult.WHITE_WINS);
        when(swGame.getPlayerNameWhite()).thenReturn("Player-White");
        when(swGame.getPlayerNameBlack()).thenReturn("Player-Black");
        when(swGame.getTeamNameWhite()).thenReturn("Team-White");
        when(swGame.getTeamNameBlack()).thenReturn("Team-Black");

        Player playerWhite = mock(Player.class);
        when(playerWhite.getName()).thenReturn("Player-White");
        when(playerService.findByNameAndTeamName("Player-White", "Team-White")).thenReturn(playerWhite);
        Player playerBlack = mock(Player.class);
        when(playerBlack.getName()).thenReturn("Player-Black");
        when(playerService.findByNameAndTeamName("Player-Black", "Team-Black")).thenReturn(playerBlack);

        Game correctGame = mock(Game.class);
        when(correctGame.getResult()).thenReturn(PgnResult.WHITE_WINS.getResultString());
        when(correctGame.getPlayerWhite()).thenReturn(playerWhite);
        when(correctGame.getPlayerBlack()).thenReturn(playerBlack);

        when(gameRepository.save(any())).thenReturn(correctGame);

        Game returnedGame = gameService.createGameForSwissChessGame(swGame, null);
        assertThat(returnedGame.getResult()).isEqualTo(correctGame.getResult());
        assertThat(returnedGame.getPlayerWhite()).isEqualTo(correctGame.getPlayerWhite());
        assertThat(returnedGame.getPlayerBlack()).isEqualTo(correctGame.getPlayerBlack());

    }
}