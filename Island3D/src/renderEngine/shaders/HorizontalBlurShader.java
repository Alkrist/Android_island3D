package renderEngine.shaders;

import android.content.Context;

public class HorizontalBlurShader extends ShaderProgram{

	private static final String VERTEX_FILE = "blurHoriz.vs";
    private static final String FRAGMENT_FILE = "blur.fs";
	
	public HorizontalBlurShader(Context context) {
		super(VERTEX_FILE, FRAGMENT_FILE, context);
	}

	private int location_targetWidth;
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_targetWidth = super.getUniformLocation("targetWidth");
	}
	
	public void loadTargetWidth(float width){
		super.loadFloat(location_targetWidth, width);
	}

}
