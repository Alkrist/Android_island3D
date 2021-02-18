package game.entities.items;

import game.Game;
import game.entities.Item;
import game.entities.Tile;
import renderEngine.models.Model;



public class Gold extends Item{
	
	private final int ID = 1;
	
	public Gold(int amount, Model model, float[] position) {
		super(amount, model, position[0], position[1], position[2]);
	}

	@Override
	public void onPick(Game game) {
		super.decreaseStackAmount();
		if(super.getStackAmount()<=0)
			game.getLevel().despawn(this);
	}

	@Override
	public void onDrop(Game game, Tile tile) {
		Item item = game.getLevel().getItem(tile, ID);
		if(item!= null)
			item.increaseStackAmount();
		else game.getLevel().spawn(this);		
	}

	@Override
	public int getId() {
		return ID;
	}

}
