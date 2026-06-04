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

import static Utilz.Constants.PlayerConstants.*;
import static Utilz.Constants.Directions.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInput = new MouseInputs(this);
    private KeyboardInputs keyboardInput = new KeyboardInputs(this);
    private long lastTime = System.currentTimeMillis(); 
    private Game game;
    
    public GamePanel(Game game) {
        addKeyListener(keyboardInput);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        this.game = game;
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

    // Ini nanti untuk entity
    public Game getGame(){
        return game;
    }
}
