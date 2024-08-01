package org.app.model;

import lombok.Data;

@Data
public class GameModel {
    private int id;
    private int visitTeam;
    private int localTeam;
    private String tournament;
    private int goalsVisitTeam;
    private int goalsLocalTeam;
    private String winner;
}
