package Entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Playing;
import levels.Level;
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

	public void update(int[][] lvlData, Player player) {
		for (Slime s : slimes)
			if (s.isActive()){
				s.update(lvlData);
				isActive(s);
			}
		if(!isActive(slimes))
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSlimes(g, xLvlOffset);
	}

	private void drawSlimes(Graphics g, int xLvlOffset) {
		for (Slime s : slimes){
			s.drawImage(EnemyArr[s.getEnemyState()][s.getAniIndex()], (int)s.getHitbox().x - xLvlOffset - ENEMY_DRAWOFFSET_X + s.flipX(), (int)s.getHitbox().y, ENEMY_WIDTH * s.flipW(), ENEMY_HEIGHT, null);
		//	s.drawHitBox(g, xLvlOffset);
			s.drawAttackBox(g, xLvlOffset);
		}
	}

	public void checkEnemyHit(Rectangele2D.Float attackBox){
		for(Slime s : slimes){
			if(attackBox.intersects(s.getHitbox())){
				s.hurt(10);
				return;
			}
		}
	}

	private void loadEnemyImgs() {
		EnemyArr = new BufferedImage[5][9];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.ENEMY_SPRITE);
		for (int j = 0; j < EnemyArr.length; j++)
			for (int i = 0; i < EnemyArr[j].length; i++)
				EnemyArr[j][i] = temp.getSubimage(i * ENEMY_WIDTH_DEFAULT, j * ENEMY_HEIGHT_DEFAULT, ENEMY_WIDTH_DEFAULT, ENEMY_HEIGHT_DEFAULT);
	}
}
