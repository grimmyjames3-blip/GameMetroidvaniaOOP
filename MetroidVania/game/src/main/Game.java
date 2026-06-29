package main;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import java.awt.Graphics;
import ui.AudioOptions;

public class Game implements Runnable {
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    private GameOptions gameOptions;  
    private AudioOptions audioOptions; 
    private AudioPlayer audioPlayer;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2.0f;
    public final static int TILES_IN_WIDTH  = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE   = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH   = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT  = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        audioOptions = new AudioOptions(this); 
        audioPlayer  = new AudioPlayer();
        menu         = new Menu(this);
        playing      = new Playing(this);
        gameOptions  = new GameOptions(this); 
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch (Gamestate.state) {
            case MENU    -> menu.update();
            case PLAYING -> playing.update();
            case OPTIONS -> gameOptions.update();
            case QUIT    -> System.exit(0);
        }
    }

    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU    -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            case OPTIONS -> gameOptions.draw(g); 
            case QUIT    -> System.exit(0);
        }
    }

    @Override
    public void run() {
        double timePerFrame  = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long prevTime = System.nanoTime();
        double deltaU = 0, deltaF = 0;

        int frames = 0, updates = 0;
        long lastCheck = System.currentTimeMillis();

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - prevTime) / timePerUpdate;
            deltaF += (currentTime - prevTime) / timePerFrame;
            prevTime = currentTime;

            if (deltaU >= 1) { update();            updates++; deltaU--; }
            if (deltaF >= 1) { gamePanel.repaint(); frames++;  deltaF--; }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0; updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Menu        getMenu()         { return menu; }
    public Playing     getPlaying()      { return playing; }
    public GameOptions getGameOptions()  { return gameOptions; }   
    public AudioOptions getAudioOptions(){ return audioOptions; }  
    public AudioPlayer getAudioPlayer()  { return audioPlayer; }
}