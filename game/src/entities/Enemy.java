package Entities;

import static utilz.Constants.EnemyConstants;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity{
    private int aniIndex, enemyState, enemyType;
	private int aniTick, aniSpeed = 25;
	private boolean firstUpdate = true;
	private boolean inAir;
	private float fallSpeed = 0.5f;
	private float gravity = 0.04f * Game.scale;
	private float walkSpeed = 0.35f * Game.scale;
	private int walkDir = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitbox(x, y, width, height);
    }

    private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;
			}
		}
	}

    public void update(int[][] lvlData) {
		updateMove(lvlData);
		updateAnimationTick();
	}

	

	private void changeWalkDir() {
		if(walkDir == LEFT){
			walkDir = RIGHT;
		}else{
			walkDir = LEFT;
		}
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getEnemyState() {
		return enemyState;
	}
}
