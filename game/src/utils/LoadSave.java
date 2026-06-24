package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import main.Game;

// Buat ambil function dari constants and entity slime
import entities.Slime;
import static utilz.Constants.EnemyConstants.Enemy;

public class LoadSave {

    public static final String PLAYER_ATLAS = "vend_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data.png";
    // Ini buat sprite musuh
    // public static final String ENEMY_SPRITE = "enemy_sprite.png";
    
    public static BufferedImage getSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/res/" + fileName);
        
        try{
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }       
        }
        return img;
    }

    // Buat summon enemy slime di map dimana pixel berwarna green
    public static ArrayList<Slime> getSlimes(){
        BufferedImage img = getSpriteAtlas(LEVEL_ONE_DATA);
        ArrayList<Slime> list = new ArrayList<>();
        for(int j = 0; j < img.getHeight(); j++){
            for(int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == Enemy)
                    list.add(new Slime(i * Game.TILE_SIZE, j * TiLE_SIZE));
            }
        return list;
        }
    }

    public static int[][] getLevelData(){
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = getSpriteAtlas(LEVEL_ONE_DATA);

        for(int j = 0; j < img.getHeight(); j++){
            for(int i = 0; i < img.getWidth(); i++){
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
