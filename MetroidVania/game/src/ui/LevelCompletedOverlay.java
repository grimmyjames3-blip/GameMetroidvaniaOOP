package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import static utils.Constants.UI.URMButtons.*;
import utils.LoadSave; 

// FIX: class renamed to LevelCompletedOverlay (was levelCompletedOverlay — Java requires PascalCase for class names)
// FIX: file must also be renamed to LevelCompletedOverlay.java
public class LevelCompletedOverlay {

	private Playing playing;
	private UrmButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;

	public LevelCompletedOverlay(Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
		bgW = (int) (img.getWidth() * Game.SCALE);
		bgH = (int) (img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (75 * Game.SCALE);
	}

	private void initButtons() {
		int menuX = (int) (330 * Game.SCALE);
		int nextX = (int) (445 * Game.SCALE);
		int y     = (int) (195 * Game.SCALE);
		next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
	}

	public void draw(Graphics g) {
		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		next.draw(g);
		menu.draw(g);
	}

	public void update() {
		menu.update();
		next.update();
	}

	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);
		if (isIn(next, e))
			next.setMouseOver(true);
		else if (isIn(menu, e))
			menu.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(menu, e)) {
			if (menu.isMousePressed()){
				playing.resetAll();
				playing.setGamestate(Gamestate.MENU);
			}
		} else if (isIn(next, e)) {
			if (next.isMousePressed()){
				playing.loadNextLevel();
				playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
			}
		}
		next.resetBools();
		menu.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(next, e))
			next.setMousePressed(true);
		else if (isIn(menu, e))
			menu.setMousePressed(true);
	}
}