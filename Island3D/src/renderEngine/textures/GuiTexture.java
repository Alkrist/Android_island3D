package renderEngine.textures;

public class GuiTexture {

	private int texture;
	
	private boolean reversed = false;
	
	private float posX, posZ;
	
	private float scaleX, scaleZ;

	public GuiTexture(int texture, float posX, float posZ, float scaleX, float scaleZ) {
		this.texture = texture;
		this.posX = posX;
		this.posZ = posZ;
		this.scaleX = scaleX;
		this.scaleZ = scaleZ;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public float[] getPosition() {
		float[] position = {posX, posZ};
		return position;
	}
	
	public void changePosition(float dx, float dz) {
		posX+=dx;
		posZ+=dz;
	}
	
	public void setPosition(float x, float z) {
		posX = x;
		posZ = z;
	}
	
	public float[] getScale() {
		float[] scale = {scaleX, scaleZ};
		return scale;
	}
	
	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	public void setReversed() {
		reversed = true;
	}
	
	public boolean isReversed() {
		return reversed;
	}
}
