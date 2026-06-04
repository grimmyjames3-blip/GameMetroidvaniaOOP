package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private MouseInputs mouseInput = new MouseInputs(this);
    private KeyboardInputs keyboardInput = new KeyboardInputs(this);
    private long lastTime = System.currentTimeMillis(); 
    

    public GamePanel() {
        addKeyListener(keyboardInput);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        setPanelSize();
        setFocusable(true);
        requestFocus();
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.render(g);
    }
}
