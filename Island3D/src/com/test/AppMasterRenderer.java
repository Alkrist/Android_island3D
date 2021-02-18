package com.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import game.Game;
import game.ModelBatch;
import menus.MenuHandler;
import menus.components.AbstractButton;
import renderEngine.Loader;
import renderEngine.RayCaster;
import renderEngine.fonts.TextMaster;
import renderEngine.gameObjects.Camera;
import renderEngine.postProcessing.Fbo;
import renderEngine.postProcessing.PostProcessing;
import renderEngine.renderers.GameMasterRenderer;
import renderEngine.renderers.MenuMasterRenderer;

public class AppMasterRenderer implements GLSurfaceView.Renderer{
	//TODO: fix the pause screen issue
	//TODO: fix the pause crash issue
	//TODO: icon on treasure stack with amount of treasure in it
	
	//Constants
	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.01f;
	public static final float FAR_PLANE = 1000; 
	public static enum RunState  { MENU, PLAY, PAUSE, ENDGAME, SETTINGS; }
	public static int Width, Height;
	//Main variables
	private static Context context; 
	private static float[] projectionMatrix = new float[16];
	private static Loader loader = new Loader();
	private static Camera camera;
	private RayCaster caster;
	
	//Application run states
	private static RunState state = RunState.MENU;
	private static RunState currentMenu;
		
	//Master renderers
	private GameMasterRenderer gameRenderer;	
	private MenuMasterRenderer menuRenderer;
		
	//Game variables
	private static Game game;	
	private Fbo fbo;
	private boolean isPostProcessingInitialized = false;	
	private static int levelSize = 5;
	
	/**
	 * The constructor for static variables
	 * @param theContext
	 * 		- Context parameter
	 * @param theCamera
	 * 		- Camera parameter
	 */
	public AppMasterRenderer(Context theContext, Camera theCamera) {
		context = theContext;
		camera = theCamera;		
	}

	/******** MAIN METHODS: CREATE SURFACE, DRAW SURFACE, CHANGE SURFACE *******/
	/*  ORDER OF METHOD EXECUTION:
	 * 1) Constructor
	 * 2) onSurfaceCreated
	 * 3) onSurfaceChanged
	 * 4) onDrawFrame
	 */
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		TextMaster.init(loader, context);
        ModelBatch.init(context, loader);
        MenuHandler.setLoader(loader, context);
        gameRenderer = new GameMasterRenderer(loader, context, projectionMatrix);
        menuRenderer = new MenuMasterRenderer(loader, context, projectionMatrix);
        caster = new RayCaster(camera, projectionMatrix); 
        game = new Game(5, 2, projectionMatrix, context, loader);
    }

	@Override
    public void onDrawFrame(GL10 unused) {
		MenuHandler.update();
		//Defines the current run state
		switch(state) {
		case MENU:
			if(currentMenu != RunState.MENU) {
				MenuHandler.createMainMenu();
				currentMenu = RunState.MENU;
			}
			break;
		case PLAY:
			if(currentMenu != RunState.PLAY) {
				MenuHandler.createGameMenu();
				currentMenu = RunState.PLAY;
			}
			
			fbo.bindFrameBuffer();
			game.getLevel().renderScene(gameRenderer, camera);	
			fbo.unbindFrameBuffer();	
			PostProcessing.doPostProcessing(fbo.getColourTexture());
			
			break;
		case PAUSE:
			if(currentMenu != RunState.PAUSE) {
				MenuHandler.createPauseMenu();
				currentMenu = RunState.PAUSE;
			}
			break;
		case SETTINGS:
			if(currentMenu != RunState.SETTINGS) {
				MenuHandler.createSettingsMenu();
				currentMenu = RunState.SETTINGS;
			}
			break;
		case ENDGAME:
			if(currentMenu != RunState.ENDGAME) {
				MenuHandler.createScoreBoard();
				currentMenu = RunState.ENDGAME;
			}
			break;
		}
		//Renders the menu the last
		if(state == RunState.PLAY)
			menuRenderer.renderInGame(game.getTeam());
		else menuRenderer.RenderNoGame();
    }

	@Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {	
		Width = width;
		Height = height;
		if(fbo==null)
			fbo = new Fbo(width, height, width, height, Fbo.DEPTH_RENDER_BUFFER);	
		if(!isPostProcessingInitialized) {
			PostProcessing.init(loader, context, width, height);
			isPostProcessingInitialized = true;
		}
		
        ModelBatch.initFonts(context, loader, width, height);
       
        //Update projectionMatrix here
		createProjectionMatrix(width, height);
		gameRenderer.setProjectionMatrix(projectionMatrix);
        game.setProjectionMatrix(projectionMatrix);
        caster.setValues(projectionMatrix, width, height);
        
        //Defines the current state after pause
        //TODO: WARNING: may not be necessary
        switch(state) {
        	case MENU:
        		MenuHandler.createMainMenu();
        		break;
        	case PAUSE:
        		MenuHandler.createPauseMenu();
        		break;
        	case SETTINGS:
        		MenuHandler.createSettingsMenu();
        		break;
	        case PLAY:
	        	MenuHandler.createGameMenu();
	        	break;
	        case ENDGAME:
	        	MenuHandler.createScoreBoard();
	        	break;
	        default:
	        	MenuHandler.createMainMenu();
	        	break;
        }
        	
    }


	private void createProjectionMatrix(int width, int height) {
		
    	float aspectRatio = (float) width / height;
    	float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix[0] = x_scale;
		projectionMatrix[5] = y_scale;
		projectionMatrix[10] = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix[11] = -1;
		projectionMatrix[14] = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix[15] = 0;
    }
	
	public void checkIntersection(float x, float y, int width, int height) {
		AbstractButton button = MenuHandler.checkButtonPressed(caster.UICast(x, y, width, height));
		if(button!=null)
			button.onClick();
		else {
			caster.cast(x, y, width, height, this);
			float[] position = caster.getPointingPosition();
			if(position!=null)
				game.onClick(position);
		}				
	}
	
	/******* GETTERS AND SETTERS ********/
	public static Game getGame() {
		return game;
	}

	public static RunState getRunState() {
		return state;
	}
	
	public static void setLevelSize(int size) {
		levelSize = size;
	}
	
	public static int getLevelSize() {
		return levelSize;
	}
	
	/******* RUN STATE SETTERS *******/
	public static void runGame() {
		state = RunState.PLAY;
		game.createNewLevel(levelSize, 2, context, loader);
	}
	
	public static void pause() {
		state = RunState.PAUSE;
	}
	
	public static void settings() {
		state = RunState.SETTINGS;
	}
	
	public static void mainMenu() {
		state = RunState.MENU;
	}
	
	public static void endGame() {
		state = RunState.ENDGAME;
	}
	
	/******** CLEAN UP ********/
	public void cleanUp() {
		gameRenderer.cleanUp();
		menuRenderer.cleanUp();
	}
}

