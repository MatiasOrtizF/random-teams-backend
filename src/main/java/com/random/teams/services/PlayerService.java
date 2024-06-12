package com.random.teams.services;

import com.random.teams.exceptions.PlayerLimitExceededException;
import com.random.teams.exceptions.ResourceNotFoundException;
import com.random.teams.exceptions.UnauthorizedException;
import com.random.teams.models.Player;
import com.random.teams.models.User;
import com.random.teams.repositories.PlayerRepository;
import com.random.teams.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, AuthService authService, UserRepository userRepository) {
        this.playerRepository = playerRepository;
        this.authService = authService;
        this.userRepository = userRepository;
    }

    public Player addPlayer(String token, Player player) {
        if(authService.validationToken(token)) {
            Long userId = authService.getUserId(token);
            if(playerRepository.findByUserId(userId).size() < 50) {
                User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("The user with this id:" + userId + "is not found"));

                float rating = calculateRating(player);

                player.setRating(rating);
                player.setUser(user);

                return playerRepository.save(player);
            } throw new PlayerLimitExceededException();
        } throw new UnauthorizedException();
    }

    public List<Player> getAllPlayers(String token) {
        if(authService.validationToken(token)) {
            Long userId = authService.getUserId(token);
            return playerRepository.findByUserId(userId);
        } throw new UnauthorizedException();
    }

    public Map<String, Boolean> deletePlayer(String token, Long id) {
        if (authService.validationToken(token)) {
            Player player = playerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The player with this id:" + id + "is not found"));

            playerRepository.delete(player);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;

        } throw new UnauthorizedException();
    }

    public Player putPlayer(String token, Long id, Player playerRequest) {
        if(authService.validationToken(token)) {
            Player player = playerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The player with this id:" + id + "is not found"));

            player.setName(playerRequest.getName());
            player.setDefense(playerRequest.getDefense());
            player.setGoal(playerRequest.getGoal());
            player.setGoalkeeper(playerRequest.getGoalkeeper());
            player.setSkill(playerRequest.getSkill());
            player.setResistance(playerRequest.getResistance());

            float rating = calculateRating(player);
            player.setRating(rating);

            return playerRepository.save(player);
        } throw new UnauthorizedException();
    }

    private int getQuantityPlayersSave(Long userId) {
        return playerRepository.findByUserId(userId).size();
    }

    private float calculateRating(Player player) {

        int defense =  player.getDefense();
        int goal =  player.getGoal();
        int goalkeeper = player.getGoalkeeper();
        int resistance = player.getResistance();
        int skill = player.getSkill();

        return (float) (defense + goal + goalkeeper + resistance + skill) /5;
    }
}