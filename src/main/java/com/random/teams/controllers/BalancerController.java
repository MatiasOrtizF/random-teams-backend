package com.random.teams.controllers;

import com.random.teams.exceptions.UnauthorizedException;
import com.random.teams.models.Player;
import com.random.teams.services.BalancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:19006/", "192.168.0.9:8081"})
@RestController
@RequestMapping("api/balancer")
public class BalancerController {

    private final BalancerService  balancerService;

    @Autowired
    public BalancerController(BalancerService balancerService) {
        this.balancerService = balancerService;
    }

    @GetMapping
    public ResponseEntity<?> generateTeam(@RequestHeader (value = "Authorization")String token,@RequestBody List<Player> playerList) {
        try {
            return ResponseEntity.ok(balancerService.generateTeam(token, playerList));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: invalid token");
        }
    }
}
