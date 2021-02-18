package renderEngine.renderers;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import renderEngine.fonts.fontMeshCreator.FontType;
import renderEngine.fonts.fontMeshCreator.GUIText;
import renderEngine.shaders.FontShader;

public class FontRenderer {

	private FontShader shader;
	
	public FontRenderer(Context context) {
		this.shader = new FontShader(context);
	}
	
	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		
		for(FontType font: texts.keySet()) {
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, font.getTextureAtlas());
			for(GUIText text: texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	private void prepare(){
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		
		shader.start();
	}
    
    private void renderText(GUIText text){
    	GLES30.glBindVertexArray(text.getMesh());
    	GLES20.glEnableVertexAttribArray(0);
    	GLES20.glEnableVertexAttribArray(1);
    	
    	shader.loadColour(text.getColour());
    	shader.loadTranslation(text.getPosition());
    	
    	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, text.getVertexCount());
    	
    	GLES20.glDisableVertexAttribArray(0);
    	GLES20.glDisableVertexAttribArray(1);
    	GLES30.glBindVertexArray(0);
    }
     
    private void endRendering(){
    	shader.stop();
    	GLES20.glDisable(GLES20.GL_BLEND);
    	GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }
}
