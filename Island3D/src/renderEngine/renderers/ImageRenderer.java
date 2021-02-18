package renderEngine.renderers;

import android.opengl.GLES20;
import renderEngine.postProcessing.Fbo;

public class ImageRenderer {

	private Fbo fbo;

	public ImageRenderer(int width, int height, int displayWidth, int displayHeight) {
		this.fbo = new Fbo(width, height, displayWidth, displayHeight, Fbo.NONE);
	}

	public ImageRenderer() {}

	public void renderQuad() {
		if (fbo != null)
			fbo.bindFrameBuffer();
		
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
		
		if (fbo != null)
			fbo.unbindFrameBuffer();
	}

	public int getOutputTexture() {
		return fbo.getColourTexture();
	}

	public void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}
	
}
