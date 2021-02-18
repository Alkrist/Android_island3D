package game.entities.tiles;

import game.Game;
import game.entities.Tile;
import renderEngine.models.Model;

public class ShoreTile extends Tile{

	public ShoreTile(Model model, float posX, float posY, float posZ, float rotX, float rotY, float rotZ, int index) {
		super(model, posX, posY, posZ, rotX, rotY, rotZ, index);
	}

	@Override
	public void onEntrance(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDiscover(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
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
