package org.app;

import org.app.model.GameModel;
import org.app.model.PlayerModel;
import org.app.persistence.GameRepository;
import org.app.persistence.PlayerRepository;

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

            JPanel searchPlayerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

            JButton searchPlayerBtn = new JButton("Buscar jugador");
            gbcPlayers.gridx=0;
            gbcPlayers.gridy=2;

            searchPlayerPanel.add(searchPlayerBtn);

            playersFrame.add(searchPlayerPanel, gbcPlayers);

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

            // Retrieve data based on the date range
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

            searchPlayerBtn.addActionListener(a->{
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
        });

        viewTeamsButton.addActionListener(e -> {
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
            gbcTeams.gridx = 1;
            gbcTeams.gridy = 1;
            gbcTeams.anchor = GridBagConstraints.CENTER;
            teamsFrame.add(backButton, gbcTeams);

            backButton.addActionListener(a->{
                teamsFrame.setVisible(false);
                frameMain.setVisible(true);
            });

            teamsFrame.setVisible(true);
        });

        // Display the frame
        frameMain.setVisible(true);
    }
}

