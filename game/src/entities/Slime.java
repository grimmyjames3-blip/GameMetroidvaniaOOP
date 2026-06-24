package Entities;

import utilz.Constants.EnemyConstants.*;

public class Slime extends Enemy{
    public Slime(float x, float y) {
        super(x, y, ENEMY_WIDTH, ENEMY_HEIGHT, ENEMY);
        initHitbox(x, y,(int)(22 * Game.scale), (int)(19 * Game.scale));
    }
    
}
