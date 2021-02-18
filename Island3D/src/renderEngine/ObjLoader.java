package renderEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import renderEngine.models.Mesh;

public class ObjLoader {

	public static Mesh loadObj(Context context, Loader loader, String path) {
		
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("meshes/"+path)));
			
					String line;
		
					List<float[]> vertices = new ArrayList<float[]>();  //xyz
					List<float[]> normals = new ArrayList<float[]>();  //xyz
					List<float[]> textures = new ArrayList<float[]>();  //xy
					List<Integer> indices = new ArrayList<Integer>();
		
					float[] verticesArray = null;
					float[] normalsArray = null;
					float[] texturesArray = null;
					int[] indicesArray = null;
		
					try {
			
						while(true) {
							line = reader.readLine();
							String[] currentLine = line.split(" ");
							
							if(line.startsWith("v ")) {
								float[] vertex = {
									Float.parseFloat(currentLine[1]),
									Float.parseFloat(currentLine[2]),
									Float.parseFloat(currentLine[3])	
								};
								vertices.add(vertex);
							}
							else if(line.startsWith("vt ")) {
								float[] texture = {
										Float.parseFloat(currentLine[1]),
										Float.parseFloat(currentLine[2])
									};
								textures.add(texture);
							}
							else if(line.startsWith("vn ")) {
								float[] normal = {
										Float.parseFloat(currentLine[1]),
										Float.parseFloat(currentLine[2]),
										Float.parseFloat(currentLine[3])	
									};
								normals.add(normal);
							}
							else if(line.startsWith("f ")) {
								texturesArray = new float[vertices.size()*2];
								normalsArray = new float[vertices.size()*3];
								break;
							}
						}
						
						while(line!=null) {
							if(!line.startsWith("f ")) {
								line = reader.readLine();
								continue;
							}
							
							String[] currentLine = line.split(" ");
							String[] vertex1 = currentLine[1].split("/");
							String[] vertex2 = currentLine[2].split("/");
							String[] vertex3 = currentLine[3].split("/");
							
							//per one triangle
							processVertex(vertex1, indices, textures, normals, texturesArray, normalsArray);
							processVertex(vertex2, indices, textures, normals, texturesArray, normalsArray);
							processVertex(vertex3, indices, textures, normals, texturesArray, normalsArray);
							
							line = reader.readLine();
						}
						
						reader.close();
			
					}catch(Exception e) {
						throw new RuntimeException("An error occured while building the obj file!");
					}
					
					verticesArray = new float[vertices.size()*3];
					indicesArray = new int[indices.size()];
					
					int vertexPointer = 0;
					for(float[] vertex: vertices) {
						verticesArray[vertexPointer++] = vertex[0];
						verticesArray[vertexPointer++] = vertex[1];
						verticesArray[vertexPointer++] = vertex[2];
					}
					
					for(int i = 0; i < indices.size(); i++) {
						indicesArray[i] = indices.get(i);
					}
					
					return loader.loadToVAO(verticesArray, texturesArray, normalsArray, indicesArray);
						
			} catch (IOException e) {
				throw new RuntimeException("Could not load an OBJ file!");
			}
		
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices, List<float[]> textures, List<float[]> normals,
			float[] textureArray, float[] normalsArray) {
		
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		float[] currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2] = currentTex[0];
		textureArray[currentVertexPointer*2+1] = 1 - currentTex[1];
		
		float[] currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm[0];
		normalsArray[currentVertexPointer*3+1] = currentNorm[1];
		normalsArray[currentVertexPointer*3+2] = currentNorm[2];
	}
	
}
