package renderEngine.renderers;

import java.util.List;
import java.util.Map;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import renderEngine.AppTools;
import renderEngine.gameObjects.Entity;
import renderEngine.models.Mesh;
import renderEngine.models.Model;
import renderEngine.shaders.ShadowShader;

public class ShadowMapEntityRenderer {

	private float[] projectionViewMatrix;
	private ShadowShader shader;
	
	public ShadowMapEntityRenderer(ShadowShader shader, float[] projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}
	
	public void render(Map<Model, List<Entity>> entities) {
		for(Model model: entities.keySet()) {
			Mesh mesh = model.getMesh();
			bindMesh(mesh);
			for(Entity entity: entities.get(model)) {
				prepareInstance(entity);
				GLES20.glDrawElements(GLES20.GL_TRIANGLES,
						mesh.getVertexCount(), GLES20.GL_UNSIGNED_INT, 0);
			}
		}
		GLES20.glDisableVertexAttribArray(0);
		GLES30.glBindVertexArray(0);
	}
	
	private void bindMesh(Mesh mesh) {
		GLES30.glBindVertexArray(mesh.getVaoID());
		GLES20.glEnableVertexAttribArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		float[] modelMatrix = AppTools.createTransformationMatrix(entity.getPosition()[0],
				entity.getPosition()[1], entity.getPosition()[2], entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		float[] mvpMatrix = new float[16];
		Matrix.multiplyMM(mvpMatrix, 0, projectionViewMatrix, 0, modelMatrix, 0);
		shader.loadMvpMatrix(mvpMatrix);
	}
}
