package renderEngine.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test.AppMasterRenderer;

import android.content.Context;
import android.opengl.GLES20;
import renderEngine.Loader;
import renderEngine.gameObjects.Camera;
import renderEngine.gameObjects.Entity;
import renderEngine.gameObjects.Light;
import renderEngine.models.Model;
import renderEngine.shaders.EntityShader;

public class GameMasterRenderer {

	//Background colour variables
	private static float RED_1 = 0.4f, RED_2 = 0.4f;
	private static float GREEN_1 = 0.7f, GREEN_2 = 0.7f; 
	private static float BLUE_1 = 1f, BLUE_2 = 1f;
	private static float blendFactor = 1;
	
	private float[] projectionMatrix;
	
	//Game Objects' Data
	private Map<Model, List<Entity>> entities = new HashMap<Model, List<Entity>>();
		
	//Shaders
	private EntityShader entityShader;
	
	//Renderers
	private EntityRenderer entityRenderer;
	private SkyboxRenderer skyboxRenderer;
	private CelestialRenderer celestialRednerer;
	private WorldGuiRenderer worldRenderer;
	
	public GameMasterRenderer(Loader loader, Context context, float[]projectionMatrix) {
		this.entityShader = new EntityShader(context);
		this.projectionMatrix = projectionMatrix;
		enableCulling();
		this.entityRenderer = new EntityRenderer(entityShader, projectionMatrix);
		this.skyboxRenderer = new SkyboxRenderer(context, loader, projectionMatrix);
		this.celestialRednerer = new CelestialRenderer(context, loader, projectionMatrix);
		this.worldRenderer = new WorldGuiRenderer(loader, context, projectionMatrix);
	}
	
	public void prepare() {
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
    	
    	GLES20.glClearColor(RED_1, GREEN_1, BLUE_1, 1);
	}
	
	
	public void render(float[] center, Camera camera) {		
		prepare();
		
		entityShader.start();
		entityShader.loadViewMatrix(camera);	
		entityShader.loadLight(celestialRednerer.getLightSource());
		entityShader.loadFogVariables(0.014f, 1.2f);
		entityShader.loadSkyColour(RED_1, GREEN_1, BLUE_1, RED_2, GREEN_2, BLUE_2);
		entityShader.loadBlendFactor(blendFactor);
		entityRenderer.render(entities);	
		entityShader.stop();
		
			
		skyboxRenderer.render(camera);
		celestialRednerer.render(camera, center);	
		if(AppMasterRenderer.getRunState() == AppMasterRenderer.RunState.PLAY)
			worldRenderer.render(AppMasterRenderer.getGame().getLevel().getActors(), camera);
		entities.clear();
				
	}
	
	public static void setSecondSkyColour(float r, float g, float b) {
		RED_2 = r;
		GREEN_2 = g;
		BLUE_2 = b;
	}
	
	public void resetDayNightCycle() {
		celestialRednerer.resetDayNightCycle();
	}
	
	public static void setFirstSkyColour(float r, float g, float b) {
		RED_1 = r;
		GREEN_1 = g;
		BLUE_1 = b;
	}
	
	public static float[] getSkyColour1() {
		float[] colour = {RED_1, GREEN_1, BLUE_1};
		return colour;
	}
	
	public static float[] getSkyColour2() {
		float[] colour = {RED_2, GREEN_2, BLUE_2};
		return colour;
	}
	
	public static float getBlendFactor() {
		return blendFactor;
	}
	
	public static void setBlendFactor(float blend) {
		blendFactor = blend;
	}
	
	public static void enableCulling() {
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glCullFace(GLES20.GL_BACK);
	}
	
	public static void disableCulling() {
		GLES20.glDisable(GLES20.GL_CULL_FACE);
	}
	
	public void cleanUp() {
		entityShader.cleanUp();
		worldRenderer.cleanUp();
		celestialRednerer.cleanUp();
		skyboxRenderer.cleanUp();
	}
	
	public void setProjectionMatrix(float[] projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
		entityRenderer.setProjectionMatrix(projectionMatrix);
		skyboxRenderer.setProjectionMatrix(projectionMatrix);
		celestialRednerer.setProjectionMatrix(projectionMatrix);
		worldRenderer.setProjectionMatrix(projectionMatrix);
	}
	
	public float[] getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public CelestialRenderer getCelestialRenderer() {
		return celestialRednerer;
	}
	
	//********** GAME OBJECT PROCESSORS BELOW **************
	
	public void processEntity(Entity entity) {
		Model model = entity.getModel();
		List<Entity> batch = entities.get(model);
		if(batch!=null)
			batch.add(entity);
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(model, newBatch);
		}
	}

}
