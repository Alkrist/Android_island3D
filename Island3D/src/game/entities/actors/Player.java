package game.entities.actors;

import game.Game;
import game.GameHandler;
import game.entities.Actor;
import game.entities.Item;
import game.entities.Tile;
import renderEngine.models.Model;

public class Player extends Actor{

	private short team;
	private int inventory = 0;
	
	public Player(short team, Model model, float[] position, float rotX, float rotY, float rotZ) {
		super(model, position[0], position[1], position[2], rotX, rotY, rotZ);
		this.setTeam(team);
	}

	@Override
	public void move(float[] destination) {
		super.getEntity().setPosition(destination[0],destination[1],destination[2]);
	}

	public boolean isInventoryFull() {
		return (inventory != 0);
	}
	
	public void pickItem(Game game, Item item) {
		if(inventory == 0) {
			inventory = item.getId();
			item.onPick(game);
		}/*else {
			GameHandler.dropItem(game, this, inventory, tile);
			inventory = item.getId();
			item.onPick(game);
		}*/
	}
	
	public void dropItem(Game game, Tile tile) {	
			GameHandler.dropItem(game, this, inventory, tile);
			inventory = 0;
		
	}
	
	public short getTeam() {
		return team;
	}

	public void setTeam(short team) {
		this.team = team;
	}

	public int getInventory() {
		return inventory;
	}
}
