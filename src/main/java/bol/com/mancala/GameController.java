package bol.com.mancala;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value = "/game/instance")
    @ApiOperation(value = "Endpoint to creat a new Mancala game instance",
            produces = "Application/JSON",
            response = GameModel.class, httpMethod = "POST")
    public ResponseEntity<GameModel> createGameInstance(@Valid @RequestBody GameInstanceApiRequestModel requestModel) {
        String id = requestModel.getGameId();
        GameModel game = gameService.createInstance(id, configFile.getPitStones());
        LOGGER.debug("Game instance {} with id {} created", game, id);
        cache.update(game);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }


    @PostMapping(value = "/sow")
    @ApiOperation(value = "Endpoint used for sowing the game. It also maintains the last state of game for consecutive calls",
            produces = "Application/JSON",
            response = GameModel.class, httpMethod = "POST")
    public ResponseEntity<GameModel> sow(@Valid @RequestBody SowApiRequestModel requestModel) {
        Integer pitIndex = requestModel.getPitIndex();

        if (pitIndex == null ||
                pitIndex < 1 || pitIndex >= MancalaConstants.LEFT_BIG_PIT_ID ||
                pitIndex == MancalaConstants.RIGHT_BIG_PIT_ID
        ) {
            throw new InputInvalidException(ErrorConstants.INPUT_INVALID_EXCEPTION);
        }
        GameModel game = cache.get(requestModel.getGameId());
        game = sowService.sow(game, pitIndex);
        cache.update(game);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping(value = "gameStatus")
    @ApiOperation(value = "Endpoint used to return the last state of the game",
            produces = "Application/JSON", response = GameModel.class, httpMethod = "POST")
    public ResponseEntity<GameModel> get(@Valid @RequestBody StatusApiRequestModel requestModel) {
        return new ResponseEntity<>(cache.get(requestModel.getGameId()), HttpStatus.OK);
    }
}
