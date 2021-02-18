package game.entities.tiles;

import game.Game;
import game.ModelBatch;
import game.entities.Tile;
import game.entities.items.Gold;
import renderEngine.models.Model;

public class TreasureTile extends Tile{

	private int goldAmount;
	
	public TreasureTile(Model model, float posX, float posY, float posZ, float rotX, float rotY, float rotZ,
			int index, int goldAmount) {
		super(model, posX, posY, posZ, rotX, rotY, rotZ, index);
		
		this.setGoldAmount(goldAmount);
	}

	public int getGoldAmount() {
		return goldAmount;
	}

	public void setGoldAmount(int goldAmount) {
		this.goldAmount = goldAmount;
	}

	@Override
	public void onEntrance(Game game) {
	
	}

	@Override
	public void onDiscover(Game game) {
		game.getLevel().spawn(new Gold(goldAmount, ModelBatch.goldModel, super.getEntity().getPosition()));
	}

	@Override
	public void onExit() {
	
	}

	@Override
	public boolean canEnter() {
		return true;
	}
	
	@Override
	public boolean canExit() {
		return true;
	}

}
