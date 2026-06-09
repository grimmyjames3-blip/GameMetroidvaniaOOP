package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utils.LoadSave;

public class LevelManager {
    private Game game;
    private BufferedImage[] levelSprite;
    private Levels level_one;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        level_one = new Levels(LoadSave.getLevelData());
    }

    public void importOutsideSprites() {
        BufferedImage img = LoadSave.getSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                int index = i * 12 + j;
                levelSprite[index] = img.getSubimage(j * 32, i * 32, 32, 32);
            }
        }
    }

    public void draw(Graphics g) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < Game.TILES_IN_WIDTH; i++) {
                int index = level_one.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i * Game.TILE_SIZE, j * Game.TILE_SIZE, Game.TILE_SIZE, Game.TILE_SIZE, null);
            }
        }
    }

    public void update() {
        // Update level logic here
    }

    public Levels getCurrentLevel(){
        return level_one;
    }
}
