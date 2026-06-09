package utils;

public class Constants {

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
