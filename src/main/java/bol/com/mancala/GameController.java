package bol.com.mancala;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private MancalaApiServiceImpl apiService;
    @Autowired
    private MancalaSowServiceImpl sowService;
    @Autowired
    private ConfigFile configFile;

    @GetMapping(value = "game/instance")
    @ApiOperation(value = "Endpoint to creat a new Mancala game instance",
            produces = "Application/JSON",
            response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> createInstance() {
        String id = UUID.randomUUID().toString();
        MancalaGame game = apiService.createGame(configFile.getPitStones(), id);
        LOGGER.debug("Game instance {} with id {} created", game, id);
        apiService.updateMancalaGame(game);
        return ResponseEntity.ok(game);
    }

    @PostMapping(value = "sow/{gameId}/{pitIndex}")
    @ApiOperation(value = "Endpoint used for sowing the game. It also maintains the last state of game for consecutive calls",
            produces = "Application/JSON",
            response = MancalaGame.class, httpMethod = "POST")
    public ResponseEntity<MancalaGame> sow(
            @ApiParam(value = "The id created by createdInstance endpoint. It must have a value and cannot be null")
            @PathVariable(value = "gameId") String gameId,
            @PathVariable(value = "pitIndex") Integer pitIndex
    ) {
        if (pitIndex == null ||
                pitIndex < 1 || pitIndex >= MancalaConstants.LEFT_BIG_PIT_ID ||
                pitIndex == MancalaConstants.RIGHT_BIG_PIT_ID
        ) {
            throw new MancalaException("Invalid pit Index!. It should be between 1..6 or 8..13");
        }
        MancalaGame game = apiService.loadMancalaGame(gameId);
        game = sowService.sow(game, pitIndex);
        apiService.updateMancalaGame(game);
        return ResponseEntity.ok(game);
    }

    @GetMapping(value = "{gameId}")
    @ApiOperation(value = "Endpoint used to return the last state of the game",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> get(
            @ApiParam(value = "The id created by createdInstance endpoint")
            @PathVariable(value = "gameId"
            ) String gameId) {
        return ResponseEntity.ok(apiService.loadMancalaGame(gameId));
    }
}
