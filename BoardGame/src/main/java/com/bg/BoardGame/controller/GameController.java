package com.bg.BoardGame.controller;

import com.bg.BoardGame.common.ApiResponse;
import com.bg.BoardGame.dto.GameDto;
import com.bg.BoardGame.model.Category;
import com.bg.BoardGame.model.Game;
import com.bg.BoardGame.repository.CategoryRepository;
import com.bg.BoardGame.repository.GameRepository;
import com.bg.BoardGame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    GameRepository gameRepository;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createGame(@RequestBody GameDto gameDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(gameDto.getCategoryId());
        if(!optionalCategory.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false,"category does not exists"), HttpStatus.BAD_REQUEST);
        }
        gameService.createGame(gameDto,optionalCategory.get());
        return new ResponseEntity<>(new ApiResponse(true,"game has been added"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<GameDto>> getGames(){
        List<GameDto> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @PostMapping("/update/{gameId}")
    public ResponseEntity<ApiResponse> updateGame(@PathVariable("gameId") Integer gameId, @RequestBody GameDto gameDto) throws Exception {
        Optional<Category> optionalCategory = categoryRepository.findById(gameDto.getCategoryId());
        if(!optionalCategory.isPresent()){
            return new ResponseEntity<ApiResponse>( new ApiResponse(false,"category does not exists."), HttpStatus.BAD_REQUEST);
        }
        gameService.updateGame(gameDto, gameId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true, "game has been updated."), HttpStatus.OK);
    }

    @DeleteMapping("/remove/{gameId}")
    public ResponseEntity<ApiResponse> removeGame(@PathVariable("gameId") Integer gameId){
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if(!optionalGame.isPresent()){
            return new ResponseEntity<ApiResponse>(new ApiResponse(false,"game does not exist."), HttpStatus.BAD_REQUEST);
        }
        gameService.removeGame(gameId);
        return new ResponseEntity<ApiResponse>(new ApiResponse(true,"game has been removed."), HttpStatus.OK);
    }
}
