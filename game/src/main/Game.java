package main;

import Entities.Player;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int fpsSet = 120;

    private Player player;

    public Game(){
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        initClasses();
        startGameLoop();
    }

    private void initClasses(){
        player = new Player(200,200);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update(){
        player.update();
    }

    public void render(Graphics g){
        player.render(g);
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / fpsSet;
        long lastTime = System.nanoTime();
        long nowTime = System.nanoTime();
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while (true) { 
            nowTime = System.nanoTime();
            if (nowTime - lastTime >= timePerFrame) {
                gamePanel.repaint();
                lastTime = nowTime;
            }
            frames++;

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    public Player getPlayer(){
        return player;
    }
}
