package menus.components;

import game.ModelBatch;
import menus.MenuHandler;
import renderEngine.fonts.fontMeshCreator.GUIText;
import renderEngine.textures.GuiTexture;

public class ScoreBoardButton extends AbstractButton{

	private static int texture_idle = ModelBatch.texture_button3_idle;
	private static int texture_pressed = ModelBatch.texture_button3_pressed;
	private static float posX = -0.9f, posY = 0.8f;
	private static float scaleX = 0.1f, scaleY = 0.2f;
	
	private boolean isActive = false;
	private float rollTimer = 0;
	private float stayTimer = 0;
	private GuiTexture scoreBoardGui = new GuiTexture(ModelBatch.texture_scoreBoard, -0.5f, 2f, 0.3f, 1);
	
	private GUIText score_0;
	private GUIText score_1;
	private float[] textPos_0 = {0.2f, -0.3f};
	private float[] textPos_1 = {0.2f, -0.15f};

	public ScoreBoardButton() {
		super(texture_idle, texture_pressed, posX, posY, scaleX, scaleY);
		MenuHandler.addInGameGui(scoreBoardGui);
		score_0 = new GUIText("0", 4, ModelBatch.font_Candara, textPos_0, 0.5f, false);
		score_1 = new GUIText("0", 4, ModelBatch.font_Candara, textPos_1, 0.5f, false);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(score_0!=null)
			score_0.remove();
		if(score_1!=null)
			score_1.remove();
	}
	
	@Override
	public void onClick() {
		super.setPressed();
		isActive = true;
	}

	@Override
	public void update() {
		super.update();
		
		if(isActive) {
			if(rollTimer <= 1 && stayTimer <= 1) {
				scoreBoardGui.changePosition(0, -0.15f);
				score_0.changePosition(0, 0.05f);
				score_1.changePosition(0, 0.05f);
				rollTimer+=0.1f;
			}else if(rollTimer >= 1 && stayTimer <= 1) {
				stayTimer+=0.02f;
			}else if(rollTimer >= 1 && stayTimer >= 1 || rollTimer <= 1 && stayTimer >= 1) {
				scoreBoardGui.changePosition(0, 0.15f);
				score_0.changePosition(0, -0.05f);
				score_1.changePosition(0, -0.05f);
				rollTimer-=0.1f;
				if(rollTimer <= 0) {
					rollTimer = 0;
					stayTimer = 0;
					scoreBoardGui.setPosition(-0.5f, 2f);
					score_0.setPosition(textPos_0[0], textPos_0[1]);
					score_1.setPosition(textPos_1[0], textPos_1[1]);
					isActive = false;
				}
			}
		}
	}
	
}
