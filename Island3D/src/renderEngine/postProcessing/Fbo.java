package renderEngine.postProcessing;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES20;
import android.opengl.GLES30;

public class Fbo {

	public static final int NONE = 0;
	public static final int DEPTH_TEXTURE = 1;
	public static final int DEPTH_RENDER_BUFFER = 2;

	private int width = 320;
	private int height = 180;
	private int displayWidth;
	private int displayHeight;
	
	private int frameBuffer;

	private int colourTexture;
	private int depthTexture;

	private int depthBuffer;
	private int colourBuffer;
	
	
	public Fbo(int width, int height, int displayWidth, int displayHeight, int depthBufferType) {
		this.width = width;
		this.height = height;
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		
		initialiseFrameBuffer(depthBufferType);
	}
	
	private void initialiseFrameBuffer(int type) {
		createFrameBuffer();
		createTextureAttachment();
		
		if (type == DEPTH_RENDER_BUFFER)
			createDepthBufferAttachment();
		else if (type == DEPTH_TEXTURE)
			createDepthTextureAttachment();
		
		unbindFrameBuffer();
	}
	
	private void createFrameBuffer() {
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES30.glGenFramebuffers(1, buffer);
		frameBuffer = buffer.get(0);
		
		buffer.clear();
		buffer.put(GLES30.GL_COLOR_ATTACHMENT0);
		buffer.flip();
		GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBuffer);
		GLES30.glDrawBuffers(1, buffer);
	}
	
	private void createTextureAttachment() {
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES20.glGenTextures(1, buffer);
		colourTexture = buffer.get(0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, colourTexture);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES30.GL_RGBA8, width, height, 0, GLES20.GL_RGBA, 
				GLES20.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
				colourTexture, 0);
	}
	
	private void createDepthTextureAttachment() {
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES20.glGenTextures(1, buffer);
		depthTexture = buffer.get(0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTexture);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES30.GL_DEPTH_COMPONENT24, width, height, 0, GLES20.GL_DEPTH_COMPONENT, 
				GLES20.GL_FLOAT, (ByteBuffer) null);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D,
				depthTexture, 0);
	}

	private void createDepthBufferAttachment() {
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES30.glGenRenderbuffers(1, buffer);
		depthBuffer = buffer.get(0);
		GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, depthBuffer);
		GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, GLES30.GL_DEPTH_COMPONENT24, width, height);
		GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER, depthBuffer);
	}
	
	public void bindFrameBuffer() {
		GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, frameBuffer);
		GLES20.glViewport(0, 0, width, height);
	}
	
	public void unbindFrameBuffer() {
		GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
		GLES20.glViewport(0, 0, displayWidth, displayHeight);
	}
	
	public void bindToRead() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, frameBuffer);
		GLES30.glReadBuffer(GLES30.GL_COLOR_ATTACHMENT0);
	}
		
	public void cleanUp() {
		IntBuffer buffer = IntBuffer.allocate(1);
		buffer.put(frameBuffer);
		GLES30.glDeleteFramebuffers(1, buffer);
		buffer.clear();
		buffer.put(colourTexture);
		GLES20.glDeleteTextures(1, buffer);
		buffer.clear();
		buffer.put(depthTexture);
		GLES20.glDeleteTextures(1, buffer);
		buffer.clear();
		buffer.put(depthBuffer);
		GLES30.glDeleteRenderbuffers(1, buffer);
		buffer.clear();
		buffer.put(colourBuffer);
		GLES30.glDeleteRenderbuffers(1, buffer);
	}	
	
	
	/******* GETTERS AND SETTERS*******/
	public int getColourTexture() {
		return colourTexture;
	}
	
	public int getDepthTexture() {
		return depthTexture;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
