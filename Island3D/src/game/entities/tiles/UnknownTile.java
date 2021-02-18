package game.entities.tiles;

import game.Game;
import game.entities.Tile;
import game.entities.actors.Player;
import renderEngine.models.Model;

public class UnknownTile extends Tile{

	public UnknownTile(Model model, float posX, float posY, float posZ, float rotX, float rotY, float rotZ, int index) {
		super(model, posX, posY, posZ, rotX, rotY, rotZ, index);
	}

	@Override
	public void onEntrance(Game game) {
		game.getLevel().openTile(super.getIndex());
		game.getLevel().getTile(super.getIndex()).onDiscover(game);
		if(game.getBuffer().getActiveActor() instanceof Player) {
			if(((Player) game.getBuffer().getActiveActor()).isInventoryFull())
				((Player) game.getBuffer().getActiveActor())
				.dropItem(game, game.getBuffer().getActiveTile());
		}
	}
		

	@Override
	public void onDiscover(Game game) {
		throw new RuntimeException("Recursed Tile Exception");
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
