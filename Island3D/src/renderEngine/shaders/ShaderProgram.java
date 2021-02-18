package renderEngine.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.opengl.GLES20;

public abstract class ShaderProgram {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(String vertexShaderFile, String fragmentShaderFile, Context context) {
		
		vertexShaderID = loadShader(vertexShaderFile, GLES20.GL_VERTEX_SHADER, context);
		fragmentShaderID = loadShader(fragmentShaderFile, GLES20.GL_FRAGMENT_SHADER, context);
		programID = GLES20.glCreateProgram();
		
		GLES20.glAttachShader(programID, vertexShaderID);
		GLES20.glAttachShader(programID, fragmentShaderID);
		
		bindAttributes();
		
		GLES20.glLinkProgram(programID);
		GLES20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	protected abstract void bindAttributes();
	
	protected abstract void getAllUniformLocations();
		
	private static int loadShader(String path, int type, Context context) {
		
		StringBuilder shaderSource = new StringBuilder();
		
		//Loads a file
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("shaders/"+path)));
			String line;
			while((line=reader.readLine())!=null) {
				shaderSource.append(line).append("\n");
			}
			reader.close();
			
		}catch(IOException e) {
			throw new RuntimeException("Error occured while reading shader file!");
		}
		
		//Generates a shader by it's type
		int shaderID = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shaderID, shaderSource.toString());
		GLES20.glCompileShader(shaderID);
		
		//Check if the shader is not compiled
		final int[] compileStatus = new int[1];
		GLES20.glGetShaderiv(shaderID, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
		if(compileStatus[0]==0) {
			throw new RuntimeException("Error creating vertex shader.");
		}
		return shaderID;
	}
	
	public void start() {
		GLES20.glUseProgram(programID);
	}
	
	public void stop() {
		GLES20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		
		GLES20.glDetachShader(programID, vertexShaderID);
		GLES20.glDetachShader(programID, fragmentShaderID);
		
		GLES20.glDeleteShader(vertexShaderID);
		GLES20.glDeleteShader(fragmentShaderID);
		GLES20.glDeleteProgram(programID);
	}
	
	protected void bindAttribute(int attribute, String variableName) {
		GLES20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected int getUniformLocation(String uniformName) {
		return GLES20.glGetUniformLocation(programID, uniformName);
	}
	
	//*************** UNIFORM LOADERS BELOW *****************
	
	protected void loadBoolean(int location, boolean value) {
		float toLoad = 0;
		if(value)
			toLoad = 1;
		GLES20.glUniform1f(location, toLoad);
	}
	
	protected void loadVector4f(int location, float[] vector) {
		GLES20.glUniform4fv(location, 1, vector, 0);
	}
	
	protected void loadVector3f(int location, float[] vector) {
		GLES20.glUniform3fv(location, 1, vector, 0);
	}
	
	protected void loadVector2f(int location, float[] vector) {
		GLES20.glUniform2fv(location, 1, vector, 0);
	}
	
	protected void loadMatrix(int location, float[] matrix) {
		GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
	}
	
	protected void loadFloat(int location, float value) {
		GLES20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value) {
		GLES20.glUniform1i(location, value);
	}
}
