package menus.components;

import com.test.AppMasterRenderer;

import game.ModelBatch;

public class SettingsButton extends AbstractButton{

	
	private static int texture_idle = ModelBatch.texture_button1_idle;
	private static int texture_pressed = ModelBatch.texture_button1_pressed;
	private static String text = "Settings";
	private static float posX = 0.625f, posY = 0;
	private static float scaleX = 0.25f, scaleY = 0.3f;
	
	public SettingsButton() {
		super(texture_idle, texture_pressed, posX, posY, scaleX, scaleY);
		super.setTextParams(text, 0.73f, 0.45f, 0.5f, 1/scaleY);
	}

	@Override
	public void onClick() {
		super.setPressed();		
	}

	@Override
	public void update() {
		super.update();
		if(super.getTimer()>=0.8f)
			AppMasterRenderer.settings();
	}
}
