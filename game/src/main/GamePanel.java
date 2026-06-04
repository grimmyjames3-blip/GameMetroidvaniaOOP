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
    private float xDelta = 10, yDelta = 10;
    private long lastTime = System.currentTimeMillis();
    private BufferedImage img, subImg;
    

    public GamePanel() {
        importImg();
        addKeyListener(keyboardInput);
        addMouseListener(mouseInput);
        addMouseMotionListener(mouseInput);
        setPanelSize();
        setFocusable(true);
        requestFocus();
    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/res/player_sprites.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void changeXDelta(int delta) {
        this.xDelta += delta;
    }

    public void changeYDelta(int delta) {
        this.yDelta += delta;
    }

    public void setDeltas(int xDelta, int yDelta) {
        this.xDelta = xDelta;
        this.yDelta = yDelta;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        subImg = img.getSubimage(1*64, 8*40, 64, 40);
        g.drawImage(subImg, (int)xDelta, (int)yDelta,  120, 80, null);
    }
}
