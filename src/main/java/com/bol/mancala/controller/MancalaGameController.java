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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api/mancala-game/",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class MancalaGameController {

    @Autowired
    private MancalaGameService gameService;

    @GetMapping(value = "start/{pitStones}")
    @ApiOperation(value = "Endpoint to creat a new Mancala game instance",
            produces = "application/json",
            response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> createGameInstance(@PathVariable Integer pitStones) {
        return new ResponseEntity<>(gameService.getNewGame(pitStones), HttpStatus.OK);
    }

    @PostMapping(value = "sow")
    @ApiOperation(value = "Endpoint used for sowing the game. It also maintains the last state of game for consecutive calls",
            produces = "application/json",
            response = MancalaGame.class, httpMethod = "POST")
    public ResponseEntity<MancalaGame> sow(@Valid @RequestBody SowDto dto) {
        return new ResponseEntity<>(this.gameService.playGame(dto.getGameId(), dto.getPitId()), HttpStatus.OK);
    }

    @GetMapping(value = "last-state/{gameId}")
    @ApiOperation(value = "Endpoint used to return the last state of the game",
            produces = "application/json", response = MancalaGame.class, httpMethod = "GET")
    public ResponseEntity<MancalaGame> getLastState(@PathVariable String gameId) {
        return new ResponseEntity<>(this.gameService.getGame(gameId), HttpStatus.OK);
    }

    @GetMapping("/home")
    @ApiOperation(value = "Endpoint used to show the web page of the game",
            produces = "application/json", response = ModelAndView.class, httpMethod = "GET")
    public ModelAndView getHomepage() {
        return new ModelAndView("home");
    }
}
