package game;

import game.entities.Actor;
import game.entities.Tile;
import game.level.Level;

public class MoveBuffer {

	private Tile activeTile;
	private Tile pickedTile;
	private Actor activeActor;
	
	
	public Tile getActiveTile() {
		return activeTile;
	}
	public Tile getPickedTile() {
		return pickedTile;
	}
	public Actor getActiveActor() {
		return activeActor;
	}
	
	public void setPickedTile(Tile tile) {
		pickedTile = tile;
	}
	
	public void setActor(Actor actor, Level level) {
		activeActor = actor;
		if(actor!=null)
		activeTile = level.getTile(actor.getEntity().getPosition()[0],
				actor.getEntity().getPosition()[2]);
		else activeTile = null;
	}
	
	public void cleanUp() {
		activeTile = null;
		activeActor = null;
		pickedTile = null;
	}
}
