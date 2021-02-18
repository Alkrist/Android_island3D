package renderEngine.gameObjects;

import renderEngine.models.Model;

public class Entity {

	private Model model;
	
	private float posX, posY, posZ;
	private float rotX, rotY, rotZ;
	private float scale = 1;
	
	private boolean tagged = false;
	
	public Entity(Model model, float posX, float posY, float posZ, float rotX, float rotY, float rotZ) {
		this.model = model;
		
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.posX+=dx;
		this.posY+=dy;
		this.posZ+=dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX+=dx;
		this.rotY+=dy;
		this.rotZ+=dz;
	}
	
	//************ GETTERS/SETTERS BELOW *****************
	
	public void setPosition(float x, float y, float z) {
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	
	public float[] getPosition() {
		float pos[] = {
			posX, posY, posZ	
		};
		return pos;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public boolean isTagged() {
		return tagged;
	}

	public void setTagged(boolean tagged) {
		this.tagged = tagged;
	}
}
