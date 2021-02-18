package menus.components;

import com.test.AppMasterRenderer;

import game.ModelBatch;

public class LandSizeSmallButton extends AbstractButton{
	
	private static int texture_idle = ModelBatch.texture_button1_idle;
	private static int texture_pressed = ModelBatch.texture_button1_pressed;
	private static String text = "Small";
	private static float scaleX = 0.25f, scaleY = 0.3f;
	private static float posX = -0.625f, posY = 0;
	float textPosX = 0.14f, textPosY = 0.45f;
	
	public LandSizeSmallButton() {
		super(texture_idle, texture_pressed, posX, posY, scaleX, scaleY);
		super.setTextParams(text, textPosX, textPosY, 0.5f, 1/scaleY);
	}
	
	@Override
	public void onClick() {
		AppMasterRenderer.setLevelSize(5);
	}
	
	@Override
	public void update() {
		if(AppMasterRenderer.getLevelSize() == 5) {
			if(gui.getTexture()!= texture_pressed)
				gui.setTexture(texture_pressed);
		}else if(gui.getTexture()!= texture_idle)
			gui.setTexture(texture_idle);		
	}
}