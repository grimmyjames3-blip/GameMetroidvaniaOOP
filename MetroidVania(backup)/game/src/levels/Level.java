package levels;

import entities.Slime;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import main.Game;
import objects.Cannon;
import utils.HelpMethods;
import static utils.HelpMethods.GetLevelData;
import static utils.HelpMethods.GetPlayerSpawn;
import static utils.HelpMethods.GetSlimes;

public class Level {

	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Slime> slimes; 
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;

	private ArrayList<Cannon> cannons;
	
	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		createEnemies();
		calcLvlOffsets();
		calcPlayerSpawn();

		createCannons();

	}

	private void createCannons() {
		cannons = HelpMethods.GetCannons(img);
	}

	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
	}

	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	private void createEnemies() {
		slimes = GetSlimes(img);
	}

	private void createLevelData() {
		lvlData = GetLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}

	public int getLvlOffset() {
		return maxLvlOffsetX;
	}

	public ArrayList<Slime> getSlimes() { 
		return slimes;
	}

	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	public ArrayList<Cannon> getCannons(){	
		return cannons;
	}

}