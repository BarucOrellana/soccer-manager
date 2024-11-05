package org.app.persistence;

import org.app.model.UserModel;
import org.app.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDate;

public class UserRepository {
    private Connection getConnection()throws SQLException {
        return ConnectionManager.connection();
    }

    public void createAccount(String username, String password) {
        try{
            UserModel user = queryUserData(username);
            if(user.getUsername().equals(username)){
                throw new RuntimeException("El usuario ya existe");
            }
        }catch (NullPointerException e){
            LocalDate date = LocalDate.now();
            try(PreparedStatement statement = getConnection().prepareStatement("INSERT INTO user (username, password, granted_date) VALUES(?,?,?)")){
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setDate(3, Date.valueOf(String.valueOf(date)));
                try {
                    statement.executeUpdate();
                    System.out.println("Usuario creado con exito");
                }catch (SQLException sqlException){
                    System.out.println("Error al crear usuario");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public boolean login(String username, String password) {
        try{
            UserModel user = queryUserData(username);
            return user.getPassword().equals(password);
        }catch (RuntimeException e){
            throw new RuntimeException();
        }
    }

    public UserModel queryUserData(String username){
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM user WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    UserModel userModel = new UserModel();
                    userModel.setId(resultSet.getInt("id"));
                    userModel.setUsername(resultSet.getString("username"));
                    userModel.setPassword(resultSet.getString("password"));
                    userModel.setGrantedDate(resultSet.getDate("granted_date"));
                    return userModel;
                }else {
                    System.out.println("El usuario no existe");
                }
            }catch (SQLException e){
                System.out.println("Error al obtener usuario");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new UserModel();
    }

    public boolean exits(Integer id) {
        try(PreparedStatement statement = getConnection().prepareStatement("SELECT id FROM user WHERE id = ?")){
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()) {
                    int idDB = resultSet.getInt("id");
                    return idDB == id;
                } return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
