package renderEngine.shaders;

import android.content.Context;

public class WorldGuiShader extends ShaderProgram{

	private static final String VERTEX_SHADER = "worldGui.vs";
	private static final String FRAGMENT_SHADER = "worldGui.fs";
	
	public WorldGuiShader(Context context) {
		super(VERTEX_SHADER, FRAGMENT_SHADER, context);		
	}
	
	private int location_projectionMatrix;
	private int location_modelViewMatrix;
	private int location_guiTexture;
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");		
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_guiTexture = super.getUniformLocation("guiTexture");
	}
	
	public void connectTextureUnit() {
		super.loadInt(location_guiTexture, 0);
	}
	
	public void loadProjectionMatrix(float[] projection) {
		super.loadMatrix(location_projectionMatrix, projection);
	}

	public void loadModelViewMatrix(float[] modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

}
