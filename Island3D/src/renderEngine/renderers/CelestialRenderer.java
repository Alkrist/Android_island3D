package renderEngine.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import renderEngine.AppTools;
import renderEngine.Loader;
import renderEngine.gameObjects.Camera;
import renderEngine.gameObjects.Light;
import renderEngine.models.Mesh;
import renderEngine.shaders.CelestialShader;

public class CelestialRenderer {

	private static final float DISTANCE = (SkyboxRenderer.SIZE/2)-(SkyboxRenderer.SIZE/2)*0.667f;
	private static final float[] VERTICES = {-1, 1, -1, -1, 1, 1, 1, -1};
	
	private float theta = 5;
	private boolean day = false;
	
	//Celestial body texture and it's variables
	private Mesh quad;
	private int texture_Sun;
	private int texture_Moon;
	private float[] position = new float[3];
	private float scale = 2.5f;
	
	//Light source variables
	private Light lightSource;
	private float[] lightColour = {0.7f, 0.7f, 0.7f};
	private float[] lightPosition = new float[3];
	
	private CelestialShader shader;
	
	public CelestialRenderer(Context context, Loader loader, float[] projection) {
		this.quad = loader.loadToVAO(VERTICES, 2);
		this.texture_Moon = loader.loadTexture(context, "moon.png");
		this.texture_Sun = loader.loadTexture(context, "sun.png");
		this.lightSource = new Light(lightPosition, lightColour);
		this.shader = new CelestialShader(context);
		
		shader.start();
		shader.loadProjectionMatrix(projection);
		shader.connectTextureUnit();
		shader.stop();
	}
	
	public void render(Camera camera, float[] center) {
		calculatePositions(camera, center);
		float[] viewMatrix = AppTools.createViewMatrix(camera);
		
		shader.start();
		
		GLES30.glBindVertexArray(quad.getVaoID());
		GLES20.glEnableVertexAttribArray(0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glDepthMask(false);
		
		bindTexture();
		updateModelViewMatrix(viewMatrix);
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		
		GLES20.glDepthMask(true);
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisableVertexAttribArray(0);
		GLES30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void resetDayNightCycle() {
		position = new float[3];
		lightPosition = new float[3];
	}
	
	private void updateModelViewMatrix(float[] viewMatrix) {
		float[] modelMatrix = AppTools.createTransformationMatrix(position[0], position[1],
				position[2], 0, 0, 0, scale);
		
		modelMatrix[0] = viewMatrix[0];
		modelMatrix[1] = viewMatrix[4];
		modelMatrix[2] = viewMatrix[8];
		
		modelMatrix[4] = viewMatrix[1];
		modelMatrix[5] = viewMatrix[5];
		modelMatrix[6] = viewMatrix[9];
		
		modelMatrix[8] = viewMatrix[2];
		modelMatrix[9] = viewMatrix[6];
		modelMatrix[10] = viewMatrix[10];
		
		Matrix.scaleM(modelMatrix, 0, scale, scale, scale);
		float[] modelViewMatrix = new float[16];
		Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
		shader.loadModelViewMatrix(modelViewMatrix);
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void calculatePositions(Camera camera, float[] center) {
		if(Math.toDegrees(theta)>=Math.toDegrees(180)) {
			theta = 0;
			if(day) {
				lightColour[0] = 0.7f;
				lightColour[1] = 0.7f;
				lightColour[2] = 0.7f;
				lightSource.setColour(lightColour);
				
				day = false;
				scale = 2.5f;
			}				
			else {
				lightColour[0] = 0.956f;
				lightColour[1] = 1;
				lightColour[2] = 0.498f;
				lightSource.setColour(lightColour);
				
				day = true;	
				scale = 5;
			}
		}
		
		position[0] = (float) (camera.getCameraPosition()[0]+((DISTANCE/2) * Math.cos(Math.toRadians(theta))));		
		position[1] = (float) (camera.getCameraPosition()[1]+((DISTANCE/2) * Math.sin(Math.toRadians(theta))));
		position[2] = camera.getCameraPosition()[2];
		
		lightPosition[0] = (float) (center[0] + ((DISTANCE) * Math.cos(Math.toRadians(theta))));
		lightPosition[1] = (float) (center[1] + ((DISTANCE) * Math.sin(Math.toRadians(theta))));
		lightPosition[2] = center[2];
	
		lightSource.setPosition(lightPosition);
	
		if(day)
			theta += (180 / SkyboxRenderer.DAY_ACCELERATION);
		else theta += (180 / SkyboxRenderer.NIGHT_ACCELERATION);			
	}
	
	private void bindTexture() {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		if(day)			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_Sun);
		else
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_Moon);
	}

	public Light getLightSource() {
		return lightSource;
	}
	
	public void setProjectionMatrix(float[]projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
}
