package org.app.view;

import org.app.persistence.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CreateAccountFrame {

    public static void createAccount(){
        JFrame frameLogin = new JFrame("Crear nueva cuenta");
        frameLogin.setContentPane(new BackgroundLogin());
        frameLogin.setSize(800, 600);
        frameLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameLogin.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Nombre de usuario");
        gbc.gridx=1;
        gbc.gridy=0;
        frameLogin.add(usernameLabel, gbc);

        JTextField username = new JTextField();
        gbc.gridx=1;
        gbc.gridy=1;
        frameLogin.add(username, gbc);

        JLabel passwordLabel = new JLabel("Contraseña");
        gbc.gridx=1;
        gbc.gridy=2;
        frameLogin.add(passwordLabel, gbc);

        JTextField password = new JTextField();
        gbc.gridx=1;
        gbc.gridy=3;
        frameLogin.add(password, gbc);

        JButton createAccount = new JButton("Crear usuario");
        gbc.gridx=1;
        gbc.gridy=4;
        frameLogin.add(createAccount, gbc);

        JButton backButton = new JButton("Regresar al inicio");
        gbc.gridx = 1;
        gbc.gridy =5;
        frameLogin.add(backButton, gbc);

        createAccount.addActionListener(e -> {
            try{
                if(username.getText().isEmpty() || password.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Por favor agregar un usuario y una contraseña");
                }else{
                    UserRepository userRepository = new UserRepository();
                    userRepository.createAccount(username.getText(), password.getText());
                    frameLogin.setVisible(false);
                    LoginFrame.login();
                }
            }catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(null, "El usuario ya existe");
            }
        });
        backButton.addActionListener(a -> {
            frameLogin.setVisible(false);
            LoginFrame.login();
        });
        frameLogin.setVisible(true);
    }
}
