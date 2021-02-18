package renderEngine.shaders;

import android.content.Context;

public class ContrastShader extends ShaderProgram{

	private static final String VERTEX_SHADER = "contrast.vs";
	private static final String FRAGMENT_SHADER = "contrast.fs";
	
	public ContrastShader(Context context) {
		super(VERTEX_SHADER, FRAGMENT_SHADER, context);
	}

	private int location_contrast;
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_contrast = super.getUniformLocation("contrast");
	}

	public void loadContrastMultiplyer(float contrast) {
		super.loadFloat(location_contrast, contrast);
	}
}
