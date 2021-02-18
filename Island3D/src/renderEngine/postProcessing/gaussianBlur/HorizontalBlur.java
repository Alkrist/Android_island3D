package renderEngine.postProcessing.gaussianBlur;

import android.content.Context;
import android.opengl.GLES20;
import renderEngine.renderers.ImageRenderer;
import renderEngine.shaders.HorizontalBlurShader;

public class HorizontalBlur {

	private ImageRenderer renderer;
	private HorizontalBlurShader shader;
	
	public HorizontalBlur(Context context, int targetFBOwidth, int targetFBOheight, int width, int height) {
		shader = new HorizontalBlurShader(context);
		renderer = new ImageRenderer(targetFBOwidth, targetFBOheight, width, height);
		
		shader.start();
		shader.loadTargetWidth(targetFBOwidth);
		shader.stop();
	}
	
	public void render(int texture) {
		shader.start();
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();	
	}
	
	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}
}
