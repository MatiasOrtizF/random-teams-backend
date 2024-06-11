package com.random.teams.controllers;

import com.random.teams.exceptions.PlayerLimitExceededException;
import com.random.teams.exceptions.ResourceNotFoundException;
import com.random.teams.exceptions.UnauthorizedException;
import com.random.teams.models.Player;
import com.random.teams.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:19006/", "192.168.0.9:8081"})
@RestController
@RequestMapping("api/player")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping
    public ResponseEntity<?> addPlayer(@RequestHeader (value = "Authorization")String token, @RequestBody Player player) {
        try {
            return ResponseEntity.ok(playerService.addPlayer(token, player));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: invalid token");
        } catch (PlayerLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Player limit exceeded. Remove a player to add another one.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPlayers(@RequestHeader (value = "Authorization")String token) {
        try {
            return ResponseEntity.ok(playerService.getAllPlayers(token));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: invalid token");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id, @RequestHeader (value = "Authorization")String token) {
        try {
            return ResponseEntity.ok(playerService.deletePlayer(token, id));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: invalid token");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player does not exist");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> putPlayer(@PathVariable Long id, @RequestHeader (value = "Authorization")String token, @RequestBody Player playerRequest) {
        try {
            return ResponseEntity.ok(playerService.putPlayer(token, id, playerRequest));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: invalid token");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player does not exist");
        }
    }
}
