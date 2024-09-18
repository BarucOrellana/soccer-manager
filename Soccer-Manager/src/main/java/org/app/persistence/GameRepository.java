package org.app.persistence;

import org.app.model.GameModel;
import org.app.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameRepository implements Repository<GameModel>{
    private Connection getConnection()throws SQLException {
        return ConnectionManager.connection();
    }
    @Override
    public List<GameModel> findAll() {
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM game")){
            List<GameModel> games = new ArrayList<>();
            while (resultSet.next()) {
                games.add(createGame(resultSet));
            }
            return games;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameModel getById(Integer id) {
        GameModel game = null;
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM game WHERE id = ?")){
            statement.setInt(1,id);
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    game = createGame(resultSet);
                }
            }
            return game;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(GameModel gameModel) {
        try(PreparedStatement statement = getConnection().prepareStatement("INSERT INTO game(date, visit_team_id, " +
                "local_team_id, tournament, " +
                "goals_visit_team, goals_local_team, winner) " +
                "VALUES(?,?,?,?,?,?,?)")){
            statement.setDate(1,gameModel.getDate());
            statement.setInt(2,gameModel.getVisitTeam());
            statement.setInt(3, gameModel.getLocalTeam());
            statement.setString(4,gameModel.getTournament());
            statement.setInt(5,gameModel.getGoalsVisitTeam());
            statement.setInt(6, gameModel.getGoalsLocalTeam());
            statement.setString(7, gameModel.getWinner());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameModel gameModel) {
        try(PreparedStatement statement = getConnection().prepareStatement("UPDATE game SET date = ?, " +
                "tournament = ?, goals_visit_team = ?, goals_local_team = ?, winner = ? WHERE id = ?")){
            if (exits(gameModel.getId())){
                statement.setDate(1, gameModel.getDate());
                statement.setString(2, gameModel.getTournament());
                statement.setInt(3,gameModel.getGoalsVisitTeam());
                statement.setInt(4, gameModel.getGoalsLocalTeam());
                statement.setString(5, gameModel.getWinner());
                statement.setInt(6,gameModel.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try(PreparedStatement statement = getConnection().prepareStatement("DELETE * FROM game WHERE id = ?")){
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exits(Integer id) {
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT id FROM game WHERE id = ?")){
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

    public List<GameModel> findGamesWithDate(Date initialDate, Date finalDate){
        List<GameModel> games = new ArrayList<>();
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM game WHERE date BETWEEN ? AND ? ")){
            statement.setDate(1, initialDate);
            statement.setDate(2, finalDate);
            try (ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    games.add(createGame(resultSet));
                }
            }
            return games;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private GameModel createGame(ResultSet resultSet) throws SQLException {
        GameModel g = new GameModel();
        g.setId(resultSet.getInt("id"));
        g.setDate(resultSet.getDate("date"));
        g.setTime(resultSet.getTime("time"));
        g.setVisitTeam(resultSet.getInt("visit_team_id"));
        g.setLocalTeam(resultSet.getInt("local_team_id"));
        g.setTournament(resultSet.getString("tournament"));
        g.setGoalsVisitTeam(resultSet.getInt("goals_visit_team"));
        g.setGoalsLocalTeam(resultSet.getInt("goals_local_team"));
        g.setWinner(resultSet.getString("winner"));
        return g;
    }
}
