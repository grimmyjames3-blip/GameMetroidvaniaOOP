package Utilz;

public class Constants {
    public static class Directions {
        public static final int Left = 0;
        public static final int Up = 1;
        public static final int Right = 2;
        public static final int Down = 3;
    }

    public static class PlayerConstants {
        public static final int Idle = 0;
        public static final int Running = 1;
        public static final int Jumping = 2;
        public static final int Falling = 3;
        public static final int Ground = 4;
        public static final int Hit = 5;
        public static final int Attack1 = 6;
        public static final int Attack_jump_1 = 7;
        public static final int Attack_jump_2 = 8;

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case Running:
                    return 6;
                case Idle:
                    return 5;
                case Hit:
                    return 4;
                case Jumping:
                case Attack1:
                case Attack_jump_1:
                case Attack_jump_2:
                    return 3;
                case Ground:
                    return 2;
                case Falling:
                default:
                    return 1;
            }
        }
    }
}
