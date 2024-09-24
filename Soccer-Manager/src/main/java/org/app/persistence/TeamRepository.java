package org.app.persistence;

import org.app.model.TeamModel;
import org.app.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamRepository implements Repository<TeamModel>{
    private Connection getConnection()throws SQLException {
        return ConnectionManager.connection();
    }
    @Override
    public List<TeamModel> findAll() {
        try(Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM team")){
            List<TeamModel> teams = new ArrayList<>();
            while (resultSet.next()) {
                teams.add(createTeam(resultSet));
            }
            return teams;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TeamModel getById(Integer id) {
        TeamModel team = null;
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM team WHERE id = ?")){
            statement.setInt(1,id);
            try (ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    team = createTeam(resultSet);
                }
            }
            return team;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(TeamModel teamModel) {
        try(PreparedStatement statement = getConnection().prepareStatement("INSERT INTO team(name, goals_conceded, goals_against, players, points) " +
                "VALUES(?,?,?,?,?)")){
            statement.setString(1, teamModel.getName());
            statement.setInt(2, teamModel.getGoalsConceded());
            statement.setInt(3, teamModel.getGoalsAgainst());
            statement.setInt(4, teamModel.getPlayers());
            statement.setInt(5, teamModel.getPoints());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(TeamModel teamModel) {
        try(PreparedStatement statement = getConnection().prepareStatement("UPDATE team SET players = ?, points = ? WHERE id = ?")){
            if (exits(teamModel.getId())){
                statement.setInt(1, teamModel.getPlayers());
                statement.setInt(2, teamModel.getPoints());
                statement.setInt(3, teamModel.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateGoals(int goals_conceded, int goals_against, int id){
        try (PreparedStatement statement = getConnection().prepareStatement("UPDATE team SET goals_conceded = ?, goals_against = ? " +
                "WHERE id = ?")){
            if (exits(id)){
                statement.setInt(1, goals_conceded);
                statement.setInt(2, goals_against);
                statement.setInt(3,id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try(PreparedStatement statement = getConnection().prepareStatement("DELETE FROM team WHERE id = ?")){
            if(exits(id)){
                statement.setInt(1, id);
                statement.executeUpdate();
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean exits(Integer id) {
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT id FROM team WHERE id = ?")){
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

    private TeamModel createTeam(ResultSet resultSet) throws SQLException {
        TeamModel t = new TeamModel();
        t.setId(resultSet.getInt("id"));
        t.setName(resultSet.getString("name"));
        t.setPlayers(resultSet.getInt("players"));
        t.setGoalsConceded(resultSet.getInt("goals_conceded"));
        t.setGoalsAgainst(resultSet.getInt("goals_against"));
        t.setPoints(resultSet.getInt("points"));
        return t;
    }
}
