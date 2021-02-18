package renderEngine.shaders;

import android.content.Context;

public class ShadowShader extends ShaderProgram{

	private static final String VERTEX_SHADER = "shadow.vs";
	private static final String FRAGMENT_SHADER = "shadow.fs";
	
	public ShadowShader(Context context) {
		super(VERTEX_SHADER, FRAGMENT_SHADER, context);
	}

	private int location_mvpMatrix;
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
	}
	
	public void loadMvpMatrix(float[] mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}
}
