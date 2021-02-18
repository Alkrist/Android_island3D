package renderEngine.renderers;

import java.util.List;
import java.util.Map;

import android.opengl.GLES20;
import android.opengl.GLES30;
import renderEngine.AppTools;
import renderEngine.gameObjects.Entity;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.shaders.EntityShader;
import renderEngine.textures.Texture;

public class EntityRenderer {
	
	private EntityShader shader;
	
	public EntityRenderer(EntityShader shader, float[]projectionMatrix) {
		this.shader = shader;
		
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<Model, List<Entity>> entities) {
		for(Model model: entities.keySet()) {
			prepareModel(model);
			
			if(model.getTexture().isTransparent())
				GameMasterRenderer.disableCulling();
			
			List<Entity> batch = entities.get(model);
			for(Entity entity: batch) {
				prepareInstance(entity);
				GLES20.glDrawElements(GLES20.GL_TRIANGLES, model.getMesh().getVertexCount(), GLES20.GL_UNSIGNED_INT, 0);
			}
			unbindModel();
			GameMasterRenderer.enableCulling();
		}
	}
	
	private void prepareModel(Model model) {
		Mesh mesh = model.getMesh();
		Texture texture = model.getTexture();
		GLES30.glBindVertexArray(mesh.getVaoID());
    	GLES20.glEnableVertexAttribArray(0);
    	GLES20.glEnableVertexAttribArray(1);
    	GLES20.glEnableVertexAttribArray(2);
    	
    	shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
    	
    	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.getTextureID());
	}
	
	private void unbindModel() {
		GameMasterRenderer.enableCulling();
		
		GLES20.glDisableVertexAttribArray(0);
		GLES20.glDisableVertexAttribArray(1);
		GLES20.glDisableVertexAttribArray(2);
		GLES30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		float[] transformationMatrix = AppTools.createTransformationMatrix
    			(entity.getPosition()[0], entity.getPosition()[1],entity.getPosition()[2],
    					entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		if(entity.isTagged())
			shader.loadTagFeature(true);
		else shader.loadTagFeature(false);
	}
	
	public void setProjectionMatrix(float[] projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
}
