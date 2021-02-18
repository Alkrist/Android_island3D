package menus;

import java.util.ArrayList;
import java.util.List;

import com.test.AppMasterRenderer;

import android.content.Context;
import game.ModelBatch;
import menus.components.AbstractButton;
import menus.components.BackButton;
import menus.components.LandSizeMediumButton;
import menus.components.LandSizeSmallButton;
import menus.components.PauseButton;
import menus.components.PlayButton;
import menus.components.ScoreBoardButton;
import menus.components.SettingsButton;
import menus.components.ToMenuButton;
import renderEngine.Loader;
import renderEngine.fonts.fontMeshCreator.GUIText;
import renderEngine.textures.GuiTexture;

public class MenuHandler {

	private static Loader loader;
	private static Context context;
	
	
	private static List<AbstractButton> buttons = new ArrayList<AbstractButton>();
	
	private static List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private static List<GuiTexture> inGameGuis = new ArrayList<GuiTexture>();
	
	private static GUIText header;
	private static GUIText[] scores = new GUIText[2];
	private static float[] headerPosition = new float[2];
	private static int bluredBackgroundTexture;
	
	public static void update() {
		for(AbstractButton button: buttons) {
			button.update();
		}
	}
	
	public static void createMainMenu() {
		resetMenus();
		//Background
		guis.add(new GuiTexture(ModelBatch.texture_mainBackground, 0, 0, 1, 1));
		
		//Header
		guis.add(new GuiTexture(ModelBatch.texture_mainTitle, 0, 0.6f, 0.5f, 0.25f));
		
		//Buttons
		buttons.add(new PlayButton());
		buttons.add(new SettingsButton());
	}
	
	public static void createGameMenu() {
		resetMenus();
		
		//Buttons
		buttons.add(new PauseButton());
		buttons.add(new ScoreBoardButton());
	}
	
	public static void createPauseMenu() {
		resetMenus();
		//Background
		GuiTexture background = new GuiTexture(bluredBackgroundTexture, 0, 0, 1, 1);	
		background.setReversed();
		guis.add(background);
		//Header
		headerPosition [0] = 0.4f;
		headerPosition[1] = 0.15f;
		header = new GUIText("Pause", 6, ModelBatch.font_Candara, headerPosition, 1f, false);
		
		//Buttons
		buttons.add(new ToMenuButton(0, 0, 0.4f, 0.45f));
		buttons.add(new BackButton());
		//buttons.add(new SettingsButton());
	}
	
	public static void createSettingsMenu() {
		resetMenus();
		//Background
		guis.add(new GuiTexture(loader.loadTexture(context, "dirt.png"), 0, 0, 1, 1));
		
		//Header
		headerPosition [0] = 0.4f;
		headerPosition[1] = 0.15f;
		header = new GUIText("Level Size", 6, ModelBatch.font_Candara, headerPosition, 1f, false);
	
		//Buttons
		buttons.add(new ToMenuButton(0, -0.6f, 0.4f, 0.75f));
		buttons.add(new LandSizeSmallButton());
		buttons.add(new LandSizeMediumButton());
	}
	
	public static void createScoreBoard() {
		resetMenus();
		
		//Background texture
		GuiTexture background = new GuiTexture(bluredBackgroundTexture, 0, 0, 1, 1);
		background.setReversed();
		guis.add(background);
		
		//Header
		header = new GUIText("Game finished", 6, ModelBatch.font_Candara, headerPosition, 1f, false);
		
		//Scoreboard
		guis.add(new GuiTexture(ModelBatch.texture_scoreBoard, 0, 0, 0.8f, 0.8f));
		
		//Scores
		float[]textPos0 = {0.3f, 0.2f};
		float[]textPos1 = {0.3f, 0.4f};
		float[][]textPos = {textPos0, textPos1};
		for(int i=0; i<scores.length; i++) {
			scores[i] = new GUIText(Integer.toString(AppMasterRenderer.getGame().getScores()[i]),
					4, ModelBatch.font_Candara, textPos[i], 1f, false);
		}
		
		//Buttons
		buttons.add(new ToMenuButton(0.6f, -0.5f, 0.7f, 0.7f));
	}
	
	public static void resetMenus() {
		for(AbstractButton button: buttons) {
			button.onDestroy();
		}
		
		if(header!=null) {
			header.remove();
			header = null;
		}
		
		if(scores[0]!=null) {
			for(int i=0; i<scores.length; i++) {
				scores[i].remove();
				scores[i] = null;
			}
		}
				
		guis.clear();
		buttons.clear();
	}
	
	public static List<GuiTexture> getGuis(){
		return guis;
	}
	
	public static List<GuiTexture> getInGameGuis(){
		return inGameGuis;
	}
	
	public static void addGui(GuiTexture gui) {
		guis.add(gui);
	}
	
	public static void setBluredBackgroundTexture(int texture) {
		bluredBackgroundTexture = texture;
	}
	public static void addInGameGui(GuiTexture gui) {
		guis.add(gui);
	}
	
	public static void setLoader(Loader theLoader, Context theContext) {
		loader = theLoader;
		context = theContext;
	}
	
	public static AbstractButton checkButtonPressed(float[] point) {
		for(AbstractButton button: buttons) {
			if(button.getPosX()+button.getScaleX() >= point[0] 
				&& button.getPosX()-button.getScaleX() <= point[0]
				&& button.getPosY()+button.getScaleY() >= point[1]
				&& button.getPosY()-button.getScaleY() <= point[1])
				return button;
		}
		return null;
	}
}
