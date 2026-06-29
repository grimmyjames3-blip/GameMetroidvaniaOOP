package entities; 

import java.awt.geom.Rectangle2D;
import main.Game;     
import static utils.Constants.Directions.*;
import static utils.Constants.EnemyConstants.*;             

public class Slime extends Enemy {

	private int attackBoxOffsetX;

	public Slime(float x, float y) {
		super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY);
		initHitbox(x, y, 22, 19);
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE)); 
		attackBoxOffsetX = (int) (Game.SCALE * 30);
	}

	public void update(int[][] lvlData, Player player) {
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX; 
		attackBox.y = hitbox.y;       
	}

	private void updateBehaviour(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir) {
			updateInAir(lvlData);
		} else {
			switch (state) {
				case IDLE:
					newState(RUNNING);
					break;
				case RUNNING:
					if (canSeePlayer(lvlData, player))
						turnTowardsPlayer(player);
					if (isPlayerCloseForAttack(player))
						newState(ATTACK);
					move(lvlData);
					break;
				case ATTACK:
					if (aniIndex == 0)
						attackChecked = false;
					if (aniIndex == 3 && !attackChecked)
						checkPlayerHit(attackBox, player);
					break;
				case HIT:
					break;
			}
		}
	}
	public int flipW() {
		return (walkDir == RIGHT) ? -1 : 1;
	}

	public int flipX() {
		return 0;
	}
}