package game.entities;

import renderEngine.gameObjects.Entity;
import renderEngine.models.Model;

/**
 * Is an abstract class which define an Actor as real time - movable
 * object which the player can interact with.
 * @author Mikhail
 */
public abstract class Actor {

	private Entity entity;
	
	public Actor(Model model, float posX, float posY, float posZ, float rotX, float rotY, float rotZ) {
		setEntity(new Entity(model, posX, posY, posZ, rotX, rotY, rotZ));
	}
	
	public abstract void move(float[] destination);
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
}
