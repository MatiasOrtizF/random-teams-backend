package com.random.teams.models;

import lombok.Data;

import java.util.List;

@Data
public class Team {
    private List<Player> team;
    private Double rating;
    private Double winRating;
}
