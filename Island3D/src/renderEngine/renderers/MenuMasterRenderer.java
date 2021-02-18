package renderEngine.renderers;

import java.util.List;

import com.test.AppMasterRenderer;

import android.content.Context;
import game.ModelBatch;
import menus.MenuHandler;
import renderEngine.Loader;
import renderEngine.fonts.TextMaster;
import renderEngine.textures.GuiTexture;

public class MenuMasterRenderer {

	private GuiRenderer renderer;
	
	private List<GuiTexture> allGuis;
	private GuiTexture currentTeamIcon;
	
	
	public MenuMasterRenderer(Loader loader, Context context, float[] projectionMatrix) {
		this.renderer = new GuiRenderer(loader, context);
		currentTeamIcon = new GuiTexture(ModelBatch.texture_flag_black, 0.7f, 0.8f, 0.08f, 0.15f);
	}
	
	public void renderInGame(short team) {
		
		allGuis = MenuHandler.getGuis();

		if(AppMasterRenderer.getRunState() == AppMasterRenderer.RunState.PLAY) {
			switch(team) {
			case 0:
				currentTeamIcon.setTexture(ModelBatch.texture_flag_black);
				break;
			case 1:
				currentTeamIcon.setTexture(ModelBatch.texture_flag_white);
				break;
			case 2:
				currentTeamIcon.setTexture(ModelBatch.texture_flag_yellow);
				break;
			case 3:
				currentTeamIcon.setTexture(ModelBatch.texture_flag_blue);
				break;
			}
			allGuis.add(currentTeamIcon);
			allGuis.addAll(MenuHandler.getInGameGuis());
			
		}
		renderer.render(allGuis);				
		TextMaster.render();	
	}
	
	public void RenderNoGame() {
		renderer.render(MenuHandler.getGuis());
		TextMaster.render();
	}
	
	/**
	 * Will destroy shaders and reset ALL menus!
	 */
	public void cleanUp() {
		renderer.cleanUp();
		TextMaster.cleanUp();
		MenuHandler.resetMenus();
	}
}
