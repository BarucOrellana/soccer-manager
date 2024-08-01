package org.app;

import org.app.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try(Connection connection = ConnectionManager.connection();){
            System.out.println("Conexion exitosa");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}