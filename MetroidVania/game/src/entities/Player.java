package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import main.Game;
import static utils.Constants.*;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import utils.LoadSave;

public class Player extends Entity {
	// Animation
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;

	// Movement
	private boolean left, right, jump;

	public boolean isJump() { return jump; }
	public void setJump(boolean jump) { this.jump = jump; }

	private int[][] lvlData;
	
	private int tileY = 0;

	// Hitbox draw offsets
	private float xDrawOffset = 20 * Game.SCALE;
	private float yDrawOffset = 13 * Game.SCALE;

	// Physics

	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

	// UI Status Bar
	private BufferedImage statusbarImg;
	private int statusBarWidth  = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58  * Game.SCALE);
	private int statusBarX      = (int) (10  * Game.SCALE);
	private int statusBarY      = (int) (10  * Game.SCALE);

	// Health bar
	private int healthBarWidth  = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4   * Game.SCALE);
	private int healthBarXStart = (int) (34  * Game.SCALE);
	private int healthBarYStart = (int) (14  * Game.SCALE);
	
	//private int maxHealth = 100;
	//private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;

	private int powerBarWidth = (int) (104 * Game.SCALE);
	private int powerBarHeight = (int) (2 * Game.SCALE);
	private int powerBarXStart = (int) (44 * Game.SCALE);
	private int powerBarYStart = (int) (34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;

	// Attack box
	private Rectangle2D.Float attackBox;
	private int flipX = 0;
	private int flipW = 1;
	private boolean attackChecked = false;

	private int attackBoxOffsetX;

	private Playing playing;

	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;
	private boolean powerAttackWindup;
	private int powerAttackWindupTick;
	private int powerAttackWindupDuration = 20;

	// hanging / climbing feature thingy
	private boolean hanging = false;
	private float hangX, hangY;
	private boolean hangCooldown = false;
	private int hangCooldownTick = 0;
	private int hangCooldownDuration = 30;
	private boolean jumpPressed = false;
	private int airTimeTick = 0;
	private int hangAirTimeReq = 18;
	
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initHitbox(x, y, 15, 29);
		initAttackBox();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 2);
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();

		if (currentHealth <= 0) {
			if (state != DEATH) {
				state = DEATH;
				aniIndex = 0;
				aniTick = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			} else if (aniIndex == GetSpriteAmount(DEATH) - 1 && aniTick >= ANI_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else {
				updateAnimationTick();
			}
			return;
		}

		if (hanging) {
			if (jump && !jumpPressed) {
				vaultUp();
			}
			jumpPressed = jump;
			updateAnimationTick();
			setAnimation();
			return;
		}
		jumpPressed = jump;

		if (hangCooldown) {
			hangCooldownTick++;
			if (hangCooldownTick >= hangCooldownDuration) {
				hangCooldown = false;
				hangCooldownTick = 0;
			}
		}

		updateAttackBox();
		updatePos();

		if (moving) {
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			if (powerAttackActive) {
				powerAttackTick++;
				if (powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}

		if (attacking || powerAttackActive)
			checkAttack();
		updateAnimationTick();
		setAnimation();
	}

	private void startHang() {
		hanging = true;
		inAir = false;
		airSpeed = 0;
		airTimeTick = 0;

		hangX = hitbox.x;

		int wallTileY = (int)(hitbox.y / Game.TILES_SIZE);
		hangY = (wallTileY + 1) * Game.TILES_SIZE - hitbox.height;

		hitbox.y = hangY;
	}

	private void vaultUp() {
		hanging = false;
		inAir = true;
		airSpeed = jumpSpeed;
		hangCooldown = true;
		hangCooldownTick = 0;
	}

	private void checkAttack() {
		if (attackChecked || aniIndex != 5)
			return;
		attackChecked = true;
		if (powerAttackActive)
			attackChecked = false;
		playing.checkEnemyHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
	}

	private void updateAttackBox() {
		if (right || (powerAttackActive && flipW == 1))
			attackBox.x = hitbox.x + hitbox.width + attackBoxOffsetX;
		else if (left || (powerAttackActive && flipW == -1))
			attackBox.x = hitbox.x - hitbox.width - attackBoxOffsetX;
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}

		private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	public void render(Graphics g, int lvlOffsetX, int lvlOffsetY) {
		float drawX;
		float drawY;

		if (hanging) {
			float hangDrawXOffsetLeft = width * 0.1f;
			float hangDrawXOffsetRight = width * 0.3f;
			float hangDrawYOffset = height * - 0.1f;

			if (flipW == 1)
				drawX = hitbox.x - xDrawOffset + hangDrawXOffsetLeft;
			else
				drawX = hitbox.x - xDrawOffset - hangDrawXOffsetRight;

			drawY = hitbox.y - yDrawOffset - hangDrawYOffset - lvlOffsetY;
		} else {
			if (flipW == 1)
				drawX = hitbox.x - xDrawOffset;
			else
				drawX = hitbox.x - (width - hitbox.width - xDrawOffset);

			drawY = hitbox.y - yDrawOffset - lvlOffsetY;
		} 

		g.drawImage(animations[state][aniIndex],
				(int) drawX - lvlOffsetX + flipX,
				(int) drawY,
				width * flipW, height, null);
		drawUI(g);
		drawHitbox(g, lvlOffsetX, lvlOffsetY);
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusbarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	g.setColor(Color.yellow);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
		
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;

			if (state == DASH_ATTACK) {
				if (aniIndex == 3) {
					powerAttackWindup = false;
					powerAttackActive = true;
				}
				if (aniIndex >= 8) {
					aniIndex = 0;
					powerAttackActive = false;
					powerAttackTick = 0;
				}
				return;
			}

			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
			}
		}
	}

	private void setAnimation() {
		int startAni = state;

		if (moving)
			state = RUNNING;
		else
			state = IDLE;

		if (inAir) {
			if (airSpeed < 0)
				state = JUMPING;
			else
				state = FALLING;
		}

		if (hanging) {
			state = CLIMBING;
			return;
		}

		if (attacking) {
			state = ATTACK;
			if (startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}

		if (powerAttackWindup || powerAttackActive) {
    		state = DASH_ATTACK;
    		return;
		}

		if (startAni != state)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;

		if (jump)
			jump();

		if (hanging)
			return;

		if (!inAir)
			if (!powerAttackActive)
				if ((!left && !right) || (right && left))
					return;

		float xSpeed = 0;

		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}

		if (powerAttackActive) {
			if (!left && !right) {
				if (flipW == -1)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;
			}

			float dashProgress = (aniIndex - 3) / 4.0f;

			// multiplier eases from 4x down to 1x
			float dashMultiplier = 2.5f - (2.0f * dashProgress);
			xSpeed *= dashMultiplier;
		}

		if (!inAir) {
            if (!IsEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            } else {
                airTimeTick = 0;
            }
        }

        if (inAir) {
            airTimeTick++;
        }

		if (inAir  && !powerAttackActive) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);

			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
		} else {
			updateXPos(xSpeed);
		}
		moving = true;
	}

	private void jump() {
		if (inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
		airTimeTick = 0;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
		airTimeTick = 0;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);

			if (inAir && !hanging && !hangCooldown && airTimeTick >= hangAirTimeReq) {
                checkLedgeGrab(xSpeed);
            }

			if (powerAttackActive) {
				powerAttackActive = false;
				powerAttackTick = 0;
			}
		}
	}

	private void checkLedgeGrab(float xSpeed) {
		float[] sidesX;
		if (xSpeed > 0)
			sidesX = new float[]{ hitbox.x + hitbox.width + 1 };
		else if (xSpeed < 0)
			sidesX = new float[]{ hitbox.x - 1 };
		else
			sidesX = new float[]{ hitbox.x + hitbox.width + 1, hitbox.x - 1 };

		for (float wallX : sidesX) {
			boolean wallSolid = IsSolid(wallX, hitbox.y, lvlData);
			boolean aboveOpen = !IsSolid(wallX, hitbox.y - Game.TILES_SIZE, lvlData);

			int wallTileY = (int)(hitbox.y / Game.TILES_SIZE);
			float wallTopY = wallTileY * Game.TILES_SIZE;
			float grabRange = Game.TILES_SIZE * 0.5f;
			boolean inGrabRange = Math.abs(hitbox.y - wallTopY) <= grabRange;

			if (wallSolid && aboveOpen && inGrabRange) {
				// set facing direction toward wall
				if (wallX > hitbox.x) {
					flipW = 1;
					flipX = 0;
				} else {
					flipW = -1;
					flipX = width;
				}
				startHang();
				return;
			}
		}
	}

	public void changeHealth(int value) {
		currentHealth += value;
		if (currentHealth <= 0)
			currentHealth = 0;
		else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}
	public void kill() {
		currentHealth = 0;
	}

	
	public void changePower(int value) {
		powerValue += value;
		if (powerValue >= powerMaxValue)
			powerValue = powerMaxValue;
		else if (powerValue <= 0)
			powerValue = 0;
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[15][12];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 69, j * 44, 69, 44);

		statusbarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
	}

	public void setAttacking(boolean attacking) { this.attacking = attacking; }
	public boolean isLeft()  { return left; }
	public void setLeft(boolean left)   { this.left = left; }

	public boolean isRight() { return right; }
	public void setRight(boolean right) { this.right = right; }


	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		hanging = false;
		state = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}

	public void powerAttack() {
		if (powerAttackActive || powerAttackWindup)
			return;
		if (powerValue >= 60) {
			powerAttackWindup = true;
			powerAttackWindupTick = 0;
			changePower(-60);
		}
	}
}
