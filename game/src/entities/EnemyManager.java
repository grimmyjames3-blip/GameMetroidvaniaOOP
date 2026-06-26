package Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*; 

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] EnemyArr;
	private ArrayList<Slime> slimes = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}

	private void addEnemies() {
		slimes = LoadSave.GetSlimes();
		System.out.println("size of slimes: " + slimes.size());
	}

	public void update(int[][] lvlData) {
		for (Slime s : slimes)
			s.update(lvlData);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSlimes(g, xLvlOffset);
	}

	private void drawSlimes(Graphics g, int xLvlOffset) {
		for (Slime s : slimes)
			s.drawImage(EnemyArr[s.getEnemyState()][s.getAniIndex()], (int)c.getHitbox().x, (int)c.getHitbox().y, ENEMY_WIDTH, ENEMY_HEIGHT, null);
	}

	private void loadEnemyImgs() {
		EnemyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_SPRITE);
		for (int j = 0; j < EnemyArr.length; j++)
			for (int i = 0; i < EnemyArr[j].length; i++)
				EnemyArr[j][i] = temp.getSubimage(i * ENEMY_WIDTH_DEFAULT, j * ENEMY_HEIGHT_DEFAULT, ENEMY_WIDTH_DEFAULT, ENEMY_HEIGHT_DEFAULT);
	}
}
