package renderEngine.gameObjects;

public class Light {

	private float posX, posY, posZ;
	private float R, G, B;
	
	public Light(float[]position, float[]colour) {
		this.posX = position[0];
		this.posY = position[1];
		this.posZ = position[2];
		
		this.R = colour[0];
		this.G = colour[1];
		this.B = colour[2];
	}
	
	public void setPosition(float[] position) {
		this.posX = position[0];
		this.posY = position[1];
		this.posZ = position[2];
	}
	
	public void setColour(float[] colour) {
		this.R = colour[0];
		this.G = colour[1];
		this.B = colour[2];
	}
	
	public float[] getPosition() {
		float[] position = { posX, posY, posZ };
		return position;				
	}
	
	public float[] getColour() {
		float[] colour = { R, G, B };
		return colour;
	}
}
