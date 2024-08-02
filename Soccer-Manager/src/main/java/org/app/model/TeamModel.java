package org.app.model;

import lombok.Data;

import java.util.List;

@Data
public class TeamModel {
    private int id;
    private String name;
    private int players;
    private int goalsConceded;
    private int goalsAgainst;
    private int points;
}
