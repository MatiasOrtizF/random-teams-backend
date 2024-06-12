package com.random.teams.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "name is mandatory")
    @Size(min = 3, max=30, message = "Name must be between 3 and 30 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "resistance is mandatory")
    @Min(value = 1, message = "resistance must be at least 1")
    @Max(value = 10, message = "resistance must be at most 10")
    @Column(name = "resistance", nullable = false)
    private Integer resistance;

    @NotNull(message = "skill is mandatory")
    @Min(value = 1, message = "skill must be at least 1")
    @Max(value = 10, message = "skill must be at most 10")
    @Column(name = "skill", nullable = false)
    private Integer skill;

    @NotNull(message = "defense is mandatory")
    @Min(value = 1, message = "Defense must be at least 1")
    @Max(value = 10, message = "Defense must be at most 10")
    @Column(name = "defense", nullable = false)
    private Integer defense;

    @NotNull(message = "goalkeeper is mandatory")
    @Min(value = 1, message = "goalkeeper must be at least 1")
    @Max(value = 10, message = "goalkeeper must be at most 10")
    @Column(name = "goalkeeper", nullable = false)
    private Integer goalkeeper;

    @NotNull(message = "goal is mandatory")
    @Min(value = 1, message = "goal must be at least 1")
    @Max(value = 10, message = "goal must be at most 10")
    @Column(name = "goal", nullable = false)
    private Integer goal;

    @NotNull(message = "rating is mandatory")
    @Min(value = 1, message = "rating must be at least 1")
    @Max(value = 10, message = "rating must be at most 10")
    @Column(name = "rating", nullable = false)
    private Float rating;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_user")
    private User user;
}
