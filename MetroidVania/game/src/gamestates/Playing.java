package gamestates;

import entities.EnemyManager;
import entities.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import levels.LevelManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import static utils.Constants.Environment.*;
import utils.LoadSave;

public class Playing extends State implements Statemethods {

	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.35 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.65 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private int yLvlOffset;
	private int topBorder = (int) (0.35 * Game.GAME_HEIGHT);
	private int bottomBorder = (int) (0.65 * Game.GAME_HEIGHT);
	private int maxLvlOffsetY;

	private int lvlTilesWide;
	private int maxTilesOffset;
	
	private BufferedImage backgroundImg, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted = false;
	private boolean playerDying;

	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_1);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (40 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));
		calcLvlOffsets();
		loadStartLevel();
	}

	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffsets() {
    lvlTilesWide = levelManager.getCurrentLevel().getLevelData()[0].length;
    maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();

    int lvlTilesTall = levelManager.getCurrentLevel().getLevelData().length;
    maxLvlOffsetY = Math.max(0, (lvlTilesTall - Game.TILES_IN_HEIGHT) * Game.TILES_SIZE);
}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);
		player = new Player(250, 500, (int) (69 * Game.SCALE), (int) (44 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
	}

	
	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelCompletedOverlay.update();
		} else if(gameOver) {
			gameOverOverlay.update();
		}else if(playerDying) {
			player.update();
		}else if (!gameOver) {
			levelManager.update();

			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			checkCloseToBorder();
		}
	}

	private void checkCloseToBorder() {
    	// X
		int playerX = (int) player.getHitbox().x;
		int diffX = playerX - xLvlOffset;
		if (diffX > rightBorder)
			xLvlOffset += diffX - rightBorder;
		else if (diffX < leftBorder)
			xLvlOffset += diffX - leftBorder;

		if (xLvlOffset > maxLvlOffsetX) xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)        xLvlOffset = 0;

		// Y
		int playerY = (int) player.getHitbox().y;
		int diffY = playerY - yLvlOffset;
		if (diffY > bottomBorder)
			yLvlOffset += diffY - bottomBorder;
		else if (diffY < topBorder)
			yLvlOffset += diffY - topBorder;

		if (yLvlOffset > maxLvlOffsetY) yLvlOffset = maxLvlOffsetY;
		else if (yLvlOffset < 0)        yLvlOffset = 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		drawClouds(g);
		levelManager.draw(g, xLvlOffset, yLvlOffset);
		player.render(g, xLvlOffset, yLvlOffset);
		enemyManager.draw(g, xLvlOffset, yLvlOffset);
		objectManager.draw(g, xLvlOffset, yLvlOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (lvlCompleted)
			levelCompletedOverlay.draw(g);
	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	@Override
		public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
			else if(e.getButton() == MouseEvent.BUTTON3)
				player.powerAttack();
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameOverOverlay.keyPressed(e);
		} else {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mousePressed(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mousePressed(e);
		}else{
			gameOverOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseReleased(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mouseReleased(e);
		}else{
			gameOverOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused)
				pauseOverlay.mouseMoved(e);
			else if (lvlCompleted)
				levelCompletedOverlay.mouseMoved(e);
		}else{
			gameOverOverlay.mouseMoved(e);
		}
	}

	public void setLevelCompleted(boolean levelCompleted) {
		this.lvlCompleted = levelCompleted;
		if(levelCompleted)
			game.getAudioPlayer().lvlCompleted();
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}
}