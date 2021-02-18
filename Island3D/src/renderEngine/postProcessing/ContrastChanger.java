package renderEngine.postProcessing;

import android.content.Context;
import android.opengl.GLES20;
import renderEngine.renderers.ImageRenderer;
import renderEngine.shaders.ContrastShader;

public class ContrastChanger {

	private ContrastShader shader;
	private ImageRenderer renderer;
	private float contrast = 0.3f;
	
	public ContrastChanger(Context context) {
		shader = new ContrastShader(context);
		renderer = new ImageRenderer();
	}
	
	public void render(int texture) {
		shader.start();
		shader.loadContrastMultiplyer(contrast);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
		renderer.cleanUp();
	}
}
