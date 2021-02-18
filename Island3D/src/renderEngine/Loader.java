package renderEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import renderEngine.models.Mesh;

import static android.opengl.GLUtils.texImage2D;

/**
 * This class is responsible for loading up all the data in the render engine
 * like meshes and textures
 * 
 * @author Mikhail
 */
public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	/**
	 * Loads the new mesh to VAO and creates the mesh object based on it's parameters
	 * @param positions
	 * 		- vertex positions (3D) - 0th VAO row
	 * @param textureCoords
	 * 		- coordinates for texture per vertex (2D) - 1th VAO row
	 * @param normals
	 * 		- normal vectors per vertex (3D) - 0th VAO row
	 * @param indices
	 * 		- indices of the render order - stored in index buffer
	 * @return new mesh object that has a VAO binded to it
	 */
	public Mesh loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		
		return new Mesh(vaoID, indices.length);
	}
	
	/**
	 * Loads a quad for font rendering
	 * @param positions
	 * 		- 2D positions (because quad is flat)
	 * @param textureCoords
	 * 		- 2D texture coordinates
	 * @return vao ID
	 */
	public int loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID = createVAO();
		
		storeDataInAttributeList(0, 2, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		
		return vaoID;
	}
	
	/**
	 * The Mesh loader for GUIs
	 * @param positions
	 * 		- 2D positions
	 * @param dimensions
	 * 		 - for 2D
	 * @return GUI Quad Mesh
	 */
	public Mesh loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		
		return new Mesh(vaoID, positions.length/dimensions);
	}
	
	private int createVAO() {
		int vaoID;
		
		//Creates an empty VAO
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES30.glGenVertexArrays(1, buffer);
		//buffer.flip();
		
		vaoID = buffer.get(0);
		vaos.add(vaoID);
		GLES30.glBindVertexArray(vaoID);
		System.out.println("creates VAO");
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int dimensions, float[] data) {
		int vboID;
		
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES20.glGenBuffers(1, buffer);
		
		vboID = buffer.get(0);
		vbos.add(vboID);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboID);
		
		FloatBuffer dataBuffer = storeDataInFloatBuffer(data);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, dataBuffer.capacity() * 4, dataBuffer, GLES20.GL_STATIC_DRAW);
		GLES20.glVertexAttribPointer(attributeNumber, dimensions, GLES20.GL_FLOAT, false, 0, 0);
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = FloatBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	private void unbindVAO() {
		GLES30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID;
		
		IntBuffer buffer = IntBuffer.allocate(1);
		GLES20.glGenBuffers(1, buffer);
		
		vboID = buffer.get(0);
		vbos.add(vboID);
		
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer indexBuffer = storeDataInIntBuffer(indices);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * 4,
				indexBuffer, GLES20.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = IntBuffer.allocate(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Destroys all resources loaded.
	 */
	public void cleanUp() {
		IntBuffer buffer = IntBuffer.allocate(1);
		
		for(int vao: vaos) {
			buffer.put(vao);
			//buffer.flip();
			GLES30.glDeleteVertexArrays(1, buffer);
			//buffer.flip();
			buffer.clear();
		}
		
		for(int vbo: vbos) {
			buffer.put(vbo);
			//buffer.flip();
			GLES20.glDeleteBuffers(1, buffer);
			//buffer.flip();
			buffer.clear();
		}
		
		for(int texture: textures) {
			buffer.put(texture);
			//buffer.flip();
			GLES20.glDeleteTextures(1, buffer);
			//buffer.flip();
			buffer.clear();
		}
	}
	
	private Bitmap loadBitmapFromAsset(AssetManager manager, String path) {
		InputStream inputStream = null;
		Bitmap bitmap = null;
		
		try {
			inputStream = manager.open("textures/"+path);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
			inputStream.close();
			
		} catch (IOException e) {
			bitmap = null;
			e.printStackTrace();
		}
		return bitmap;
	}
	
	/**
	 * Loads a PNG file from assets/textures and converts it to OpenGL texture
	 * @param context
	 * 		- Application context
	 * @param path
	 * 		- File_name.png
	 * @return texture ID
	 */
	public int loadTexture(Context context, String path) {
		int[] textureHandle = new int[1];		
		GLES20.glGenTextures(1, textureHandle, 0);
		
		if(textureHandle[0]!=0) {
			Bitmap bitmap = loadBitmapFromAsset(context.getAssets(), path);
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
			
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
	        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
	   
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			bitmap.recycle();
							
		}else throw new RuntimeException("Error occured while generating texture!");
		
		if(textureHandle[0]!=0)
			textures.add(textureHandle[0]);
		return textureHandle[0];
	}
	
	/**
	 * Loads a set of PNG files (6) and converts them to OpengGL cubeMap texture
	 * @param context
	 * 		- Application context
	 * @param files
	 * 		- set of File_name.png names
	 * @return cube map ID
	 */
	public int loadCubeMapTexture(Context context, String[] files) {
		int[] textureHandle = new int[1];		
		GLES20.glGenTextures(1, textureHandle, 0);
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		Bitmap[] bitmaps = new Bitmap[files.length];
		
		if(textureHandle[0]!=0) {
			
			for(int i=0; i<files.length; i++) {
				bitmaps[i] = loadBitmapFromAsset(context.getAssets(), files[i]);
			}
			
			GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureHandle[0]);	
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		    GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	
		    //In case of sharped edges
		    //GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		    //GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		    
		    texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmaps[0], 0);
	        texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0,  bitmaps[1], 0);
	        
	        texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0,  bitmaps[2], 0);
	        texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0,  bitmaps[3], 0);
	        
	        texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0,  bitmaps[4], 0);
	        texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0,  bitmaps[5], 0); 
	        
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	        
	        for(Bitmap bitmap: bitmaps) {
	        	bitmap.recycle();
	        }
	        
		}else throw new RuntimeException("Error occured while generating bitmap!");
		
		if(textureHandle[0]!=0)
			textures.add(textureHandle[0]);
		return textureHandle[0];
	}
	
}
