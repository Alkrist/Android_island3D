package renderEngine.renderers;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import renderEngine.Loader;
import renderEngine.gameObjects.Camera;
import renderEngine.models.Mesh;
import renderEngine.shaders.SkyboxShader;

public class SkyboxRenderer {
	
	public static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
		    -SIZE,  SIZE, -SIZE,
		    -SIZE, -SIZE, -SIZE,
		    SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE, -SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,

		    -SIZE, -SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE,
		    -SIZE, -SIZE,  SIZE,

		    -SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE, -SIZE,
		     SIZE,  SIZE,  SIZE,
		     SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE,  SIZE,
		    -SIZE,  SIZE, -SIZE,

		    -SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE, -SIZE,
		     SIZE, -SIZE, -SIZE,
		    -SIZE, -SIZE,  SIZE,
		     SIZE, -SIZE,  SIZE
		};
	
	private static final float TIME_ACCELERATION = 10;
	private static String[] TEXTURE_FILES = {"left.png","right.png","bottom.png","top.png", "back.png","front.png"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightLeft.png","nightRight.png","nightBottom.png","nightTop.png","nightBack.png","nightFront.png"};
	
	public static final float NIGHT_ACCELERATION = 5000 / TIME_ACCELERATION;
	public static final float DAY_ACCELERATION = (24000 - 5000) / TIME_ACCELERATION;
	
	private Mesh cube;
	private int texture_day;
	private int texture_night;
	
	private SkyboxShader shader;
	
	public SkyboxRenderer(Context context, Loader loader, float[] projectionMatrix) {
		this.cube = loader.loadToVAO(VERTICES, 3);
		this.texture_day = loader.loadCubeMapTexture(context, TEXTURE_FILES);
		this.texture_night = loader.loadCubeMapTexture(context, NIGHT_TEXTURE_FILES);
		this.shader = new SkyboxShader(context);
		
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		
		GLES30.glBindVertexArray(cube.getVaoID());
		GLES20.glEnableVertexAttribArray(0);
		
		bindTextures();
		
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, cube.getVertexCount());
		
		GLES20.glDisableVertexAttribArray(0);
		GLES30.glBindVertexArray(0);
		
		shader.stop();
	}
	
	private float time = 0;
	private void bindTextures() {
		time += TIME_ACCELERATION;
		time %= 24000;
		
		int texture1;
		int texture2;
		//RGB1 - first day time's fog colour, RGB2 - second day time's fog colour
		float R1, R2;
		float G1, G2;
		float B1, B2;
		
		float blendFactor;	
		float fogBlendFactor;
		
		//If it's night
		if(time >= 0 && time < 5000){
			texture1 = texture_night;
			texture2 = texture_night;
			
			R1 = R2 = 0.05f;
			G1 = G2 = 0.03f;
			B1 = B2 = 0.16f;
			
			blendFactor = (time - 0)/(5000 - 0);
			
			fogBlendFactor = (time - 0)/(2500 - 0);
			
		//If it's morning
		}else if(time >= 5000 && time < 8000){
			texture1 = texture_night;
			texture2 = texture_day;
						
			blendFactor = (time - 5000)/(8000 - 5000);
			
			if(time <= 6500) {
				R1 = 0.05f; R2 = 0.5f;
				G1 = 0.03f; G2 = 0.17f;
				B1 = 0.16f; B2 = 0.24f;
				fogBlendFactor = (time - 5000)/(6500 - 5000);
			}else {
				R1 = 0.5f; R2 = 0.4f;
				G1 = 0.17f; G2 = 0.7f;
				B1 = 0.24f; B2 = 1f;
				fogBlendFactor = (time - 6500)/(8000 - 6500);
			}
						
		//If it's day
		}else if(time >= 8000 && time < 21000){
			texture1 = texture_day;
			texture2 = texture_day;
			
			R1 = R2 = 0.4f;
			G1 = G2 = 0.7f;
			B1 = B2 = 1f;
			
			blendFactor = (time - 8000)/(21000 - 8000);
			fogBlendFactor = (time - 8000)/(14500 - 8000);
			
		//If it's twilight
		}else{
			texture1 = texture_day;
			texture2 = texture_night;
						 
			blendFactor = (time - 21000)/(24000 - 21000);
			
			if(time <= 22500) {
				R1 = 0.4f; R2 = 0.5f;
				G1 = 0.7f; G2 = 0.1f;
				B1 = 1f; B2 = 0.03f;
				fogBlendFactor = (time - 21000)/(22500 - 21000);
			}else {
				R1 = 0.5f; R2 = 0.05f;
				G1 = 0.1f; G2 = 0.03f;
				B1 = 0.03f; B2 = 0.16f;
				fogBlendFactor = (time - 22500)/(24000 - 22500);
			}
			
		}
		GameMasterRenderer.setFirstSkyColour(R1, G1, B1);
		GameMasterRenderer.setSecondSkyColour(R2, G2, B2);
		GameMasterRenderer.setBlendFactor(fogBlendFactor);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texture1);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadFogColour(R1, G1, B1, R2, G2, B2);
		shader.loadBlendFactor(blendFactor, fogBlendFactor);
	}
	
	public void setProjectionMatrix(float[] projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
