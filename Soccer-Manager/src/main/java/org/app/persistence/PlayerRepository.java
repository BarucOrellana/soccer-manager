package org.app.persistence;

import org.app.model.PlayerModel;
import org.app.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository implements Repository<PlayerModel>{
    private Connection getConnection()throws SQLException {
        return ConnectionManager.connection();
    }
    @Override
    public List<PlayerModel> findAll() {
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM player")){
            List<PlayerModel> players= new ArrayList<>();
            while (resultSet.next()) {
                players.add(createPlayer(resultSet));
            }
            return players;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel getById(Integer id) {
        PlayerModel player = null;
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM player WHERE id = ?")){
            statement.setInt(1,id);
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    player = createPlayer(resultSet);
                }
            }
            return player;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(PlayerModel playerModel) {
        try(PreparedStatement statement = getConnection().prepareStatement("INSERT INTO player(name, age, team_id, score) " +
                "VALUES(?,?,?,?)")){
            statement.setString(1, playerModel.getName());
            statement.setInt(2,playerModel.getAge());
            statement.setInt(3, playerModel.getTeam());
            statement.setInt(4,playerModel.getScore());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel playerModel) {
        try(PreparedStatement statement = getConnection().prepareStatement("UPDATE player SET name = ?, age = ?, "+
                "team_id = ?, score = ? WHERE id = ?")){
            if (exits(playerModel.getId())){
                statement.setString(1, playerModel.getName());
                statement.setInt(2, playerModel.getAge());
                statement.setInt(3, playerModel.getTeam());
                statement.setInt(4, playerModel.getScore());
                statement.setInt(5, playerModel.getId());
                statement.executeUpdate();
            }else {
                System.out.println("Error al editar jugador");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try(PreparedStatement statement = getConnection().prepareStatement("DELETE * FROM player WHERE id = ?")){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exits(Integer id) {
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT id FROM player WHERE id = ?")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()) {
                    int idDB = resultSet.getInt("id");
                    if (idDB == id) {
                        return true;
                    }
                    return false;
                } return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PlayerModel createPlayer(ResultSet resultSet) throws SQLException {
        PlayerModel p = new PlayerModel();
        p.setId(resultSet.getInt("id"));
        p.setName(resultSet.getString("name"));
        p.setAge(resultSet.getInt("age"));
        p.setTeam(resultSet.getInt("team_id"));
        p.setScore(resultSet.getInt("score"));
        return p;
    }
}
