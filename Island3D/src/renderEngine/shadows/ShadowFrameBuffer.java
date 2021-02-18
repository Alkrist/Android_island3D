package renderEngine.shadows;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import com.test.AppMasterRenderer;

import android.opengl.GLES20;
import android.opengl.GLES30;

public class ShadowFrameBuffer {

	private final int WIDTH;
	private final int HEIGHT;
	private int fbo;
	private int shadowMap;
	
	public ShadowFrameBuffer(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		initialiseFrameBuffer();
	}
	
	private void initialiseFrameBuffer() {
		fbo = createFrameBuffer();
		shadowMap = createDepthBufferAttachment(WIDTH, HEIGHT);
		unbindFrameBuffer();
	}
	
	private int createFrameBuffer() {
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES30.glGenFramebuffers(1, buffer);
		GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, buffer.get(0));
		
		buffer.clear();
		buffer.put(GLES20.GL_NONE);
		buffer.flip();
		
		GLES30.glDrawBuffers(1, buffer);
		GLES30.glReadBuffer(GLES20.GL_NONE);
		return buffer.get(0);
	}	
	
	private static int createDepthBufferAttachment(int width, int height) {
		int texture;
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES20.glGenTextures(1, buffer);
		texture = buffer.get(0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT16, width, height, 0, GLES20.GL_DEPTH_COMPONENT, 
				GLES20.GL_FLOAT, (ByteBuffer) null);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D,
				texture, 0);
		return texture;
	}
	
	private static void bindFrameBuffer(int frameBuffer, int width, int height) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GLES20.glViewport(0, 0, width, height);
	}
	
	public void bindFrameBuffer() {
		bindFrameBuffer(fbo, WIDTH, HEIGHT);
	}
	
	public void unbindFrameBuffer() {
		GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, AppMasterRenderer.Width, AppMasterRenderer.Height);
	}
	
	public int getShadowMap() {
		return shadowMap;
	}
	
	public void cleanUp() {
		IntBuffer buffer = IntBuffer.allocate(1);
		buffer.put(fbo);
		GLES30.glDeleteFramebuffers(1, buffer);
		buffer.clear();
		buffer.put(shadowMap);
		GLES20.glDeleteTextures(1, buffer);
	}
}
