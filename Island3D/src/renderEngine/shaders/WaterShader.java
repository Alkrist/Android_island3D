package renderEngine.shaders;

import android.content.Context;
import renderEngine.AppTools;
import renderEngine.gameObjects.Camera;
import renderEngine.gameObjects.Light;

public class WaterShader extends ShaderProgram{

	private static final String VERTEX_SHADER = "water.vs";
	private static final String FRAGMENT_SHADER = "water.fs";
	
	public WaterShader(Context context) {
		super(VERTEX_SHADER, FRAGMENT_SHADER, context);
	}

	private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_modelMatrix;
    private int location_dudvMap;
    private int location_normalMap;
    private int location_moveFactor;
    private int location_lightPosition;
    private int location_cameraPosition;
    private int location_lightColour;
    private int location_water1;
    private int location_water2;
    private int location_skyColour1;
	private int location_skyColour2;
	private int location_blendFactor;
	private int location_density;
	private int location_gradient;
    
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_dudvMap = super.getUniformLocation("dudvMap");
		location_normalMap = super.getUniformLocation("normalMap");
		location_moveFactor = super.getUniformLocation("moveFactor");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_cameraPosition = super.getUniformLocation("cameraPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_water1 = super.getUniformLocation("water1");
		location_water2 = super.getUniformLocation("water2");
		location_skyColour1 = super.getUniformLocation("skyColour1");
		location_skyColour2 = super.getUniformLocation("skyColour2");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_density = super.getUniformLocation("density");
		location_gradient = super.getUniformLocation("gradient");
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_dudvMap, 0);
		super.loadInt(location_normalMap, 1);
		super.loadInt(location_water1, 2);
		super.loadInt(location_water2, 3);
	}
	
	public void loadFogVariables(float density, float gradient) {
		super.loadFloat(location_density, density);
		super.loadFloat(location_gradient, gradient);
	}
	
	public void loadSkyColour(float[] colour1, float[] colour2) {
		super.loadVector3f(location_skyColour1, colour1);
		super.loadVector3f(location_skyColour2, colour2);
	}
	
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	public void loadLight(Light light) {
		super.loadVector3f(location_lightColour, light.getColour());
		super.loadVector3f(location_lightPosition, light.getPosition());
	}
	
	public void loadProjectionMatrix(float[] projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadMoveFactor(float moveFactor) {
		super.loadFloat(location_moveFactor, moveFactor);
	}
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(location_viewMatrix, AppTools.createViewMatrix(camera));
		super.loadVector3f(location_cameraPosition, camera.getCameraPosition());
	}
	
	public void loadModelMatrix(float[] modelMatrix) {
		super.loadMatrix(location_modelMatrix, modelMatrix);
	}
	
}
