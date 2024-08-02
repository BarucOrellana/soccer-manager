package org.app.model;

import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class GameModel {
    private int id;
    private int visitTeam;
    private int localTeam;
    private Date date;
    private String tournament;
    private int goalsVisitTeam;
    private int goalsLocalTeam;
    private String winner;
}
