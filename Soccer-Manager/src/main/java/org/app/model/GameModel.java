package org.app.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GameModel {
    private int id;
    private int visitTeam;
    private int localTeam;
    private LocalDate date;
    private String tournament;
    private int goalsVisitTeam;
    private int goalsLocalTeam;
    private String winner;
}
