package renderEngine.shaders;

import android.content.Context;
import renderEngine.AppTools;
import renderEngine.gameObjects.Camera;
import renderEngine.gameObjects.Light;

public class EntityShader extends ShaderProgram{

	private static final String VERTEX_SHADER = "entity.vs";
	private static final String FRAGMENT_SHADER = "entity.fs";
	
	public EntityShader(Context context) {
		super(VERTEX_SHADER, FRAGMENT_SHADER, context);
	}
	
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_invertedViewMatrix;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColour1;
	private int location_skyColour2;
	private int location_blendFactor;
	private int location_density;
	private int location_gradient;
	private int location_tagged;
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");	
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_invertedViewMatrix = super.getUniformLocation("invertedViewMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour = super.getUniformLocation("lightColour");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");	
		location_skyColour1 = super.getUniformLocation("skyColour1");
		location_skyColour2 = super.getUniformLocation("skyColour2");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_density = super.getUniformLocation("density");
		location_gradient = super.getUniformLocation("gradient");
		location_tagged = super.getUniformLocation("tagged");
	}
	
	public void loadTagFeature(boolean tagged) {
		super.loadBoolean(location_tagged, tagged);
	}
	
	public void loadTransformationMatrix(float[] transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}
	
	public void loadFogVariables(float density, float gradient) {
		super.loadFloat(location_density, density);
		super.loadFloat(location_gradient, gradient);
	}
	
	public void loadSkyColour(float R1, float G1, float B1, float R2, float G2, float B2) {
		float[] colour1 = {R1, G1, B1};
		float[] colour2 = {R2, G2, B2};
		super.loadVector3f(location_skyColour1, colour1);
		super.loadVector3f(location_skyColour2, colour2);
	}
	
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}
	
	public void loadProjectionMatrix(float[] projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		super.loadMatrix(location_viewMatrix, AppTools.createViewMatrix(camera));
		super.loadMatrix(location_invertedViewMatrix, AppTools.createInvertedViewMatrix(camera));
	}
	
	public void loadLight(Light light) {
		super.loadVector3f(location_lightPosition, light.getPosition());
		super.loadVector3f(location_lightColour, light.getColour());
	}
	
	public void loadShineVariables(float shineDamper, float reflectivity) {
		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
}
