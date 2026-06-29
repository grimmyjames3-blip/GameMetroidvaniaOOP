package inputs;

import gamestates.Gamestate;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU    -> gamePanel.getGame().getMenu().keyPressed(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().keyPressed(e); // ← baru
            case QUIT -> System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU    -> gamePanel.getGame().getMenu().keyReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().keyReleased(e);
            case QUIT -> System.exit(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}