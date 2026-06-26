package Entities;

import utilz.Constants.EnemyConstants.*;

public class Slime extends Enemy{
    public Slime(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY);
        initHitbox(x, y,(int)(22 * Game.scale), (int)(19 * Game.scale));
    }

	public void update(int[][] lvlData, Player player) {
		updateMove(lvlData, player);
		updateAnimationTick();
	}

    private void updateMove(int[][] lvlData, Player player) {
		if(firstUpdate)
			firstUpdateCheck(lvlData);
		if(inAir){
			updateInAir(lvlData);
		}else{
			switch(enemyState){
				case IDLE:
					newState(RUNNING);
					break;
				case RUNNING:
					if(canSeePlayer(lvlData, player)){
						turnTowardsPlayer(player);
					}
					if(isPlayerCloseForAttack(player)){
						newState(ATTACK);
					}
					move(lvlData);
					break;
			}
		}
	}
    
}
