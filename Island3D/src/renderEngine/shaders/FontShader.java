package renderEngine.shaders;

import android.content.Context;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_SHADER = "font.vs";
	private static final String FRAGMENT_SHADER = "font.fs";
	
	public FontShader(Context context) {
		super(VERTEX_SHADER, FRAGMENT_SHADER, context);		
	}

	private int location_colour;
    private int location_translation;
    
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
        location_translation = super.getUniformLocation("translation");
	}

	public void loadColour(float[] colour) {
		super.loadVector3f(location_colour, colour);
	}
	
	public void loadTranslation(float[] translation) {
		super.loadVector2f(location_translation, translation);
	}
}
