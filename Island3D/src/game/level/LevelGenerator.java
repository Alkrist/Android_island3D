package game.level;

import java.util.Random;

import android.content.Context;
import game.ModelBatch;
import game.entities.actors.Ship;
import game.entities.tiles.BlankTile;
import game.entities.tiles.ShoreTile;
import game.entities.tiles.TreasureTile;
import game.entities.tiles.UnknownTile;
import renderEngine.Loader;
import renderEngine.ObjLoader;
import renderEngine.gameObjects.Camera;
import renderEngine.models.Model;
import renderEngine.textures.Texture;

public class LevelGenerator {
	//11.982198f  5.991099f 70
	public static final float SCALE = 0.2f;
	public static final float TILE_OFFSET = 70 * SCALE; //The half length of the tile
	public static final float LAND_HEIGHT = -5;
	public static final float CORRECTION = TILE_OFFSET*0.03f;
	private Random random;
	
	private Model actorModel_ship;
	
	public LevelGenerator(Loader loader, Context context) {
		random = new Random();
		
		initModels(context, loader);
	}
	
	private void initModels(Context context, Loader loader) {
		actorModel_ship = new Model(ObjLoader.loadObj(context, loader, "cube.obj"),
				new Texture(loader.loadTexture(context, "test.png")));
	}
	
	public void generateHash(int size, Level level) {
		int location;
		
		level.setHashTile(random.nextInt(size*size), 4);
		
		for(int i=0; i < 2; i++) {
			location = generateLocation(size, level);
			level.setHashTile(location, 4);
		}
			
		for(int i=0; i < 3; i++) {
			location = generateLocation(size, level);
			level.setHashTile(location, 4);
		}
			
		for(int i=0; i < 4; i++) {
			location = generateLocation(size, level);
			level.setHashTile(location, 4);
		}
		
	}
	
	private int generateLocation(int size, Level level) {
		int index = random.nextInt(size*size);
		
		while(level.getHashTile(index)!=0) {
			index = random.nextInt(size*size);
		}
		return index;
	}
	
	public void openTile(int index, Level level, float posX, float posZ) {
		int type = level.getHashTile(index);
		Model model = null;
		int modelType = (int) (Math.random()*4);
		switch(modelType) {
		case 0:
			model = ModelBatch.tileModel_blank_1;
			break;
		case 1:
			model = ModelBatch.tileModel_blank_2;
			break;
		case 2:
			model = ModelBatch.tileModel_blank_3;
			break;
		case 3:
			model = ModelBatch.tileModel_blank_4;
			break;
		}
		switch(type) {
		case 2:
			level.setTile(index, new TreasureTile(model, posX, LAND_HEIGHT, posZ, 0, 0, 0, index, 2));
			break;
		case 3:
			level.setTile(index, new TreasureTile(model, posX, LAND_HEIGHT, posZ, 0, 0, 0, index, 3));
			break;
		case 4:
			level.setTile(index, new TreasureTile(model, posX, LAND_HEIGHT, posZ, 0, 0, 0, index, 4));
			break;
		case 5:
			level.setTile(index, new TreasureTile(model, posX, LAND_HEIGHT, posZ, 0, 0, 0, index, 5));
			break;
		default:
			level.setTile(index, new BlankTile(model, posX, LAND_HEIGHT, posZ, 0, 0, 0, index));
			break;
		}
	}
	
	/**
	 * The generator for "active" tiles - tiles will be displayed
	 * @param size
	 * 		- island's length
	 * @param level
	 * 		- Level object to load generated tiles to
	 */
	public void generateLevel(int size, Level level) {
		int index = 0;
		float posX = 0, posZ = 0;
		
		// **** MAIN TERRAIN GENERATOR ****
		
		for(int x = 0; x < size; x++) {
			for(int z = 0; z < size; z++) {
				level.setTile(index, new UnknownTile(ModelBatch.tileModel_unknown, posX, LAND_HEIGHT, posZ, 0, 0, 0, index));
				if(x==size/2 && z == size/2) {
					Camera.setCenterPosition(posX, LAND_HEIGHT+5, posZ);
					level.setCenter(posX, posZ);
				}
					
				posZ+=(TILE_OFFSET*2);
				index++;
			}
			posX+=(TILE_OFFSET*2);
			posZ=0;
		}
		
		// **** SHORE GENERATOR ****
		
		float posMax = (TILE_OFFSET*2) * size;
		int type;
		posX = 0;
		posZ = 0;
		index++;
		
		for(int x = 0; x < size; x++) {
			//Top row
			type = (int) (Math.random()*5);
			switch(type) {
			case 0:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_1, posMax, LAND_HEIGHT, posZ, 0, 180, 0, index));
				break;
			case 1:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_2, posMax, LAND_HEIGHT, posZ, 0, 180, 0, index));
				break;
			case 2:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_3, posMax, LAND_HEIGHT, posZ, 0, 180, 0, index));
				break;
			case 3:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_4, posMax, LAND_HEIGHT, posZ, 0, 180, 0, index));
				break;
			case 4:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_5, posMax, LAND_HEIGHT, posZ, 0, 180, 0, index));
				break;
			}
			if(x==size/2)
				level.spawn(new Ship((short) 0, actorModel_ship, posMax, LAND_HEIGHT, posZ, 0, 0, 0));
				
			
			//BottomRow
			index++;
			type = (int) (Math.random()*5);
			switch(type) {
			case 0:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_1, -(TILE_OFFSET*2)+CORRECTION, LAND_HEIGHT, posZ, 0, 0, 0, index));
				break;
			case 1:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_2, -(TILE_OFFSET*2)+CORRECTION, LAND_HEIGHT, posZ, 0, 0, 0, index));
				break;
			case 2:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_3, -(TILE_OFFSET*2)+CORRECTION, LAND_HEIGHT, posZ, 0, 0, 0, index));
				break;
			case 3:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_4, -(TILE_OFFSET*2)+CORRECTION, LAND_HEIGHT, posZ, 0, 0, 0, index));
				break;
			case 4:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_5, -(TILE_OFFSET*2)+CORRECTION, LAND_HEIGHT, posZ, 0, 0, 0, index));
				break;
			}
			if(x==size/2)
				level.spawn(new Ship((short) 1, actorModel_ship, -(TILE_OFFSET*2), LAND_HEIGHT, posZ, 0, 0, 0));
			
			posZ+=(TILE_OFFSET*2);
			index++;
		}
		posZ=0;
		for(int x = 0; x < size; x++) {
			//Left column
			type = (int) (Math.random()*5);
			switch(type) {
			case 0:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_1, posX, LAND_HEIGHT, -(TILE_OFFSET*2), 0, 270, 0, index));
				break;
			case 1:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_2, posX, LAND_HEIGHT, -(TILE_OFFSET*2), 0, 270, 0, index));
				break;
			case 2:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_3, posX, LAND_HEIGHT, -(TILE_OFFSET*2), 0, 270, 0, index));
				break;
			case 3:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_4, posX, LAND_HEIGHT, -(TILE_OFFSET*2), 0, 270, 0, index));
				break;
			case 4:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_5, posX, LAND_HEIGHT, -(TILE_OFFSET*2), 0,270, 0, index));
				break;
			}	
			/*if(x==size/2)
				level.spawn(new Ship((short) 2, actorModel_ship, posX, LAND_HEIGHT, -(TILE_OFFSET*2), 0, 0, 0));
			*/
			
			//Right column
			index++;
			type = (int) (Math.random()*5);
			switch(type) {
			case 0:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_1, posX, LAND_HEIGHT, posMax-CORRECTION, 0, 90, 0, index));
				break;
			case 1:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_2, posX, LAND_HEIGHT, posMax-CORRECTION, 0, 90, 0, index));
				break;
			case 2:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_3, posX, LAND_HEIGHT, posMax-CORRECTION, 0, 90, 0, index));
				break;
			case 3:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_4, posX, LAND_HEIGHT, posMax-CORRECTION, 0, 90, 0, index));
				break;
			case 4:
				level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreStright_5, posX, LAND_HEIGHT, posMax-CORRECTION, 0,90, 0, index));
				break;
			}
			//level.setTile(index, new ShoreTile(ModelBatch.tileModel_shore, posX, LAND_HEIGHT, posMax, 0, 90, 0, index));	
			/*if(x==size/2)
				level.spawn(new Ship((short) 3, actorModel_ship, posX, LAND_HEIGHT, posMax, 0, 0, 0));
			*/
			posX+=(TILE_OFFSET*2);
			index++;
		}
		//Bottom Left
		level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreCorner_1, -(TILE_OFFSET*2)+CORRECTION, LAND_HEIGHT, -(TILE_OFFSET*2), 0, 0, 0, index));
		index++;
		//Top Left
		level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreCorner_2, posMax, LAND_HEIGHT, -(TILE_OFFSET*2), 0, 270, 0, index));
		index++;
		//Top Right
		level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreCorner_1, posMax, LAND_HEIGHT, posMax-CORRECTION, 0, 180, 0, index));
		index++;
		//Bottom Right
		level.setTile(index, new ShoreTile(ModelBatch.tileModel_shoreCorner_2, -(TILE_OFFSET*2)+CORRECTION, LAND_HEIGHT, posMax, 0, 90, 0, index));
	}
	
}
