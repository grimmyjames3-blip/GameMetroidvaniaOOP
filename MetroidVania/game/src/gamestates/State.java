package gamestates;

import audio.AudioPlayer;
import java.awt.event.MouseEvent;
import main.Game;
import ui.MenuButton;
import ui.PauseButton;

public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }

    public void setGamestate(Gamestate state) {
        switch (state) {
            case MENU    -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
            case OPTIONS -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case QUIT    -> System.exit(0);
        }
        Gamestate.state = state;
    }
}