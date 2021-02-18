package renderEngine.postProcessing;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import menus.MenuHandler;
import renderEngine.Loader;
import renderEngine.models.Mesh;
import renderEngine.postProcessing.gaussianBlur.HorizontalBlur;
import renderEngine.postProcessing.gaussianBlur.VerticalBlur;

public class PostProcessing {

	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static Mesh quad;

	private static ContrastChanger contrastChanger;
	private static HorizontalBlur horizBlur;
	private static VerticalBlur vertBlur;
	
	
	public static void init(Loader loader, Context context, int width, int height){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger(context);
		horizBlur = new HorizontalBlur(context, width/8, height/8, width, height);
		vertBlur = new VerticalBlur(context, width/8, height/8, width, height);
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
		horizBlur.render(colourTexture);
		vertBlur.render(horizBlur.getOutputTexture());
		MenuHandler.setBluredBackgroundTexture(vertBlur.getOutputTexture());
		contrastChanger.render(colourTexture);
		
		end();
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		vertBlur.cleanUp();
		horizBlur.cleanUp();
	}
	
	private static void start(){
		GLES30.glBindVertexArray(quad.getVaoID());
		GLES20.glEnableVertexAttribArray(0);
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDisableVertexAttribArray(0);
		GLES30.glBindVertexArray(0);
	}


}
