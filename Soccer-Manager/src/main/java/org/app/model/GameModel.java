package org.app.model;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GameModel {
    private int id;
    private int visitTeam;
    private int localTeam;
    private Date date;
    private Time time;
    private String tournament;
    private int goalsVisitTeam;
    private int goalsLocalTeam;
    private String winner;
}
