package menus.components;

import com.test.AppMasterRenderer;

import game.ModelBatch;

public class PauseButton extends AbstractButton{

	private static int texture_idle = ModelBatch.texture_button2_idle;
	private static int texture_pressed = ModelBatch.texture_button2_pressed;
	private static float posX = 0.9f, posY = 0.8f;
	private static float scaleX = 0.1f, scaleY = 0.2f;
	
	public PauseButton() {
		super(texture_idle, texture_pressed, posX, posY, scaleX, scaleY);
	}

	@Override
	public void onClick() {
		super.setPressed();		
	}
	
	@Override
	public void update() {
		super.update();
		if(super.getTimer()>=0.8f)
			AppMasterRenderer.pause();
	}

}
