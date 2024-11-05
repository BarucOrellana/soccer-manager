package org.app.view;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class BackgroundLogin extends JPanel {
    private final Image image;

    public BackgroundLogin() {
        image = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/Diseño_sin_título_(16).png"))).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
        setOpaque(false);
    }
}
