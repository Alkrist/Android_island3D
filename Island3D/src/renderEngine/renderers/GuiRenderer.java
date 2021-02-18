package renderEngine.renderers;

import java.util.List;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import renderEngine.AppTools;
import renderEngine.Loader;
import renderEngine.models.Mesh;
import renderEngine.shaders.GuiShader;
import renderEngine.textures.GuiTexture;

public class GuiRenderer {

	private final Mesh quad;
	private GuiShader shader;
	
	public GuiRenderer(Loader loader, Context context) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader(context);
	}
	
	public void render(List<GuiTexture> guis) {
		shader.start();
		
		GLES30.glBindVertexArray(quad.getVaoID());
		GLES20.glEnableVertexAttribArray(0);
		
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		
		GameMasterRenderer.disableCulling();
		
		for(GuiTexture gui: guis) {
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, gui.getTexture());
			
			float[] transformationMatrix = 
					AppTools.createTransformationMatrix(gui.getPosition(), gui.getScale());
			if(gui.isReversed()) {
				Matrix.rotateM(transformationMatrix, 0, 180, 0, 1, 0);
				Matrix.rotateM(transformationMatrix, 0, 180, 0, 0, 1);
			}
				
			shader.loadTransformationMatrix(transformationMatrix);
			
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());	
			
		}
		
		GameMasterRenderer.enableCulling();
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisable(GLES20.GL_BLEND);
		
		GLES20.glDisableVertexAttribArray(0);
		GLES30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
