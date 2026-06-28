package entities; // FIX: was "Entities" (wrong case)

import gamestates.Playing;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import levels.Level;
import static utils.Constants.EnemyConstants.*;                          // FIX: was "utilz"
import utils.LoadSave; // FIX: was "utilz"

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] enemyArr;
	private ArrayList<Slime> slimes = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	// FIX: now called with a Level so enemies come from the level PNG, not a static file
	public void loadEnemies(Level level) {
		slimes = level.getSlimes();
		System.out.println("Slimes loaded: " + slimes.size());
	}

	public void update(int[][] lvlData, Player player) {
		for (Slime s : slimes)
			if (s.isActive())
				s.update(lvlData, player);

		// FIX: isActive(s) and isActive(slimes) don't exist — replaced with proper stream check
		boolean anyActive = slimes.stream().anyMatch(Slime::isActive);
		if (!anyActive && !slimes.isEmpty())
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawSlimes(g, xLvlOffset);
	}

	private void drawSlimes(Graphics g, int xLvlOffset) {
		for (Slime s : slimes) {
			if (s.isActive()) {
				// FIX: was s.drawImage() — Slime has no such method; use g.drawImage() directly
				g.drawImage(
					enemyArr[s.getEnemyState()][s.getAniIndex()],
					(int) s.getHitbox().x - xLvlOffset - ENEMY_DRAWOFFSET_X + s.flipX(),
					(int) s.getHitbox().y - ENEMY_DRAWOFFSET_Y,
					ENEMY_WIDTH * s.flipW(),
					ENEMY_HEIGHT,
					null
				);
			}
		}
	}

	// FIX: was "Rectangele2D" (typo)
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Slime s : slimes) {
			if (s.isActive()) {
				if (attackBox.intersects(s.getHitbox())) {
					s.hurt(10);
					return;
				}
			}
		}
	}

	private void loadEnemyImgs() {
		enemyArr = new BufferedImage[5][9];
		// FIX: was LoadSave.ENEMY_SPRITE — now uses SLIME_SPRITE (defined in LoadSave)
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SLIME_SPRITE);
		for (int j = 0; j < enemyArr.length; j++)
			for (int i = 0; i < enemyArr[j].length; i++)
				enemyArr[j][i] = temp.getSubimage(
					i * ENEMY_WIDTH_Default,
					j * ENEMY_HEIGHT_Default,
					ENEMY_WIDTH_Default,
					ENEMY_HEIGHT_Default
				);
	}

	public void resetAllEnemies() {
		for (Slime s : slimes)
			s.resetEnemy();
	}
}