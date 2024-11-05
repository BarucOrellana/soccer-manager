package org.app;

import org.app.view.LoginFrame;

import javax.swing.*;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        try {
            LoginFrame.login();
        }catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la aplicación");
            throw new RuntimeException(e);
        }
    }
}

