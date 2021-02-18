package renderEngine.shaders;

import android.content.Context;
import android.opengl.Matrix;
import renderEngine.AppTools;
import renderEngine.gameObjects.Camera;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "skybox.vs";
    private static final String FRAGMENT_FILE = "skybox.fs";
	
    private static final float ROTATION_SPEED = 0.5f; //degrees per second
    
    private float rotation = 0; //current rotation;
    
	public SkyboxShader(Context context) {
		super(VERTEX_FILE, FRAGMENT_FILE, context);
	}
	    
	private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_cubeMapDay;
    private int location_cubeMapNight;
    private int location_fogColour1;
    private int location_fogColour2;
    private int location_blendFactor;
    private int location_fogBlendFactor;
    
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");		
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_cubeMapDay = super.getUniformLocation("cubeMapDay");
        location_cubeMapNight = super.getUniformLocation("cubeMapNight");
        location_fogColour1 = super.getUniformLocation("fogColour1");
        location_fogColour2 = super.getUniformLocation("fogColour2");
        location_blendFactor = super.getUniformLocation("blendFactor");
        location_fogBlendFactor = super.getUniformLocation("fogBlendFactor");
	}
	
	public void connectTextureUnits() {
    	super.loadInt(location_cubeMapDay, 0);
    	super.loadInt(location_cubeMapNight, 1);
    }
	
	public void loadViewMatrix(Camera camera) {
		float[] matrix = AppTools.createSkyboxViewMatrix(camera);
		rotation+= ROTATION_SPEED;
		Matrix.rotateM(matrix, 0, (float) Math.toRadians(rotation), 0, 1, 0);
		super.loadMatrix(location_viewMatrix, matrix);	
	}
	
	public void loadProjectionMatrix(float[] projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadFogColour(float r1, float g1, float b1, float r2, float g2, float b2) {
		float[] colour1 = {r1, g1, b1};
		float[] colour2 = {r2, g2, b2};
		super.loadVector3f(location_fogColour1, colour1);
		super.loadVector3f(location_fogColour2, colour2);
	}
	
	public void loadBlendFactor(float blend, float fogBlend) {
		super.loadFloat(location_blendFactor, blend);
		super.loadFloat(location_fogBlendFactor, fogBlend);
	}
	
}
