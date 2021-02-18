package renderEngine.models;

import renderEngine.textures.Texture;

public class Model {

	private Mesh mesh;
	
	private Texture texture;

	public Model(Mesh mesh, Texture texture) {
		this.mesh = mesh;
		this.texture = texture;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

	public Texture getTexture() {
		return texture;
	}
	
}
