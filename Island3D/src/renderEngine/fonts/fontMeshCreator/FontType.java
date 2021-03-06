package renderEngine.fonts.fontMeshCreator;

import android.content.Context;

public class FontType {

	private int textureAtlas;
    private TextMeshCreator loader;
 
    /**
     * Creates a new font and loads up the data about each character from the
     * font file.
     *
     * @param textureAtlas
     *            - the ID of the font atlas texture.
     * @param fontFile
     *            - the font file containing information about each character in
     *            the texture atlas.
     */
    public FontType(int textureAtlas, String path, Context context, int width, int height) {
        this.textureAtlas = textureAtlas;
        this.loader = new TextMeshCreator(path, context, width, height);
    }
 
    /**
     * @return The font texture atlas.
     */
    public int getTextureAtlas() {
        return textureAtlas;
    }
 
    /**
     * Takes in an unloaded text and calculate all of the vertices for the quads
     * on which this text will be rendered. The vertex positions and texture
     * coords and calculated based on the information from the font file.
     *
     * @param text
     *            - the unloaded text.
     * @return Information about the vertices of all the quads.
     */
    public TextMeshData loadText(GUIText text) {
        return loader.createTextMesh(text);
    }
}
