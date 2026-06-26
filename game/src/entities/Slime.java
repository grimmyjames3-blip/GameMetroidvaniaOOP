package Entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.Directions.*;

public class Slime extends Enemy{
	//AttackBox slime
	private Rectangle2D.Float attackBox;
	private int attackBoxOffsetX;
	
    public Slime(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY);
        initHitbox(x, y,(int)(22 * Game.scale), (int)(19 * Game.scale));
		initAttackBox();
    }

	private void initAttackBox(){
		attackBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int)(19 * Game.scale));
		attackBoxOffsetX = (int) (Game.SCALE * 30);	
	}

	public void update(int[][] lvlData, Player player) {
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox(){
		attackBox.x = hitBox.x - attackBoxOffsetX;
		attackBox.y = hitBox.y;
	}

    private void updateBehaviour(int[][] lvlData, Player player) {
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
				case ATTACK:
					// Buat check animasi attack enemy apakah kena player di animationIndex sprite ke berapa
					if(aniIndex == 3 && !attackChecked){
						
					}
					break;
				case HIT:
					break;
			}
		}
	}

	public void drawAttackBox(Graphics g, int xLvlOffset){
		g.setColor(Color.red);
		g.drawRect((int)(attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	public int flipX(){
		if(walkDir == RIGHT){
			return width;
		}else{
			return 0;
		}
	}

	public int flipW(){
		if(walkDir == RIGHT){
			return -1;
		}else{
			return 1;
		}
	}
}
