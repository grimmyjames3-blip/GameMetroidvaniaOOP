package Entities;

import utilz.Constants.EnemyConstants.*;

public class Slime extends Enemy{
    public Slime(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY);
        initHitbox(x, y,(int)(22 * Game.scale), (int)(19 * Game.scale));
    }

    private void updateMove(int[][] lvlData) {
		if(firstUpdate){
			if(!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;
			firstUpdate = false;
		}

		if(inAir){
			if(CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)){
				hitbox.y += fallSpeed;
				fallSpeed += gravity;
			}else{  
				inAir = false;
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed);
			}
		}else{
			switch(enemyState){
				case IDLE:
					enemyState = RUNNING;
					break;
				case RUNNING:
					float xSpeed = 0;

					if(walkDir == LEFT){
						xSpeed = -walkSpeed;
					}else{
						xSpeed = walkSpeed;
					}

					if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)){
						if(IsFloor(hitbox, xSpeed, lvlData)){
							hitbox.x += xSpeed;
							return;
						}else{
							if(walkDir == LEFT){
								walkDir = RIGHT;
							}else{
								walkDir = LEFT;
							}
						}
					}
					changeWalkDir();
					break;
			}
		}
	}
    
}
