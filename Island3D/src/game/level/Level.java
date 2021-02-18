package game.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import game.entities.Actor;
import game.entities.Item;
import game.entities.Tile;
import game.entities.actors.Player;
import game.entities.actors.Ship;
import renderEngine.Loader;
import renderEngine.gameObjects.Camera;
import renderEngine.gameObjects.Water;
import renderEngine.renderers.GameMasterRenderer;
import renderEngine.renderers.WaterRenderer;
import renderEngine.shaders.WaterShader;

/**
 * The Level class holds all the level-related data including tiles and all actors and other entities
 * and performs the rendering of all the level-related stuff. To get any level-related data we need 
 * to call this class.
 * 
 * @author Mikhail
 */
public class Level {

	private float[] projectionMatrix;
	private LevelGenerator generator;	
	private int levelSize;
	private float[] center = new float[2];
	
	//All active tiles to render
	private Map<Integer, Tile> tiles = new HashMap<Integer, Tile>();
	//Tile types of Integer range which will be converted to tile objects in Level Generator
	private Map<Integer, Integer> levelHash = new HashMap<Integer, Integer>();
	
	//Actors and Items
	private List<Actor> actors = new ArrayList<Actor>();
	private List<Item> items = new ArrayList<Item>();
	
	// **** TEST ****
	private List<Water> waters = new ArrayList<Water>();	
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	
	/**
	 * The Level constructor.
	 * Generates the level based on the given parameters.
	 * @param size
	 * 		- level size (level's length)
	 * @param context
	 * 		 - OpenglES Context attribute
	 * @param loader
	 * 		 - Loader attribute
	 */
	public Level(int size, Context context, Loader loader, float[] projection) {
		this.projectionMatrix = projection;
		this.waterShader = new WaterShader(context);
		this.levelSize = size;
		this.generator = new LevelGenerator(loader, context);
		this.waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, context);
		generator.generateHash(levelSize, this);
		
		generator.generateLevel(size, this);	
		
		float waterPosX = -(LevelGenerator.TILE_OFFSET*4);
		float waterPosZ = -(LevelGenerator.TILE_OFFSET*4);
		
		for(int x=0; x < size; x++) {
			for(int z=0; z < size; z++) {
				waters.add(new Water(-10, waterPosX, waterPosZ));
				waterPosZ += LevelGenerator.TILE_OFFSET*4;
			}
			waterPosZ = -(LevelGenerator.TILE_OFFSET*4);
			waterPosX += LevelGenerator.TILE_OFFSET*4;
		}
	}
	
	/**
	 * Renders all of the data related to the current level
	 * @param renderer
	 * 		- Game renderer object
	 * @param camera
	 * 		- Global client Camera object
	 */
	public void renderScene(GameMasterRenderer renderer, Camera camera) {
		
		for(Integer index: tiles.keySet()) {
			renderer.processEntity(tiles.get(index).getEntity());
		}
		
		for(Actor actor: actors) {
			renderer.processEntity(actor.getEntity());
		}
		
		for(Item item: items) {
			renderer.processEntity(item.getEntity());
		}
		
		float[] centerXYZ = {center[0], LevelGenerator.LAND_HEIGHT, center[1]};
		renderer.render(centerXYZ, camera);
		waterRenderer.render(waters, camera, renderer.getCelestialRenderer().getLightSource());
	}
	
	/************** TILES ******************/
	
	/**
	 * Gets the tile clicked onto.
	 * @param point
	 * 		- click position
	 * @return tile clicked onto, will be null if misclicked
	 */
	public Tile spotTaggedTiles(float[] point) {
		Tile tagged = null;
		boolean isTagged = false;
		for(int index: tiles.keySet()) {
			if((point[0] <= tiles.get(index).getEntity().getPosition()[0]+10 &&
					point[0] >= tiles.get(index).getEntity().getPosition()[0]-10) && 
					(point[2] <= tiles.get(index).getEntity().getPosition()[2]+10 &&
					point[2] >= tiles.get(index).getEntity().getPosition()[2]-10) && !isTagged) {
				tiles.get(index).getEntity().setTagged(true);
				tagged = tiles.get(index);
				isTagged = true;				
			}				
			else tiles.get(index).getEntity().setTagged(false);
		}
		return tagged;
	}	
	
	/**
	 * Get tile by it's index
	 * @param index
	 * 		- index of the tile
	 * @return tile
	 */
	public Tile getTile(int index) {
		if(tiles.get(index)!=null)
			return tiles.get(index);
		else return null;
	}
	
	/**
	 * Get tile by a position (the tile which the point belongs to).
	 * @param x
	 * 		- X coordinate
	 * @param z
	 * 		- Z coordinate
	 * @return tile, null if misclicked
	 */
	public Tile getTile(float x, float z) {
		for(int index: tiles.keySet()) {
			if((x <= tiles.get(index).getEntity().getPosition()[0]+LevelGenerator.TILE_OFFSET &&
				x >= tiles.get(index).getEntity().getPosition()[0]-LevelGenerator.TILE_OFFSET) 
					&& 
				(z <= tiles.get(index).getEntity().getPosition()[2]+LevelGenerator.TILE_OFFSET &&
				z >= tiles.get(index).getEntity().getPosition()[2]-LevelGenerator.TILE_OFFSET))
			return tiles.get(index);
		}
		return null;
	}
				
	/**
	 * Calls the Open Tile method from Level Generator to unlock undiscovered tile.
	 * @param index
	 * 		- Tile index
	 */
	public void openTile(int index) {
		generator.openTile(index, this,
		tiles.get(index).getEntity().getPosition()[0], tiles.get(index).getEntity().getPosition()[2]);	
	}
	
	/**
	 * Set tile on the index position
	 * @param index
	 * 		- index of the tile
	 * @param tile 
	 * 		- new tile
	 */
	public void setTile(int index, Tile tile) {
		if(tiles.get(index)!=null)
			tiles.remove(index);
		tiles.put(index, tile);
	}
	
	/**
	 * Get the tile from hash by it's index
	 * @param index
	 * 		- index of the tile
	 * @return hash tile
	 */
	public int getHashTile(int index) {
		if(levelHash.get(index)!=null)
			return levelHash.get(index);
		else return 0;
	}
	
	/**
	 * Set tile to the hash on the index position
	 * @param index
	 * 		- index of the tile
	 * @param data
	 * 		- new hash tile
	 */
	public void setHashTile(int index, int data) {
		if(levelHash.get(index)!=null)
			levelHash.remove(index);
		levelHash.put(index, data);
	}

	/************** ACTORS AND ITEMS ******************/
	
	/**
	 * Get the actor clicked onto.
	 * @param point
	 * 		- click position [X, Z]
	 * @return actor clicked onto, will return null if misclicked
	 */
	public Actor spotTaggedActors(float[] point) {
		Actor tagged = null;
		for(Actor actor: actors) {
			if((point[0] <= actor.getEntity().getPosition()[0]+7 &&
				point[0] >= actor.getEntity().getPosition()[0]-7) && 
				(point[2] <= actor.getEntity().getPosition()[2]+7 &&
				point[2] >= actor.getEntity().getPosition()[2]-7)) {
				actor.getEntity().setTagged(true);
				tagged = actor;
			}				
			else actor.getEntity().setTagged(false);
		}
		return tagged;
	}
	
	/**
	 * Spawn the actor given on this level
	 * @param actor
	 * 		 - actor given
	 */
	public void spawn(Actor actor) {
		actors.add(actor);
	}
	
	/**
	 * Spawn the item given on this level
	 * @param item
	 * 		- item given
	 */
	public void spawn(Item item) {
		items.add(item);
	}
	
	/**
	 * Despawn an actor from this level
	 * @param actor
	 * 		- desired actor
	 */
	public void despawn(Actor actor) {
		if(actors.contains(actor))
			actors.remove(actor);
	}
	
	/**
	 * Despawn an item from this level
	 * @param item
	 * 		- desired item
	 */
	public void despawn(Item item) {
		if(items.contains(item))
			items.remove(item);
	}
	
	/**
	 * Adds an actor to his team's ship on board
	 * @param player
	 * 		- player who died and is about to respawn
	 */
	public void respawn(Player player) {
		for(Actor actor: actors) {
			if(actor instanceof Ship) {
				if(((Ship) actor).getTeam() == player.getTeam())
					((Ship) actor).increaseCrew();
			}
		}
	}
	
	/**
	 * @return list of actors on this level
	 */
	public List<Actor> getActors(){
		return actors;
	}
	
	public List<Item> getItems(){
		return items;
	}
	
	public Item getItem(Tile tile, int id) {
		Tile parent;
		for(Item item: items) {
			parent = getTile(item.getEntity().getPosition()[0],
					item.getEntity().getPosition()[2]);
			
			if(parent.getEntity().getPosition()[0] == tile.getEntity().getPosition()[0]
				&& parent.getEntity().getPosition()[2] == tile.getEntity().getPosition()[2])
				if(item.getId() == id)
					return item;
		}
		return null;
	}
	
	public Item getItem(Tile tile) {
		Tile parent;
		for(Item item: items) {
			parent = getTile(item.getEntity().getPosition()[0],
					item.getEntity().getPosition()[2]);
			if(parent.getEntity().getPosition()[0] == tile.getEntity().getPosition()[0]
				&& parent.getEntity().getPosition()[2] == tile.getEntity().getPosition()[2])
				return item;
		}
		return null;
	}
	
	/************** OTHER ******************/
	
	/**
	 * @return level size (level's length)
	 */
	public int getLevelSize() {
		return levelSize;
	}
	
	public void setProjectionMatrix(float[] projection) {
		this.projectionMatrix = projection;
		waterRenderer.setProjectionMatrix(projection);
	}

	public float[] getCenter() {
		return center;
	}

	public void setCenter(float x, float z) {
		center[0] = x;
		center[1] = z;
	}
}
