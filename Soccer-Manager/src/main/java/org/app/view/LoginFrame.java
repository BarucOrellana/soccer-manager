package org.app.view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame {
    public static void login(){
        JFrame frameLogin = new JFrame("Inicia sesión");
        frameLogin.setSize(800, 600);
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

    }
}
