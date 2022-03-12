package com.bol.mancala.controller;

import com.bol.mancala.model.MancalaGame;
import com.bol.mancala.model.dto.SowDto;
import com.bol.mancala.service.MancalaGameService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/mancala-game/",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class MancalaGameController {

    @Autowired
    private MancalaGameService gameService;

    @GetMapping(value = "start/{pitStones}")
    @ApiOperation(value = "Endpoint to creat a new Mancala game instance",
            produces = "Application/JSON",
            response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> createGameInstance(@PathVariable Integer pitStones) {
        return new ResponseEntity<>(gameService.getNewGame(pitStones), HttpStatus.OK);
    }


    @PostMapping(value = "sow")
    @ApiOperation(value = "Endpoint used for sowing the game. It also maintains the last state of game for consecutive calls",
            produces = "Application/JSON",
            response = MancalaGame.class, httpMethod = "POST")
    public ResponseEntity<MancalaGame> sow(@Valid @RequestBody SowDto dto) {
        return new ResponseEntity<>(this.gameService.playGame(dto.getGameId(), dto.getPitId()), HttpStatus.OK);
    }

    @GetMapping(value = "last-state/{gameId}")
    @ApiOperation(value = "Endpoint used to return the last state of the game",
            produces = "Application/JSON", response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> get(@Valid @PathVariable String gameId) {
        return new ResponseEntity<>(this.gameService.getGame(gameId), HttpStatus.OK);
    }
}
