package org.app;

import org.app.model.GameModel;
import org.app.model.PlayerModel;
import org.app.model.TeamModel;
import org.app.persistence.GameRepository;
import org.app.persistence.PlayerRepository;
import org.app.persistence.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        GameRepository gameRepository = new GameRepository();

        // Main page
        JFrame frameMain = new JFrame("Futbol Manager");
        frameMain.setSize(800, 600);
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initial date input
        JLabel labelInitialDate = new JLabel("Fecha de inicio: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frameMain.add(labelInitialDate, gbc);

        SpinnerModel initialDateModel = new SpinnerDateModel();
        JSpinner initialDateSpinner = new JSpinner(initialDateModel);
        JSpinner.DateEditor initialDateEditor = new JSpinner.DateEditor(initialDateSpinner, "yyyy-MM-dd");
        initialDateSpinner.setEditor(initialDateEditor);
        gbc.gridx = 1;
        gbc.gridy = 0;
        frameMain.add(initialDateSpinner, gbc);

        // Final date input
        JLabel labelFinalDate = new JLabel("Fecha final: ");
        gbc.gridx = 0;
        gbc.gridy = 1;
        frameMain.add(labelFinalDate, gbc);

        SpinnerModel finalDateModel = new SpinnerDateModel();
        JSpinner finalDateSpinner = new JSpinner(finalDateModel);
        JSpinner.DateEditor finalDateEditor = new JSpinner.DateEditor(finalDateSpinner, "yyyy-MM-dd");
        finalDateSpinner.setEditor(finalDateEditor);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frameMain.add(finalDateSpinner, gbc);

        // Search button
        JButton buttonSearch = new JButton("Buscar");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frameMain.add(buttonSearch, gbc);

        // Panel for action buttons (Add, Edit, Delete)
        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Left aligned with 10px spacing

        JButton addButton = new JButton("Agregar");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Eliminar");
        JButton viewPlayersButton = new JButton("Ver jugadores");
        JButton viewTeamsButton = new JButton("Ver equipos");

        actionButtonPanel.add(addButton);
        actionButtonPanel.add(editButton);
        actionButtonPanel.add(deleteButton);
        actionButtonPanel.add(viewPlayersButton);
        actionButtonPanel.add(viewTeamsButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frameMain.add(actionButtonPanel, gbc); // Add the panel above the table

        // Creating the table model and table
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable gamesTable = new JTable(tableModel);

        String[] columns = {"id", "Fecha","Hora del juego", "Equipo visitante", "Equipo local", "Torneo", "Goles visitante", "Goles local", "Ganador"};
        tableModel.setColumnIdentifiers(columns);

        // Adding the table inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(gamesTable);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frameMain.add(scrollPane, gbc);

        // Action listener for the search button
        buttonSearch.addActionListener(e -> {
            java.util.Date initialDate = (java.util.Date) initialDateSpinner.getValue();
            java.util.Date finalDate = (java.util.Date) finalDateSpinner.getValue();

            Date initialDateSQL = new Date(initialDate.getTime());
            Date finalDateSQL = new Date(finalDate.getTime());

            // Clear previous table data
            tableModel.setRowCount(0);

            // Retrieve data based on the date range
            List<GameModel> games = gameRepository.findGamesWithDate(initialDateSQL, finalDateSQL);

            // Populate table with data
            for (GameModel game : games) {
                Vector<Object> row = new Vector<>();
                row.add(game.getId());
                row.add(game.getDate());
                row.add(game.getTime());
                row.add(game.getVisitTeam());
                row.add(game.getLocalTeam());
                row.add(game.getTournament());
                row.add(game.getGoalsVisitTeam());
                row.add(game.getGoalsLocalTeam());
                row.add(game.getWinner());
                tableModel.addRow(row);
            }
            if (games.isEmpty()) {
                String alertMessage = "No hay juegos en ese rango de fechas";
                JOptionPane.showMessageDialog(frameMain, alertMessage);
            }
        });

        addButton.addActionListener(e -> {
            JPanel addPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            addPanel.add(new JLabel("Fecha: (YYYY-MM-DD)"));
            JTextField dateInput = new JTextField();
            addPanel.add(dateInput);

            addPanel.add(new JLabel("Horario: (HH:MM:SS)"));
            JTextField timeInput = new JTextField();
            addPanel.add(timeInput);

            addPanel.add(new JLabel("Equipo visitante:"));
            JTextField visitTeamInput = new JTextField();
            addPanel.add(visitTeamInput);

            addPanel.add(new JLabel("Equipo local:"));
            JTextField localTeamInput = new JTextField();
            addPanel.add(localTeamInput);

            addPanel.add(new JLabel("Torneo:"));
            JTextField tournamentInput = new JTextField();
            addPanel.add(tournamentInput);

            int result = JOptionPane.showConfirmDialog(null, addPanel,
                    "Agregar Nuevo Juego", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Date date = Date.valueOf(dateInput.getText());

                    String timeString = timeInput.getText();
                    if (!timeString.matches("\\d{2}:\\d{2}:\\d{2}")) {
                        throw new IllegalArgumentException("El formato de hora debe ser HH:mm:ss");
                    }
                    Time time = Time.valueOf(timeString);

                    int visitTeam = Integer.parseInt(visitTeamInput.getText());
                    int localTeam = Integer.parseInt(localTeamInput.getText());
                    String tournament = tournamentInput.getText();

                    GameModel newGame = new GameModel();
                    newGame.setDate(date);
                    newGame.setTime(time);
                    newGame.setVisitTeam(visitTeam);
                    newGame.setLocalTeam(localTeam);
                    newGame.setTournament(tournament);

                    gameRepository.save(newGame);
                    String text = "Nuevo juego agregado el día: " + date + " a las " + time + " ";
                    JOptionPane.showMessageDialog(null, text);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Error al agregar el juego. Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        editButton.addActionListener(e -> {
            TeamRepository teamRepository = new TeamRepository();
            JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 10));

            updatePanel.add(new JLabel("Id del juego: "));
            JTextField idInput = new JTextField();
            updatePanel.add(idInput);

            updatePanel.add(new JLabel("Goles equipo visitante: "));
            JTextField  goalsVisitInput = new JTextField();
            updatePanel.add(goalsVisitInput);

            updatePanel.add(new JLabel("Goles equipo local: "));
            JTextField goalsLocalInput = new JTextField();
            updatePanel.add(goalsLocalInput);

            updatePanel.add(new JLabel("Ganador: "));
            JTextField winnerInput = new JTextField();
            updatePanel.add(winnerInput);


            int result = JOptionPane.showConfirmDialog(null, updatePanel,
                    "Editar juego", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(idInput.getText());
                    int goalsVisit = Integer.parseInt(goalsVisitInput.getText());
                    int goalsLocal = Integer.parseInt(goalsLocalInput.getText());
                    String winner = winnerInput.getText();

                    //Update point dynamically
                    GameModel game = gameRepository.getById(id);
                    TeamModel localTeam = teamRepository.getById(game.getLocalTeam());
                    TeamModel visitTeam = teamRepository.getById(game.getVisitTeam());

                    if (goalsLocal > goalsVisit){
                        teamRepository.updatePoints(localTeam.getId(), 3);
                    }
                    if(goalsVisit > goalsLocal){
                        teamRepository.updatePoints(visitTeam.getId(), 3);
                    }
                    if (goalsVisit == goalsLocal){
                        teamRepository.updatePoints(localTeam.getId(), 1);
                        teamRepository.updatePoints(visitTeam.getId(), 1);
                    }

                    //Update score dynamically
                    if (goalsVisit > 0 ){
                        GameModel gameModel = gameRepository.getById(id);
                        int idTeam = gameModel.getVisitTeam();
                        teamRepository.updateGoals(goalsLocal, goalsVisit, idTeam);
                    }
                    //Update score dynamically
                    if (goalsLocal > 0 ){
                        GameModel gameModel = gameRepository.getById(id);
                        int idTeam = gameModel.getLocalTeam();
                        teamRepository.updateGoals(goalsVisit, goalsLocal, idTeam);
                    }

                    GameModel newGame = new GameModel();
                    newGame.setId(id);
                    newGame.setGoalsVisitTeam(goalsVisit);
                    newGame.setGoalsLocalTeam(goalsLocal);
                    newGame.setWinner(winner);

                    gameRepository.update(newGame);
                    String text = "Juego " + id + " editado con exito ";
                    JOptionPane.showMessageDialog(null, text);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "Error al editar el juego. Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int idGame = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el id del juego que deseas eliminar: "));
            boolean deleteStatus = gameRepository.delete(idGame);
            if (deleteStatus){
                JOptionPane.showMessageDialog(null, "Juego eliminado con exito");
            }else {
                JOptionPane.showMessageDialog(null, "Error al eliminar juego, verifica el id ingresado");
            }
        });

        viewPlayersButton.addActionListener(e -> {
            PlayerRepository playerRepository = new PlayerRepository();
            frameMain.setVisible(false);

            JFrame playersFrame = new JFrame("Jugadores");
            playersFrame.setSize(800, 600);
            playersFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            playersFrame.setLayout(new GridBagLayout());
            GridBagConstraints gbcPlayers = new GridBagConstraints();
            gbcPlayers.insets = new Insets(10, 10, 10, 10);
            gbcPlayers.fill = GridBagConstraints.HORIZONTAL;

            // Back button
            JButton backButton = new JButton("Regresar a la pagina principal");
            gbcPlayers.gridx = 0;
            gbcPlayers.gridy = 1;
            gbcPlayers.gridwidth=10;
            playersFrame.add(backButton, gbcPlayers);

            backButton.addActionListener(a -> {
                playersFrame.setVisible(false);
                frameMain.setVisible(true);
            });

            JPanel actionPlayerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

            JButton searchPlayerButton = new JButton("Buscar jugador");
            JButton addPlayerButton = new JButton("Agregar");
            JButton editPlayerButton = new JButton("Editar");
            JButton deletePlayerButton = new JButton("Eliminar");

            actionPlayerPanel.add(addPlayerButton);
            actionPlayerPanel.add(editPlayerButton);
            actionPlayerPanel.add(deletePlayerButton);
            actionPlayerPanel.add(searchPlayerButton);

            gbcPlayers.gridx = 0;
            gbcPlayers.gridy = 2;
            gbcPlayers.gridwidth = 2;

            playersFrame.add(actionPlayerPanel, gbcPlayers);

            DefaultTableModel tablePlayersModel = new DefaultTableModel();
            JTable playersTable = new JTable(tablePlayersModel);

            String[] columnsPlayers = {"id", "nombre", "edad", "equipo", "goles"};
            tablePlayersModel.setColumnIdentifiers(columnsPlayers);

            JScrollPane scrollPlayers = new JScrollPane(playersTable);
            gbcPlayers.gridx = 0;
            gbcPlayers.gridy = 3;
            gbcPlayers.gridwidth = 2;
            gbcPlayers.fill = GridBagConstraints.BOTH;
            gbcPlayers.weightx = 1.0;
            gbcPlayers.weighty = 1.0;
            playersFrame.add(scrollPlayers, gbcPlayers);

            playersFrame.setVisible(true);

            tablePlayersModel.setRowCount(0);

            List<PlayerModel> players = playerRepository.findAll();

            // Populate table with data
            for (PlayerModel player : players) {
                Vector<Object> row = new Vector<>();
                row.add(player.getId());
                row.add(player.getName());
                row.add(player.getAge());
                row.add(player.getTeam());
                row.add(player.getScore());
                tablePlayersModel.addRow(row);
            }

            searchPlayerButton.addActionListener(a->{
                int idPlayer = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el id del jugador: "));
                if(playerRepository.exits(idPlayer)){
                    PlayerModel player = playerRepository.getById(idPlayer);
                    String text = "Nombre: "+ player.getName() + ", Edad: " + player.getAge() + ", Equipo: " + player.getTeam() +
                    ", Goles: " +player.getScore();
                    JOptionPane.showMessageDialog(null, text);
                }else {
                    JOptionPane.showMessageDialog(null, "Ingresa un id valido");
                }
            });

            addPlayerButton.addActionListener(a -> {
                TeamRepository teamRepository = new TeamRepository();
                JPanel addPanel = new JPanel(new GridLayout(0, 2, 10, 10));

                addPanel.add(new JLabel("Nombre: "));
                JTextField nameInput = new JTextField();
                addPanel.add(nameInput);

                addPanel.add(new JLabel("Edad: "));
                JTextField ageInput = new JTextField();
                addPanel.add(ageInput);

                addPanel.add(new JLabel("Equipo: "));
                JTextField teamInput = new JTextField();
                addPanel.add(teamInput);

                addPanel.add(new JLabel("Total de goles:"));
                JTextField scoreInput = new JTextField();
                addPanel.add(scoreInput);

                int result = JOptionPane.showConfirmDialog(null, addPanel,
                        "Agregar Nuevo Jugador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String name = nameInput.getText();
                        int age = Integer.parseInt(ageInput.getText());
                        int team = Integer.parseInt(teamInput.getText());
                        int score = Integer.parseInt(scoreInput.getText());

                        //Update players dynamically
                        teamRepository.updatePlayers(team);

                        PlayerModel newPlayer = new PlayerModel();
                        newPlayer.setName(name);
                        newPlayer.setAge(age);
                        newPlayer.setTeam(team);
                        newPlayer.setScore(score);

                        playerRepository.save(newPlayer);
                        String text = "Nuevo jugador agregado, Nombre: " + name + " Edad: " + age + " Equipo: " + team;
                        JOptionPane.showMessageDialog(null, text);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Error al agregar el jugador. Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            editPlayerButton.addActionListener(a -> {
                JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 10));

                updatePanel.add(new JLabel("Id del jugador: "));
                JTextField idInput = new JTextField();
                updatePanel.add(idInput);

                updatePanel.add(new JLabel("Nombre del jugador: "));
                JTextField  nameInput = new JTextField();
                updatePanel.add(nameInput);

                updatePanel.add(new JLabel("Edad del jugador: "));
                JTextField ageInput = new JTextField();
                updatePanel.add(ageInput);

                updatePanel.add(new JLabel("Equipo: "));
                JTextField teamInput = new JTextField();
                updatePanel.add(teamInput);

                updatePanel.add(new JLabel("Goles: "));
                JTextField scoreInput = new JTextField();
                updatePanel.add(scoreInput);

                int result = JOptionPane.showConfirmDialog(null, updatePanel,
                        "Editar jugador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int id = Integer.parseInt(idInput.getText());
                        String name = nameInput.getText();
                        int age = Integer.parseInt(ageInput.getText());
                        int team = Integer.parseInt(teamInput.getText());
                        int score = Integer.parseInt(scoreInput.getText());

                        PlayerModel newPlayer = new PlayerModel();
                        newPlayer.setId(id);
                        newPlayer.setName(name);
                        newPlayer.setAge(age);
                        newPlayer.setTeam(team);
                        newPlayer.setScore(score);

                        playerRepository.update(newPlayer);
                        String text = "Jugador " + id + " editado con exito ";
                        JOptionPane.showMessageDialog(null, text);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Error al editar el jugador. Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            deletePlayerButton.addActionListener(a -> {
                int idPlayer = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el id del jugador que deseas eliminar: "));
                boolean deleteStatus = playerRepository.delete(idPlayer);
                if (deleteStatus){
                    JOptionPane.showMessageDialog(null, "Jugador eliminado con exito");
                }else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar jugador, verifica el id ingresado");
                }
            });
        });

        viewTeamsButton.addActionListener(e -> {
            TeamRepository teamRepository = new TeamRepository();
            frameMain.setVisible(false);

            JFrame teamsFrame = new JFrame("Equipos");

            teamsFrame.setSize(800, 600);
            teamsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            teamsFrame.setLayout(new GridBagLayout());
            GridBagConstraints gbcTeams = new GridBagConstraints();
            gbcTeams.insets = new Insets(10, 10, 10, 10);
            gbcTeams.fill = GridBagConstraints.HORIZONTAL;

            // Back button
            JButton backButton = new JButton("Regresar a la pagina principal");
            gbcTeams.gridx = 0;
            gbcTeams.gridy = 1;
            gbcTeams.gridwidth=10;
            teamsFrame.add(backButton, gbcTeams);

            backButton.addActionListener(a->{
                teamsFrame.setVisible(false);
                frameMain.setVisible(true);
            });

            JPanel actionTeamPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

            JButton searchTeamButton = new JButton("Buscar equipo");
            JButton addTeamButton = new JButton("Agregar");
            JButton updateTeamButton = new JButton("Editar");
            JButton deleteTeamButton = new JButton("Eliminar");

            actionTeamPanel.add(addTeamButton);
            actionTeamPanel.add(updateTeamButton);
            actionTeamPanel.add(deleteTeamButton);
            actionTeamPanel.add(searchTeamButton);

            gbcTeams.gridx = 0;
            gbcTeams.gridy = 2;
            gbcTeams.gridwidth = 2;

            teamsFrame.add(actionTeamPanel, gbcTeams);

            DefaultTableModel tableTeamsModel = new DefaultTableModel();
            JTable teamsTable = new JTable(tableTeamsModel);

            String[] columnsTeams = {"id", "nombre", "jugadores", "Goles en contra", "Goles a favor", "Puntos"};
            tableTeamsModel.setColumnIdentifiers(columnsTeams);

            JScrollPane scrollTeams = new JScrollPane(teamsTable);
            gbcTeams.gridx = 0;
            gbcTeams.gridy = 3;
            gbcTeams.gridwidth = 2;
            gbcTeams.fill = GridBagConstraints.BOTH;
            gbcTeams.weightx = 1.0;
            gbcTeams.weighty = 1.0;
            teamsFrame.add(scrollTeams, gbcTeams);

            tableTeamsModel.setRowCount(0);

            List<TeamModel> teams = teamRepository.findAll();

            // Populate table with data
            for (TeamModel team : teams) {
                Vector<Object> row = new Vector<>();
                row.add(team.getId());
                row.add(team.getName());
                row.add(team.getPlayers());
                row.add(team.getGoalsConceded());
                row.add(team.getGoalsAgainst());
                row.add(team.getPoints());
                tableTeamsModel.addRow(row);
            }

            searchTeamButton.addActionListener(a->{
                int idTeam = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el id del equipo: "));
                if(teamRepository.exits(idTeam)){
                    TeamModel team = teamRepository.getById(idTeam);
                    String text = "Nombre: "+ team.getName() + ", Jugadores: " + team.getPlayers() + ", Goles en contra: " + team.getGoalsConceded() +
                            ", Goles a favor: " +team.getGoalsAgainst() + ", Puntos: " + team.getPoints();
                    JOptionPane.showMessageDialog(null, text);
                }else {
                    JOptionPane.showMessageDialog(null, "Ingresa un id valido");
                }
            });

            addTeamButton.addActionListener(a->{
                JPanel addPanel = new JPanel(new GridLayout(0, 2, 10, 10));

                addPanel.add(new JLabel("Nombre: "));
                JTextField nameInput = new JTextField();
                addPanel.add(nameInput);

                addPanel.add(new JLabel("Jugadores: "));
                JTextField playersInput = new JTextField();
                addPanel.add(playersInput);

                addPanel.add(new JLabel("Goles en contra: "));
                JTextField goalsConcededInput = new JTextField();
                addPanel.add(goalsConcededInput);

                addPanel.add(new JLabel("Goles a favor:"));
                JTextField goalsAgainstInput = new JTextField();
                addPanel.add(goalsAgainstInput);

                addPanel.add(new JLabel("Puntos:"));
                JTextField pointsInput = new JTextField();
                addPanel.add(pointsInput);

                int result = JOptionPane.showConfirmDialog(null, addPanel,
                        "Agregar Nuevo Equipo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        String name = nameInput.getText();
                        int players = Integer.parseInt(playersInput.getText());
                        int goalsConceded = Integer.parseInt(goalsConcededInput.getText());
                        int goalsAgainst = Integer.parseInt(goalsAgainstInput.getText());
                        int points = Integer.parseInt(pointsInput.getText());

                        TeamModel newTeam = new TeamModel();
                        newTeam.setName(name);
                        newTeam.setPlayers(players);
                        newTeam.setGoalsConceded(goalsConceded);
                        newTeam.setGoalsAgainst(goalsAgainst);
                        newTeam.setPoints(points);

                        teamRepository.save(newTeam);
                        String text = "Nuevo equipo agregado, Nombre: " + name;
                        JOptionPane.showMessageDialog(null, text);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Error al agregar el equipo. Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            updateTeamButton.addActionListener(a->{
                JPanel updatePanel = new JPanel(new GridLayout(0, 2, 10, 10));

                updatePanel.add(new JLabel("Id del equipo: "));
                JTextField idInput = new JTextField();
                updatePanel.add(idInput);

                updatePanel.add(new JLabel("Jugadores: "));
                JTextField  playersInput = new JTextField();
                updatePanel.add(playersInput);

                int result = JOptionPane.showConfirmDialog(null, updatePanel,
                        "Editar equipo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int id = Integer.parseInt(idInput.getText());
                        int players = Integer.parseInt(playersInput.getText());

                        TeamModel newTeam = new TeamModel();
                        newTeam.setId(id);
                        newTeam.setPlayers(players);

                        teamRepository.update(newTeam);
                        String text = "Equipo " + id + " editado con exito ";
                        JOptionPane.showMessageDialog(null, text);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Error al editar el equipo. Verifica los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            deleteTeamButton.addActionListener(a->{
                int idTeam = Integer.parseInt(JOptionPane.showInputDialog("Ingresa el id del equipo que deseas eliminar: "));
                boolean deleteStatus = teamRepository.delete(idTeam);
                if (deleteStatus){
                    JOptionPane.showMessageDialog(null, "Equipo eliminado con exito");
                }else {
                    JOptionPane.showMessageDialog(null, "Error al eliminar equipo, verifica el id ingresado");
                }
            });

            teamsFrame.setVisible(true);
        });

        // Display the frame
        frameMain.setVisible(true);
    }
}

