package renderEngine.renderers;

import java.util.List;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import game.ModelBatch;
import game.entities.Actor;
import game.entities.actors.Player;
import game.entities.actors.Ship;
import renderEngine.AppTools;
import renderEngine.Loader;
import renderEngine.gameObjects.Camera;
import renderEngine.models.Mesh;
import renderEngine.shaders.WorldGuiShader;

public class WorldGuiRenderer {

	private static final float[] VERTICES = {-1, 1, -1, -1, 1, 1, 1, -1};
	private static final float SCALE = 2;
	
	private WorldGuiShader shader;
	private Mesh quad;

	public WorldGuiRenderer(Loader loader, Context context, float[] projection) {
		this.quad = loader.loadToVAO(VERTICES, 2);
		this.shader = new WorldGuiShader(context);
		
		shader.start();
		shader.loadProjectionMatrix(projection);
		shader.connectTextureUnit();
		shader.stop();
	}
	
	public void render(List<Actor>actors, Camera camera) {
		float[] viewMatrix = AppTools.createViewMatrix(camera);
		shader.start();
		
		GLES30.glBindVertexArray(quad.getVaoID());
		GLES20.glEnableVertexAttribArray(0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		//GLES20.glDepthMask(false);
		
		for(Actor actor: actors) {
			if(actor instanceof Ship || actor instanceof Player) {
				if(distanceTest(actor.getEntity().getPosition(), camera)) {
					GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
					if(actor instanceof Ship) {
						switch(((Ship)actor).getTeam()) {
						case 0:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_black);
							break;
						case 1:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_white);
							break;
						case 2:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_yellow);
							break;
						case 3:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_blue);
							break;
						}
					}else if(actor instanceof Player) {
						switch(((Player)actor).getTeam()) {
						case 0:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_black);
							break;
						case 1:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_white);
							break;
						case 2:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_yellow);
							break;
						case 3:
							GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ModelBatch.texture_flag_blue);
							break;
						}
					}
					updateModelViewMatrix(viewMatrix, actor.getEntity().getPosition());
					GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
				}
			}				
		}
		//GLES20.glDepthMask(true);
		GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisableVertexAttribArray(0);
		GLES30.glBindVertexArray(0);
		shader.stop();
	}
	
	private boolean distanceTest(float[] position, Camera camera) {
		float dx = position[0]-camera.getCameraPosition()[0];
		float dy = (position[1]+15)-camera.getCameraPosition()[1];
		float dz = position[2]-camera.getCameraPosition()[2];		
		float horizDistance = (float) Math.sqrt(dx*dx+dz*dz);
		float vertDistance = (float) Math.sqrt(dy*dy+horizDistance*horizDistance);
		
		if(vertDistance >= 70 || vertDistance <= 5)
			return false;
		return true;
	}
	
	private void updateModelViewMatrix(float[] viewMatrix, float[] position) {
		float[] modelMatrix = AppTools.createTransformationMatrix(position[0], position[1]+15,
				position[2], 0, 0, 0, SCALE);
		
		modelMatrix[0] = viewMatrix[0];
		modelMatrix[1] = viewMatrix[4];
		modelMatrix[2] = viewMatrix[8];
		
		modelMatrix[4] = viewMatrix[1];
		modelMatrix[5] = viewMatrix[5];
		modelMatrix[6] = viewMatrix[9];
		
		modelMatrix[8] = viewMatrix[2];
		modelMatrix[9] = viewMatrix[6];
		modelMatrix[10] = viewMatrix[10];
		
		Matrix.scaleM(modelMatrix, 0, SCALE, SCALE, SCALE);
		float[] modelViewMatrix = new float[16];
		Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
		shader.loadModelViewMatrix(modelViewMatrix);
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	public void setProjectionMatrix(float[]projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
}
