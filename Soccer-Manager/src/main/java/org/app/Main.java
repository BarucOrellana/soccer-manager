package org.app;

import org.app.model.GameModel;
import org.app.persistence.GameRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

        String[] columns = {"id", "Fecha", "Equipo visitante", "Equipo local", "Torneo", "Goles visitante", "Goles local", "Ganador"};
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

            java.sql.Date initialDateSQL = new java.sql.Date(initialDate.getTime());
            java.sql.Date finalDateSQL = new java.sql.Date(finalDate.getTime());

            // Clear previous table data
            tableModel.setRowCount(0);

            // Retrieve data based on the date range
            List<GameModel> games = gameRepository.findGamesWithDate(initialDateSQL, finalDateSQL);

            // Populate table with data
            for (GameModel game : games) {
                Vector<Object> row = new Vector<>();
                row.add(game.getId());
                row.add(game.getDate());
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

        // Adding actions for add, edit, and delete buttons (to be implemented)
        addButton.addActionListener(e -> {
            // Logic to add a new game
            JOptionPane.showMessageDialog(frameMain, "Agregar un nuevo juego");
        });

        editButton.addActionListener(e -> {
            // Logic to edit the selected game
            JOptionPane.showMessageDialog(frameMain, "Editar el juego seleccionado");
        });

        deleteButton.addActionListener(e -> {
            // Logic to delete the selected game
            JOptionPane.showMessageDialog(frameMain, "Eliminar el juego seleccionado");
        });

        viewPlayersButton.addActionListener(e -> {
            // Logic to view players
        });

        viewTeamsButton.addActionListener(e -> {
            // Logic to view teams
        });

        // Display the frame
        frameMain.setVisible(true);
    }
}

