package renderEngine.renderers;


import java.util.List;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import renderEngine.AppTools;
import renderEngine.Loader;
import renderEngine.gameObjects.Camera;
import renderEngine.gameObjects.Light;
import renderEngine.gameObjects.Water;
import renderEngine.models.Mesh;
import renderEngine.shaders.WaterShader;

public class WaterRenderer {
	
	private static final float WAVE_STRENGTH = 0.003f;
	
	private Mesh quad;	
	private WaterShader shader;
	private int dudvMap;
	private int normalMap;
	private int water1, water2;
	private float moveFactor = 0;
	
	public WaterRenderer(Loader loader, WaterShader shader, float[] projectionMatrix, Context context) {
		
		this.shader = shader;
		this.dudvMap = loader.loadTexture(context, "waterDUDV.png");
		this.normalMap = loader.loadTexture(context, "waterN.png");
		this.water1 = loader.loadTexture(context, "water1.png");
		this.water2 = loader.loadTexture(context, "water2.png");
		shader.start();		
		shader.loadProjectionMatrix(projectionMatrix);
		shader.connectTextureUnits();
		shader.stop();
		
		setUpVAO(loader);
	}
	
	public void render(List<Water> waters, Camera camera, Light sun) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadLight(sun);
		
		moveFactor+= WAVE_STRENGTH;
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadFogVariables(0.017f, 1.5f);
		shader.loadBlendFactor(GameMasterRenderer.getBlendFactor());
		shader.loadSkyColour(GameMasterRenderer.getSkyColour1(), GameMasterRenderer.getSkyColour2());
		
		GLES30.glBindVertexArray(quad.getVaoID());
		GLES20.glEnableVertexAttribArray(0);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, dudvMap);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, normalMap);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, water1);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, water2);
		
		for(Water water: waters) {
			float[] modelMatrix = AppTools.createTransformationMatrix(water.getX(), 
					water.getHeight(), water.getZ(), 0, 0, 0, Water.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		
		GLES20.glDisableVertexAttribArray(0);
		GLES30.glBindVertexArray(0);
		
		shader.stop();
	}
	
	private void setUpVAO(Loader loader) {
		float vertices[] = {-1f, -1f, -1f, 
							1f, 1f, -1f,
							1f, -1f, -1f, 
							1f, 1f, 1f};
		
		quad = loader.loadToVAO(vertices, 2);
	}
	
	public void setProjectionMatrix(float[] projection) {
		shader.start();
		shader.loadProjectionMatrix(projection);	
		shader.stop();
	}
}
