package game;


import game.entities.Actor;
import game.entities.Item;
import game.entities.Tile;
import game.entities.actors.Player;
import game.entities.actors.Ship;
import game.entities.items.Gold;
import game.entities.tiles.ShoreTile;
import game.level.LevelGenerator;

public class GameHandler {

	protected static void viewActor(Game game, float[] point) {
			//TODO: show actor data
	}
	
	protected static void processMove(Game game, float[] point) {
		//Case it's 1 stage --> choose tile
		if(game.getStage() > 0) {
			Tile tile = game.getLevel().spotTaggedTiles(point);
			if(tile!=null) {
				game.getBuffer().setPickedTile(tile);
				selectTile(game, point);
			}
		
		//Case it's 0 stage --> choose actor
		}else {
			Actor actor = game.getLevel().spotTaggedActors(point);
			if(actor!=null) {
				/*Continue with next stage only if chosen actor is ship or player
				 of the active team*/
				if(actor instanceof Player) {
					if(((Player) actor).getTeam()==game.getTeam()) {
						game.getBuffer().setActor(actor, game.getLevel());
						game.shuffleStage();
					}						
				}else if(actor instanceof Ship) {
					if(((Ship) actor).getTeam()==game.getTeam()) {
						game.getBuffer().setActor(actor, game.getLevel());
						game.shuffleStage();
					}						
				}
			}
		}
	}
	
	protected static void selectTile(Game game, float[] point) {
		//Case the actor is player
		if(game.getBuffer().getActiveActor() instanceof Player) {
			
			//TODO;
			//In case the player clicked on the same tile and has any item - drop it
			/*if(game.getBuffer().getActiveTile().getEntity().getPosition()[0] == point[0]
					&& game.getBuffer().getActiveTile().getEntity().getPosition()[2] == point[1]) {
				if(((Player)game.getBuffer().getActiveActor()).isInventoryFull())
					((Player)game.getBuffer().getActiveActor()).dropItem(game);	
			}*/
						
			if(isInNeighborhood_Player(game, point)) {
				//If clicked onto water
				if(game.getBuffer().getPickedTile() instanceof ShoreTile)
					movePlayerToWater(game);
				//If clicked onto terrain
				else
					movePlayerOnIsland(game);						
			}
			//To repick an actor if clicked somewhere out
			else {
				game.shuffleStage();
				processMove(game, point);
			}
			
		//Case the actor is ship	
		}else if (game.getBuffer().getActiveActor() instanceof Ship) {
			if(isInNeighborhood_Ship(game, point)) {
				//If there are some players on board
				if(((Ship) game.getBuffer().getActiveActor()).getCrew()>0) {
					//If clicked onto water
					if(game.getBuffer().getPickedTile() instanceof ShoreTile)
						moveShip(game);
					//If clicked onto terrain
					else
						spawnPlayerNearShip(game);
				}else {
					game.getBuffer().cleanUp();
					game.shuffleStage();
				}	
			}
			//To repick an actor if clicked somewhere out
			else {
				game.shuffleStage();
				processMove(game, point);
			}
		}		
	}
	
	protected static void moveShip(Game game) {
		if(cornerCheck(game.getBuffer().getActiveTile(), game.getBuffer().getPickedTile())) {
			game.getBuffer().getActiveActor().move(game.getBuffer().getPickedTile().getEntity().getPosition());
			BattleHandler.SvPcheck(game);
			game.shuffleStage();
			game.shuffleTeams();
		}
	}
	
	protected static void spawnPlayerNearShip(Game game) {
		Ship ship = (Ship) game.getBuffer().getActiveActor();
		if(canSpawnPlayer(ship, game.getBuffer().getPickedTile())) {
			ship.decreaseCrew();
			game.getLevel().spawn(new Player(ship.getTeam(), ModelBatch.playerModel,
			game.getBuffer().getPickedTile().getEntity().getPosition(), 0, 0, 0));
			game.getBuffer().getPickedTile().onEntrance(game);
			game.shuffleStage();
			game.shuffleTeams();
		}		
	}
	
	protected static void movePlayerToWater(Game game) {
		Ship ship = getShipOnTile(game, game.getBuffer().getPickedTile());
		if(ship!=null) {
			//In case the ship is friendly
			if(ship.getTeam()==game.getTeam()) {
				if(((Player) game.getBuffer().getActiveActor()).isInventoryFull()) {
					game.increaseScore(ship.getTeam());
					game.decreaseTotalGoldLeft();
					if(game.getTotalGold()<= 0)
						game.onGameEnded();
				}
				game.getLevel().despawn(game.getBuffer().getActiveActor());
				ship.increaseCrew();
				game.shuffleStage();
				game.shuffleTeams();
			//In case the ship is enemy
			}else {
				game.getLevel().despawn(game.getBuffer().getActiveActor());
				game.shuffleStage();
				game.shuffleTeams();
			}
		//In case there's no ship, player can move only if he's already in water
		}else if(game.getBuffer().getActiveTile() instanceof ShoreTile){
			game.getBuffer().getActiveActor().move(game.getBuffer().getPickedTile().getEntity().getPosition());
		}
	}
	
	protected static void movePlayerOnIsland(Game game) {
		if(game.getBuffer().getActiveTile()==game.getBuffer().getPickedTile()) {
			//Whether the player has no item picked --> pick it.
			if(!((Player) game.getBuffer().getActiveActor()).isInventoryFull()) {
				Item item = game.getLevel().getItem(game.getBuffer().getActiveTile());
				if(item!=null)
					((Player) game.getBuffer().getActiveActor()).pickItem(game, item);
			//Whether the player has already item picked --> drop it.
			}else {
				((Player) game.getBuffer().getActiveActor()).dropItem(game, game.getBuffer().getActiveTile());
			}
		
		}else {
			//If player can leave current tile right now
			if(game.getBuffer().getActiveTile().canExit()) {
				//If player can enter the chosen tile
				if(game.getBuffer().getPickedTile().canEnter()) {
					game.getBuffer().getActiveActor().move(game.getBuffer().getPickedTile().getEntity().getPosition());
					game.getBuffer().getPickedTile().onEntrance(game);
					BattleHandler.PvPcheck(game);
					BattleHandler.PvSCheck(game);
					game.shuffleStage();
					game.shuffleTeams();
				}			
			}else game.getBuffer().getActiveTile().onExit();
		}
	}
	
	private static  boolean isInNeighborhood_Ship(Game game, float[] point) {
		if(game.getBuffer().getActiveTile()!=null) {
			if(game.getBuffer().getActiveTile()!=game.getBuffer().getPickedTile()) {
				float[] active = game.getBuffer().getActiveTile().getEntity().getPosition();
				float[] picked = game.getBuffer().getPickedTile().getEntity().getPosition();
				if(picked[0]<= (active[0]+LevelGenerator.TILE_OFFSET*2)+0.1f 
					&& picked[0] >= (active[0]-LevelGenerator.TILE_OFFSET*2)-0.1f			
					&& picked[2] <= (active[2]+LevelGenerator.TILE_OFFSET*2)+0.1f 
					&& picked[2] >= (active[2]-LevelGenerator.TILE_OFFSET*2)-0.1f)		
				return true;
			else return false;			
			}		
		}			
		return false;
	}
	
	private static  boolean isInNeighborhood_Player(Game game, float[] point) {
		if(game.getBuffer().getActiveTile()!=null) {
			float[] active = game.getBuffer().getActiveTile().getEntity().getPosition();
			float[] picked = game.getBuffer().getPickedTile().getEntity().getPosition();
			if(picked[0]<= (active[0]+LevelGenerator.TILE_OFFSET*2)+0.1f 
				&& picked[0] >= (active[0]-LevelGenerator.TILE_OFFSET*2)-0.1f			
				&& picked[2] <= (active[2]+LevelGenerator.TILE_OFFSET*2)+0.1f 
				&& picked[2] >= (active[2]-LevelGenerator.TILE_OFFSET*2)-0.1f)		
				return true;
			else return false;				
		}			
		return false;
	}

	private static boolean canSpawnPlayer(Ship ship, Tile ground) {
		if(ship.getEntity().getPosition()[0] == ground.getEntity().getPosition()[0]
				|| ship.getEntity().getPosition()[2] == ground.getEntity().getPosition()[2])
			return true;
		else return false;		
	}
	
	private static boolean cornerCheck(Tile active, Tile picked) {
		if(active.getEntity().getPosition()[0] == picked.getEntity().getPosition()[0]
				|| active.getEntity().getPosition()[2] == picked.getEntity().getPosition()[2])
			return true;
		else return false;
	}
	
	private static Ship getShipOnTile(Game game, Tile tile) {
		for(Actor actor: game.getLevel().getActors()) {
			if(actor instanceof Ship) {
				if(actor.getEntity().getPosition()[0] <= tile.getEntity().getPosition()[0]+LevelGenerator.TILE_OFFSET
						&& actor.getEntity().getPosition()[0] >= tile.getEntity().getPosition()[0]-LevelGenerator.TILE_OFFSET
						&& actor.getEntity().getPosition()[2] <= tile.getEntity().getPosition()[2]+LevelGenerator.TILE_OFFSET
						&& actor.getEntity().getPosition()[2] >= tile.getEntity().getPosition()[2]-LevelGenerator.TILE_OFFSET)
					return (Ship) actor;
			}
		}
		return null;
	}
	
	public static void dropItem(Game game, Player player, int id, Tile tile) {
		Item item;
		switch(id) {
		case 1:
			item = new Gold(1, ModelBatch.goldModel, 
					tile.getEntity().getPosition());
			item.onDrop(game, tile);
			break;
		default:
			break;
		}
	}
}
