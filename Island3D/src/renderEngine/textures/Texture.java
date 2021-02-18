package renderEngine.textures;

public class Texture {

	private int textureID;
	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean transparent = false;
	
	public Texture(int id) {
		this.textureID = id;
	}
	
	public int getTextureID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparency(boolean transparent) {
		this.transparent = transparent;
	}
	
}
