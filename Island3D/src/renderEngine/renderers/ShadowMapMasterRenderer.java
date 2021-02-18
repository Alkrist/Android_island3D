package renderEngine.renderers;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import renderEngine.AppTools;
import renderEngine.gameObjects.Camera;
import renderEngine.gameObjects.Entity;
import renderEngine.gameObjects.Light;
import renderEngine.models.Model;
import renderEngine.shaders.ShadowShader;
import renderEngine.shadows.ShadowBox;
import renderEngine.shadows.ShadowFrameBuffer;

public class ShadowMapMasterRenderer {

	private static final int SHADOW_MAP_SIZE = 2048;

	private ShadowFrameBuffer shadowFbo;
	private ShadowShader shader;
	private ShadowBox shadowBox;
	
	private float[] projectionMatrix = new float[16];
	private float[] lightViewMatrix = new float[16];
	private float[] projectionViewMatrix = new float[16];
	private float[] offset = createOffset();

	private ShadowMapEntityRenderer entityRenderer;
	
	public ShadowMapMasterRenderer(Context context, Camera camera) {
		shader = new ShadowShader(context);
		shadowBox = new ShadowBox(lightViewMatrix, camera);
		shadowFbo = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
		entityRenderer = new ShadowMapEntityRenderer(shader, projectionViewMatrix);
	}
	
	/********* PREPARE AND RENDER MODULE *********/
	
	public void render(Map<Model, List<Entity>> entities, Light sun) {
		shadowBox.update();
		float[] sunPosition = sun.getPosition();
		float[] lightDirection = {
				-sunPosition[0],
				-sunPosition[1],
				-sunPosition[2]
		};
		prepare(lightDirection, shadowBox);
		entityRenderer.render(entities);
		finish();
	}
	
	private void prepare(float[] lightDirection, ShadowBox box) {
		updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
		updateLightViewMatrix(lightDirection, box.getCenter());
		Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, lightViewMatrix, 0);
		shadowFbo.bindFrameBuffer();
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);
		shader.start();
	}
	
	private void finish() {
		shader.stop();
		shadowFbo.unbindFrameBuffer();
	}
	
	
	/********* MATRIX DEFENITION MODULE *********/
	
	private void updateLightViewMatrix(float[] direction, float[] center) {
		direction = AppTools.normalizeVector3f(direction);
		center[0] = -center[0];
		center[1] = -center[1];
		center[2] = -center[2];
		Matrix.setIdentityM(lightViewMatrix, 0);
		float pitch = (float) Math.acos(Math.sqrt(direction[0]*direction[0]+direction[2]*direction[2]));
		Matrix.rotateM(lightViewMatrix, 0, pitch, 1, 0, 0);
		float yaw = (float) Math.toDegrees(((float) Math.atan(direction[0] / direction[2])));
		yaw = direction[2] > 0 ? yaw - 180 : yaw;
		Matrix.rotateM(lightViewMatrix, 0, -yaw, 0, 1, 0);
		Matrix.translateM(lightViewMatrix, 0, center[0], center[1], center[2]);
	}
	
	private void updateOrthoProjectionMatrix(float width, float height, float length) {
		Matrix.setIdentityM(projectionMatrix, 0);
		projectionMatrix[0] = 2f / width;
		projectionMatrix[5] = 2f / height;
		projectionMatrix[10] = -2f / length;
		projectionMatrix[15] = 1;
	}
	
	private static float[] createOffset() {
		float[] offset = new float[16];
		Matrix.translateM(offset, 0, 0.5f, 0.5f, 0.5f);
		Matrix.scaleM(offset, 0, 0.5f, 0.5f, 0.5f);
		return offset;
	}
	
	/********* GETTERS, SETTERS, CLEAN UP *********/
	
	public float[] getToShadowMapSpaceMatrix() {
		float[] matrix = new float[16];
		Matrix.multiplyMM(matrix, 0, offset, 0, projectionViewMatrix, 0);
		return matrix;
	}
	
	public int getShadowMap() {
		return shadowFbo.getShadowMap();
	}
	
	public float[] getLightSpaceTransform() {
		return lightViewMatrix;
	}
	
	public void cleanUp() {
		shader.cleanUp();
		shadowFbo.cleanUp();
	}
}
