package org.app.view;

import org.app.model.PlayerModel;
import org.app.model.TeamModel;
import org.app.persistence.PlayerRepository;
import org.app.persistence.TeamRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class PlayersFrame {
    public static void players(){
        TeamRepository teamRepository = new TeamRepository();
        PlayerRepository playerRepository = new PlayerRepository();

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
            GamesFrame.games();
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
            TeamModel team = teamRepository.getById(player.getTeam());
            Vector<Object> row = new Vector<>();
            row.add(player.getId());
            row.add(player.getName());
            row.add(player.getAge());
            row.add(team.getName());
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
        playersFrame.setVisible(true);
    }
}
