package renderEngine.shaders;

import android.content.Context;

public class GuiShader extends ShaderProgram{

	private static final String VERTEX_SHADER = "gui.vs";
	private static final String FRAGMENT_SHADER = "gui.fs";
	
	public GuiShader(Context context) {
		super(VERTEX_SHADER, FRAGMENT_SHADER, context);		
	}

	private int location_transformationMatrix;
	
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadTransformationMatrix(float[] transformationMatrix) {
		super.loadMatrix(location_transformationMatrix, transformationMatrix);
	}

}
