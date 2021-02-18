package renderEngine.shaders;

import android.content.Context;

public class VerticalBlurShader extends ShaderProgram{

	private static final String VERTEX_FILE = "blurVert.vs";
    private static final String FRAGMENT_FILE = "blur.fs";
    
	public VerticalBlurShader(Context context) {
		super(VERTEX_FILE, FRAGMENT_FILE, context);
	}

	private int location_targetHeight;
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_targetHeight = super.getUniformLocation("targetHeight");
	}

	public void loadTargetHeight(float height){
		super.loadFloat(location_targetHeight, height);
	}
	
}
