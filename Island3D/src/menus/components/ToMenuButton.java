package menus.components;

import com.test.AppMasterRenderer;

import game.ModelBatch;

public class ToMenuButton extends AbstractButton{

	private static int texture_idle = ModelBatch.texture_button1_idle;
	private static int texture_pressed = ModelBatch.texture_button1_pressed;
	private static String text = "Main Menu";
	private static float scaleX = 0.25f, scaleY = 0.3f;
	
	public ToMenuButton(float posX, float posY, float textPosX, float textPosY) {
		super(texture_idle, texture_pressed, posX, posY, scaleX, scaleY);
		super.setTextParams(text, textPosX, textPosY, 0.5f, 1/scaleY);
	}

	@Override
	public void onClick() {
		super.setPressed();		
	}
	
	@Override
	public void update() {
		super.update();
		if(super.getTimer()>=0.8f)
			AppMasterRenderer.mainMenu();
	}

}
