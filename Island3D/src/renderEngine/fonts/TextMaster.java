package renderEngine.fonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import renderEngine.Loader;
import renderEngine.fonts.fontMeshCreator.FontType;
import renderEngine.fonts.fontMeshCreator.GUIText;
import renderEngine.fonts.fontMeshCreator.TextMeshData;
import renderEngine.renderers.FontRenderer;

public class TextMaster {

	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static FontRenderer renderer;
	
	public static void init(Loader theLoader, Context context) {
		loader = theLoader;
		renderer = new FontRenderer(context);
	}
	
	public static void render() {
		renderer.render(texts);
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vao, data.getVertexCount());
		
		List<GUIText> batch = texts.get(font);
		if(batch==null) {
			batch = new ArrayList<GUIText>();
			texts.put(font, batch);
		}
		batch.add(text);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> batch = texts.get(text.getFont());
		batch.remove(text);
		
		if(batch.isEmpty()) {
			texts.remove(text.getFont());
			//Can also remove VAO of this text
		}
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
}
