package game;

import android.content.Context;
import renderEngine.Loader;
import renderEngine.ObjLoader;
import renderEngine.fonts.fontMeshCreator.FontType;
import renderEngine.models.Model;
import renderEngine.textures.Texture;

/**
 * The container static class for all models used in the game.
 * @author Mikhail
 */
public class ModelBatch {

	public static FontType font_Candara;
	
	public static Model playerModel;
	public static Model goldModel;
	
	public static Model tileModel_unknown;
	public static Model tileModel_shoreStright_1;
	public static Model tileModel_shoreStright_2;
	public static Model tileModel_shoreStright_3;
	public static Model tileModel_shoreStright_4;
	public static Model tileModel_shoreStright_5;
	public static Model tileModel_shoreCorner_1;
	public static Model tileModel_shoreCorner_2;
	public static Model tileModel_blank_1;
	public static Model tileModel_blank_2;
	public static Model tileModel_blank_3;
	public static Model tileModel_blank_4;
	public static Model tileModel_treasure;
	
	public static int texture_button1_idle;
	public static int texture_button1_pressed;
	public static int texture_button2_idle;
	public static int texture_button2_pressed;
	public static int texture_button3_idle;
	public static int texture_button3_pressed;
	public static int texture_scoreBoard;
	public static int texture_mainTitle;
	public static int texture_mainBackground;
	public static int texture_flag_white;
	public static int texture_flag_black;
	public static int texture_flag_yellow;
	public static int texture_flag_blue;
	/**
	 * Initialize all of the models in the batch.
	 * @param context
	 * 		- Android context attribute
	 * @param loader
	 * 		- Loader attribute
	 */
	public static void init(Context context, Loader loader) {
		playerModel = new Model(ObjLoader.loadObj(context, loader, "cross.obj"),
				new Texture(loader.loadTexture(context, "test2.png")));
		goldModel = new Model(ObjLoader.loadObj(context, loader, "stone.obj"),
				new Texture(loader.loadTexture(context, "stone.png")));
		
		//Terra incognita
		tileModel_unknown = new Model(ObjLoader.loadObj(context, loader, "tiles/land_undiscovered.obj"),
				new Texture(loader.loadTexture(context, "tiles/land_undiscovered.png")));
		//Shores
		tileModel_shoreStright_1 = new Model(ObjLoader.loadObj(context, loader, "tiles/shore_stright01.obj"),
				new Texture(loader.loadTexture(context, "tiles/shore_stright0102.png")));
		tileModel_shoreStright_2 = new Model(ObjLoader.loadObj(context, loader, "tiles/shore_stright02.obj"),
				new Texture(loader.loadTexture(context, "tiles/shore_stright0102.png")));
		tileModel_shoreStright_3 = new Model(ObjLoader.loadObj(context, loader, "tiles/shore_stright03.obj"),
				new Texture(loader.loadTexture(context, "tiles/shore_stright03.png")));
		tileModel_shoreStright_4 = new Model(ObjLoader.loadObj(context, loader, "tiles/shore_stright04.obj"),
				new Texture(loader.loadTexture(context, "tiles/shore_stright04.png")));
		tileModel_shoreStright_5 = new Model(ObjLoader.loadObj(context, loader, "tiles/shore_stright05.obj"),
				new Texture(loader.loadTexture(context, "tiles/shore_stright05.png")));
		tileModel_shoreCorner_1 = new Model(ObjLoader.loadObj(context, loader, "tiles/shore_corner01.obj"),
				new Texture(loader.loadTexture(context, "tiles/shore_corner01.png")));
		tileModel_shoreCorner_2 = new Model(ObjLoader.loadObj(context, loader, "tiles/shore_corner02.obj"),
				new Texture(loader.loadTexture(context, "tiles/shore_corner02.png")));
		
		//Blank land
		tileModel_blank_1 = new Model(ObjLoader.loadObj(context, loader, "tiles/land_blank01.obj"),
				new Texture(loader.loadTexture(context, "tiles/land_blank01.png")));
		tileModel_blank_2 = new Model(ObjLoader.loadObj(context, loader, "tiles/land_blank02.obj"),
				new Texture(loader.loadTexture(context, "tiles/land_blank02.png")));
		tileModel_blank_3 = new Model(ObjLoader.loadObj(context, loader, "tiles/land_blank03.obj"),
				new Texture(loader.loadTexture(context, "tiles/land_blank03.png")));
		tileModel_blank_4 = new Model(ObjLoader.loadObj(context, loader, "tiles/land_blank04.obj"),
				new Texture(loader.loadTexture(context, "tiles/land_blank04.png")));
		
		texture_button1_idle = loader.loadTexture(context, "button_idle.png");
		texture_button1_pressed = loader.loadTexture(context, "button_pressed.png");
		texture_button2_idle = loader.loadTexture(context, "pause_idle.png");
		texture_button2_pressed = loader.loadTexture(context, "pause_pressed.png");
		texture_scoreBoard = loader.loadTexture(context, "drop_down.png");
		texture_flag_white = loader.loadTexture(context, "flag_white.png");
		texture_flag_black = loader.loadTexture(context, "flag_black.png");
		texture_flag_yellow = loader.loadTexture(context, "flag_yellow.png");
		texture_flag_blue = loader.loadTexture(context, "flag_blue.png");
		texture_button3_idle = loader.loadTexture(context, "stats_idle.png");
		texture_button3_pressed = loader.loadTexture(context, "stats_pressed.png");
		texture_mainTitle = loader.loadTexture(context, "menus/title.png");
		texture_mainBackground = loader.loadTexture(context, "menus/background.png");
	}
	
	public static void initFonts(Context context, Loader loader, int width, int height) {
		font_Candara = new FontType(loader.loadTexture(context, "candara.png"), "candara.fnt", context, width, height);
	}
	
	/**
	 * Destroys all of the models in the batch.
	 */
	public static void cleanUp() {
		playerModel = null;
		goldModel = null;
	}
}
