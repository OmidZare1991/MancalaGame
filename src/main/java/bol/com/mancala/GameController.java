package bol.com.mancala;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;
    @Autowired
    private MancalaSowServiceImpl sowService;
    @Autowired
    private ConfigFile configFile;
    @Autowired
    private CacheProvider cache;

    @GetMapping(value = "game/instance")
    @ApiOperation(value = "Endpoint to creat a new Mancala game instance",
            produces = "Application/JSON",
            response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> createGameInstance() {
        String id = UUID.randomUUID().toString();
        MancalaGame game = gameService.createInstance(id, configFile.getPitStones());
        LOGGER.debug("Game instance {} with id {} created", game, id);
        cache.update(game);
        return ResponseEntity.ok(game);
    }




    @PostMapping(value = "sow/{gameId}/{pitIndex}")
    @ApiOperation(value = "Endpoint used for sowing the game. It also maintains the last state of game for consecutive calls",
            produces = "Application/JSON",
            response = MancalaGame.class, httpMethod = "POST")
    public ResponseEntity<MancalaGame> sow(@Valid @RequestBody SowApiRequestModel requestModel) {
        Integer pitIndex = requestModel.getPitIndex();

        if (pitIndex == null ||
                pitIndex < 1 || pitIndex >= MancalaConstants.LEFT_BIG_PIT_ID ||
                pitIndex == MancalaConstants.RIGHT_BIG_PIT_ID
        ) {
            throw new InputInvalidException(ErrorConstants.INPUT_INVALID_EXCEPTION);
        }
        MancalaGame game = cache.get(requestModel.getGameId());
        game = sowService.sow(game, pitIndex);
        cache.update(game);
        return ResponseEntity.ok(game);
    }

    @PostMapping(value = "{gameStatus}")
    @ApiOperation(value = "Endpoint used to return the last state of the game",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "POST")
    public ResponseEntity<MancalaGame> get(@Valid @RequestBody StatusApiRequestModel requestModel) {
        return ResponseEntity.ok(cache.get(requestModel.getGameId()));
    }
}
