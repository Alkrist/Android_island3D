package menus.components;

import game.ModelBatch;
import menus.MenuHandler;
import renderEngine.fonts.fontMeshCreator.GUIText;
import renderEngine.textures.GuiTexture;

public abstract class AbstractButton {

	private float posX;
	private float posY;
	
	private float scaleX;
	private float scaleY;
	
	private int texture_idle;
	private int texture_pressed;
	
	protected boolean pressed;
	private float timer = 0;
	
	protected GuiTexture gui;
	private GUIText text;
	
	public AbstractButton(int texture_idle, int texture_pressed, float posX, float posY, float scaleX, float scaleY) {
		this.posX = posX;
		this.posY = posY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.texture_idle = texture_idle;
		this.texture_pressed = texture_pressed;
		
		this.gui = new GuiTexture(texture_idle, posX, posY, scaleX, scaleY);
		
		
		MenuHandler.addGui(gui);
	}
	
	public abstract void onClick();
	
	public void onDestroy() {
		if(text!=null)
			text.remove();
	}
	
	protected void setTextParams(String label, float posX, float posY, float length, float scale) {
		float[] pos = {posX, posY};
		this.text = new GUIText(label, scale, ModelBatch.font_Candara, pos, length, false);
	}
	
	public void update() {
		if(pressed) {
			timer+=0.07f;
			if(gui.getTexture()!=texture_pressed)
				gui.setTexture(texture_pressed);
			if(timer>= 1) {
				pressed = false;
				gui.setTexture(texture_idle);
				timer = 0;
			}
		}else if(gui.getTexture()!=texture_idle)
			gui.setTexture(texture_idle);
	}
	
	public void setPressed() {
		pressed = true;
	}
	
	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	protected float getTimer() {
		return timer;
	}
	
	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

}
