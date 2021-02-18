package game;

import java.util.ArrayList;
import java.util.List;

import game.entities.Actor;
import game.entities.Tile;
import game.entities.actors.Player;
import game.entities.actors.Ship;
import game.entities.tiles.ShoreTile;
import game.level.LevelGenerator;

public class BattleHandler {

	
	protected static void PvPcheck(Game game) {
		List<Actor>batch = new ArrayList<Actor>();
		for(Actor actor: game.getLevel().getActors()) {
			//If it's a player
			if(actor instanceof Player) {
				//If the player is enemy
				if(((Player) actor).getTeam()!= game.getTeam()) {
					if(game.getBuffer().getPickedTile()==
					game.getLevel().getTile(actor.getEntity().getPosition()[0], actor.getEntity().getPosition()[2])) {
						batch.add(actor);
					}
				}
			}
		}for(Actor actor: batch) {
			if(game.getLevel().getActors().contains(actor)) {
				game.getLevel().despawn(actor);
				((Player)actor).dropItem(game, game.getBuffer().getActiveTile());
				game.getLevel().respawn((Player) actor);
			}
				
		}
	}
	
	protected static void SvPcheck(Game game) {
		Tile tile = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0],
				game.getBuffer().getPickedTile().getEntity().getPosition()[2]+LevelGenerator.TILE_OFFSET);
		
		if(tile==null || tile instanceof ShoreTile)
			tile  = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0],
					game.getBuffer().getPickedTile().getEntity().getPosition()[2]-LevelGenerator.TILE_OFFSET);
		if(tile==null || tile instanceof ShoreTile)
			tile  = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0]+LevelGenerator.TILE_OFFSET,
					game.getBuffer().getPickedTile().getEntity().getPosition()[2]);
		if(tile==null || tile instanceof ShoreTile)
			tile  = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0]-LevelGenerator.TILE_OFFSET,
					game.getBuffer().getPickedTile().getEntity().getPosition()[2]);
		
		List<Player> batch = new ArrayList<Player>();
		for(Actor actor: game.getLevel().getActors()) {
			if(actor instanceof Player) {
				if(game.getLevel().getTile(actor.getEntity().getPosition()[0],
						actor.getEntity().getPosition()[2]) == tile)
					if(((Player)actor).getTeam()!=game.getTeam())
						batch.add((Player)actor);
			}
		}
		for(Player player: batch) {
			player.dropItem(game, game.getBuffer().getActiveTile());
			game.getLevel().despawn(player);
		}
	}
	
	protected static void PvSCheck(Game game) {
		Tile tile = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0],
				game.getBuffer().getPickedTile().getEntity().getPosition()[2]+LevelGenerator.TILE_OFFSET*2);
		if(!(tile instanceof ShoreTile))
			tile = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0],
					game.getBuffer().getPickedTile().getEntity().getPosition()[2]-LevelGenerator.TILE_OFFSET*2);
		if(!(tile instanceof ShoreTile))
			tile = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0]+LevelGenerator.TILE_OFFSET*2,
					game.getBuffer().getPickedTile().getEntity().getPosition()[2]);
		if(!(tile instanceof ShoreTile))
			tile = game.getLevel().getTile(game.getBuffer().getPickedTile().getEntity().getPosition()[0]-LevelGenerator.TILE_OFFSET*2,
					game.getBuffer().getPickedTile().getEntity().getPosition()[2]);
		
		if(!(tile instanceof ShoreTile))
			return;
		else {
			if(tile != game.getBuffer().getActiveTile()) {
				for(Actor actor: game.getLevel().getActors()) {
					if(actor instanceof Ship) {
						if(((Ship)actor).getTeam()!=game.getTeam()) {
							if(game.getLevel().getTile(actor.getEntity().getPosition()[0],
									actor.getEntity().getPosition()[2]) == tile) {
								game.getLevel().despawn(game.getBuffer().getActiveActor());
								return;
							}
								
						}
					}
				}
			}
		}
	}

	
}
