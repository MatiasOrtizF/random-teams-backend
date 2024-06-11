package com.random.teams.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PlayerLimitExceededException extends RuntimeException {
    public PlayerLimitExceededException() {
        super("Player limit exceeded. Remove a player to add another one.");
    }

    public PlayerLimitExceededException(String message) {
        super(message);
    }
}
