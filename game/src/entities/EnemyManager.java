package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;
import static Utilz.Constants.EnemyConstants.*; 

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] EnemyArr;
	private ArrayList<Enemy> enemies = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}

	private void addEnemies() {
		enemies = LoadSave.GetEnemies();
		System.out.println("size of enemies: " + enemies.size());
	}

	public void update() {
		for (Enemy e : enemies)
			e.update();
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawEnemies(g, xLvlOffset);
	}

	private void drawEnemies(Graphics g, int xLvlOffset) {
		for (Enemy e : enemies)
			e.draw(g, xLvlOffset);
	}

	private void loadEnemyImgs() {
		EnemyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_SPRITE);
		for (int j = 0; j < EnemyArr.length; j++)
			for (int i = 0; i < EnemyArr[j].length; i++)
				EnemyArr[j][i] = temp.getSubimage(i * Enemy_Width_Default, j * Enemy_Height_Default, Enemy_Width_Default, Enemy_Height_Default);
	}
}