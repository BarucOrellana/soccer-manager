package org.app.view;

import org.app.persistence.UserRepository;

import javax.swing.*;
import java.awt.*;


public class LoginFrame {
    public static void login(){

        JFrame frameLogin = new JFrame("Inicia sesión");
        frameLogin.setContentPane(new BackgroundLogin());
        frameLogin.setSize(800, 600);
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Nombre de usuario");
        gbc.gridx=0;
        gbc.gridy=0;
        frameLogin.add(usernameLabel, gbc);

        JTextField username = new JTextField();
        gbc.gridx=0;
        gbc.gridy=1;
        frameLogin.add(username, gbc);

        JLabel passwordLabel = new JLabel("Contraseña");
        gbc.gridx=0;
        gbc.gridy=2;
        frameLogin.add(passwordLabel, gbc);

        JTextField password = new JTextField();
        gbc.gridx=0;
        gbc.gridy=3;
        frameLogin.add(password, gbc);

        JButton login = new JButton("Iniciar sesión");
        gbc.gridx=0;
        gbc.gridy=4;
        frameLogin.add(login, gbc);

        login.addActionListener(e -> {
            UserRepository userRepository = new UserRepository();
            try{
                if(userRepository.login(username.getText(), password.getText())){
                    GamesFrame.games();
                    frameLogin.setVisible(false);
                }
            }catch (RuntimeException ex){
                JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
            }
        });

        JButton createAccountBtn = new JButton("Crear un nuevo usuario");
        gbc.gridx=0;
        gbc.gridy=8;
        frameLogin.add(createAccountBtn, gbc);
        createAccountBtn.addActionListener(e -> {
            try{
                CreateAccountFrame.createAccount();
                frameLogin.setVisible(false);
            }catch (RuntimeException exception){
                JOptionPane.showMessageDialog(null, "Error al cargar");
            }
        });
        frameLogin.setVisible(true);
    }
}
