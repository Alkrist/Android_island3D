package renderEngine.gameObjects;

import game.level.LevelGenerator;

public class Water {

	public static final float TILE_SIZE = LevelGenerator.TILE_OFFSET*4;
	
	private float height;
	private float x, z;
	
	public Water(float height, float x, float z) {
		this.height = height;
		this.x = x;
		this.z = z;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	
}
