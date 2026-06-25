package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import utils.LoadSave;

public class Player extends Entity {
    //animation shenanigans
    private BufferedImage[][] animations;
    private int animationTick = 0, animationSpeed = 15, animationIndex = 0;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;

    //movement variables and lvl collision
    private boolean left, up, right, down, jump;
    private float playerSpeed = 2.0f;
    private int[][] lvlData;

    //hitbox offsets
    private float xDrawOffset = 20 * Game.SCALE;
    private float yDrawOffset = 13 * Game.SCALE;

    //gravity stuff for jumping and falling idk
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitbox(x, y, 15 * Game.SCALE, 29 * Game.SCALE);
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= getSpriteAmount(playerAction)) {
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex], (int)hitbox.x - (int)xDrawOffset, (int)hitbox.y - (int)yDrawOffset, width, height, null);
        // drawHitbox(g);
    }

    public void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (attacking) {
            playerAction = ATTACK;
        }

        if (inAir){
            if (airSpeed < 0){
                playerAction = JUMPING;
            } else {
                playerAction = FALLING;
            }
        }

        if (startAni != playerAction) {
            animationIndex = 0;
            animationTick = 0;
        }
    }

    public void setDeltas(int xDelta, int yDelta) {
        this.x = xDelta;
        this.y = yDelta;
    }

    private void updatePosition() {
        moving = false;

        if (jump){
            jump();
        }

        if (!left && !right && !inAir){
            return;
        }

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
        }
        if (right) {
            xSpeed += playerSpeed;
        }

        if (!inAir){
            if (!isEntityOnFloor(hitbox, lvlData)){
                inAir = true;
            }
        }

        if (inAir) {
            if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = getEntityPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0){
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }

        moving = true;
    }

    private void jump(){
        if (inAir){
            return;
        }
        inAir = true;
        airSpeed += jumpSpeed;
    }

    private void resetInAir(){
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed){
        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[15][12];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 69, j * 44, 69, 44);
            }
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;

        if (!isEntityOnFloor(hitbox, lvlData)){
            inAir = true;
        }
    }

    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDown() {
        return down;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setJump(boolean jump){
        this.jump = jump;
    }
}
