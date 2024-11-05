package org.app.view;

import org.app.model.TeamModel;
import org.app.persistence.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class TeamsFrame {
    public static void teams(){
        TeamRepository teamRepository = new TeamRepository();

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
            GamesFrame.games();
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
    }
}
