package com.random.teams.services;

import com.random.teams.exceptions.UnauthorizedException;
import com.random.teams.models.Player;
import com.random.teams.models.Team;
import com.random.teams.models.TeamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BalancerService {

    private final AuthService authService;

    @Autowired
    public BalancerService(AuthService authService) {
        this.authService = authService;
    }

    public TeamResponse generateTeam(String token, List<Player> playerList) {
        if(authService.validationToken(token)) {
            List<Player> equipo1 = new ArrayList<>();
            List<Player> equipo2 = new ArrayList<>();
            //List<Player> team3 = new ArrayList<>();

            //List<Player> playerList = playerListWrapper.getPlayers();
            playerList.sort((p1, p2) -> Float.compare(p2.getRating(), p1.getRating())); // Orden descendente
            for (int i=0; i<playerList.size(); i++) {
                if(i%2 == 0) {
                    equipo1.add(playerList.get(i));
                } else {
                    equipo2.add(playerList.get(i));
                }
            }

            Team team1 = new Team();
            Team team2 = new Team();

            double ratingTeam1 = calculateRatingTeam(equipo1);
            double ratingTeam2 = calculateRatingTeam(equipo2);

            double winRateTeam1 = calculateWinRate(ratingTeam1, ratingTeam2);
            double winRateTeam2 = calculateWinRate(ratingTeam2, ratingTeam1);

            team1.setTeam(equipo1);
            team1.setRating(ratingTeam1);
            team1.setWinRating(winRateTeam1);

            team2.setTeam(equipo2);
            team2.setRating(ratingTeam2);
            team2.setWinRating(winRateTeam2);

            TeamResponse teams = new TeamResponse();

            teams.setTeam1(team1);
            teams.setTeam2(team2);

            return teams;

        } throw new UnauthorizedException();
    }

    private double calculateRatingTeam(List<Player> playerList) {
        double ratingGeneral = 0;

        for(int i=0; i<playerList.size(); i++) {
            ratingGeneral += playerList.get(i).getRating();
        }

        // Redondear hacia arriba y mantener dos decimales
        double rating = ratingGeneral/5;
        return Math.round(rating * 100.0) / 100.0;
    }

    private double calculateWinRate(double ratingTeam1, double ratingTeam2) {
        // Fórmula Elo: P(A) = 1 / (1 + 10^((Rb - Ra) / 400))
        double winRate = (1.0 / (1.0 + Math.pow(10.0, (ratingTeam2 - ratingTeam1) / 400.0))) * 100;

        // Redondear hacia arriba y mantener dos decimales
        return Math.round(winRate * 100.0) / 100.0;
    }
}
