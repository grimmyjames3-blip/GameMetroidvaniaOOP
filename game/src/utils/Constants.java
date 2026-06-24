package utils;

public class Constants {
    public static class EnemyConstants {
		public static final int ENEMY = 0;

		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;

		public static final int ENEMY_WIDTH_Default = 72;
		public static final int ENEMY_HEIGHT_Default = 32;

		public static final int ENEMY_WIDTH = (int) (ENEMY_WIDTH_Default * Game.SCALE);
		public static final int ENEMY_HEIGHT = (int) (ENEMY_HEIGHT_Default * Game.SCALE);

		// Buat beda antara posisi gambar enemy dengan posisi hitbox enemy, karena gambar enemy lebih besar dari hitbox enemy
        public static final int ENEMY_DRAWOFFSET_X = (int) (26 * Game.SCALE);
        public static final int ENEMY_DRAWOFFSET_Y = (int) (9 * Game.SCALE);

		public static int GetSpriteAmount(int enemy_type, int enemy_state) {

			switch (enemy_type) {
			case ENEMY:
				switch (enemy_state) {
				case IDLE:
					return 9;
				case RUNNING:
					return 6;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEAD:
					return 5;
				}
			}
			return 0;
		}
	}

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }
    
    public static class PlayerConstants {

        // NOT DONE WILL BE CHANGED DEPENDING ON SPRITESHEET BTW (STILL TEMP AND IS SUBJECT TO CHANGE)
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int DEATH = 3;
        public static final int HIT = 4;
        public static final int JUMPING = 5;
        public static final int FALLING = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;

        public static int getSpriteAmount(int playerAction) {
            switch (playerAction) {
                case DEATH:
                    return 11;
                case RUNNING:
                    return 8;
                case IDLE:
                    return 6;
                case JUMPING:
                    return 5;
                case HIT:
                    return 4;
                case FALLING:
                    return 3;
                case ATTACK:
                    return 9;
                case ATTACK_JUMP_1:
                case ATTACK_JUMP_2:
                    return 3;
                default:
                    return 1;
            }
        }
    }
}
