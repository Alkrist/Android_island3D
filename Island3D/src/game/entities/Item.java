package game.entities;

import game.Game;
import renderEngine.gameObjects.Entity;
import renderEngine.models.Model;

/**
 * Is an abstract class which define an Item that can be carried by an Actor.
 * Contains model data and transformation of the model.
 * @author Mikhail
 */
public abstract class Item {

	private Entity entity;
	private int stackAmount;
	
	public Item(int amount, Model model, float posX, float posY, float posZ) {
		setEntity(new Entity(model, posX, posY, posZ, 0, 0, 0));
		entity.setScale(1.5f);
		this.setStackAmount(amount);
	}
	
	public abstract void onPick(Game game);
	public abstract void onDrop(Game game, Tile tile);
	public abstract int getId();
	
	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public void increaseStackAmount() {
		stackAmount++;
	}
	
	public void decreaseStackAmount() {
		stackAmount--;
	}
	
	public int getStackAmount() {
		return stackAmount;
	}

	public void setStackAmount(int stackAmount) {
		this.stackAmount = stackAmount;
	}
}
