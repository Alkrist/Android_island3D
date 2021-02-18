package game.entities;

import game.Game;
import game.level.LevelGenerator;
import renderEngine.gameObjects.Entity;
import renderEngine.models.Model;

/**
 * Is an abstract class which define a chunk of the level with it's features, ID and model.
 * @author Mikhail
 */
public abstract class Tile {

	private Entity entity;
	private int index;
	
	public Tile(Model model, float posX, float posY, float posZ, float rotX, float rotY, float rotZ, int index) {
		setEntity(new Entity(model, posX, posY, posZ, rotX, rotY, rotZ));
		entity.setScale(LevelGenerator.SCALE);
		this.index = index;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public abstract void onEntrance(Game game);
	public abstract void onDiscover(Game game);
	public abstract void onExit();
	
	public abstract boolean canEnter();
	public abstract boolean canExit();

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}
