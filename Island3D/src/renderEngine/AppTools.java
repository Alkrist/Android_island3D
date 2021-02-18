package renderEngine;

import android.opengl.Matrix;
import renderEngine.gameObjects.Camera;

/**
 * This class has all of the static mathematical calculations 
 * used for multiple purpouses
 * 
 * @author Mikhail
 */
public class AppTools {

	/**
	 * Creates the transformation matrix by the parameters given
	 * @param transX
	 * 		- translated X
	 * @param transY
	 * 		- translated Y
	 * @param transZ
	 * 		- translated Z
	 * @param rX
	 * 		- rotation X
	 * @param rY
	 * 		- rotation Y	
	 * @param rZ
	 * 		- rotation Z
	 * @param scale
	 * 		- scaling factor
	 * 
	 * @return transformation of the object, matrix 16x16
	 */
	public static float[] createTransformationMatrix(float transX, float transY, float transZ,
			float rX, float rY, float rZ, float scale) {
		float[] matrix = new float[16];		
		Matrix.setIdentityM(matrix, 0);
		Matrix.translateM(matrix, 0, transX, transY, transZ);
		
		Matrix.rotateM(matrix, 0, rX, 1, 0, 0);
		Matrix.rotateM(matrix, 0, rY, 0, 1, 0);
		Matrix.rotateM(matrix, 0, rZ, 0, 0, 1);
		
		Matrix.scaleM(matrix, 0, scale, scale, scale);
		
		return matrix;
	}
	
	public static float[] createTransformationMatrix(float[] translation, float[] scale) {
		float[] matrix = new float[16];	
		Matrix.setIdentityM(matrix, 0);
		
		Matrix.translateM(matrix, 0, translation[0], translation[1], 0);
		Matrix.scaleM(matrix, 0, scale[0], scale[1], 1f);
		
		return matrix;
	}
	
	/**
	 * Creates the view matrix (Camera matrix) by the camera object
	 * @param camera
	 * 		- camera object that contains all the camera's transformation
	 * 
	 * @return view matrix 16x16
	 */
	public static float[] createViewMatrix(Camera camera) {
		float[] matrix = new float[16];		
		Matrix.setIdentityM(matrix, 0);
		
		Matrix.rotateM(matrix, 0, (float) Math.toDegrees(camera.getPitch()), 1, 0, 0);
		Matrix.rotateM(matrix, 0, (float) Math.toDegrees(camera.getYaw()), 0, 1, 0);
		float[] cameraNegativePos = {
			-camera.getCameraPosition()[0], 
			-camera.getCameraPosition()[1],
			-camera.getCameraPosition()[2]
		};
		
		Matrix.translateM(matrix, 0, cameraNegativePos[0], cameraNegativePos[1], cameraNegativePos[2]);
		return matrix;
	}
	
	public static float[] createSkyboxViewMatrix(Camera camera) {
		float[] matrix = new float[16];		
		Matrix.setIdentityM(matrix, 0);
		
		Matrix.rotateM(matrix, 0, (float) Math.toDegrees(camera.getPitch()), 1, 0, 0);
		Matrix.rotateM(matrix, 0, (float) Math.toDegrees(camera.getYaw()), 0, 1, 0);
		return matrix;
	}
	
	/**
	 * Creates an inverted version of the View matrix for vertex shader
	 * @param camera
	 * 		- camera object that contains all the camera's transformation
	 * 
	 * @return inverted view matrix 16x16
	 */
	public static float[] createInvertedViewMatrix(Camera camera) {
		float[] matrix = new float[16];		
		Matrix.setIdentityM(matrix, 0);
		
		Matrix.rotateM(matrix, 0, (float) Math.toDegrees(camera.getPitch()), 1, 0, 0);
		Matrix.rotateM(matrix, 0, (float) Math.toDegrees(camera.getYaw()), 0, 1, 0);
		Matrix.translateM(matrix, 0, camera.getCameraPosition()[0], camera.getCameraPosition()[1], camera.getCameraPosition()[2]);
		return matrix;
	}
	
	public static float[] addVector3f(float[] left, float[] right) {
		float[] vector = {
				left[0] + right[0],
				left[1] + right[1],
				left[2] + right[2]
		};
		return vector;
	}
	
	public static float[] transformMatrix4f(float[]matrix4f, float[] vector4f) {
		float[] transformed = {
				(matrix4f[0]*vector4f[0])+(matrix4f[4]*vector4f[1])+(matrix4f[8]*vector4f[2])+(matrix4f[12]*vector4f[3]),
				(matrix4f[1]*vector4f[0])+(matrix4f[5]*vector4f[1])+(matrix4f[9]*vector4f[2])+(matrix4f[13]*vector4f[3]),
				(matrix4f[2]*vector4f[0])+(matrix4f[6]*vector4f[1])+(matrix4f[10]*vector4f[2])+(matrix4f[14]*vector4f[3]),
				(matrix4f[12]*vector4f[0])+(matrix4f[13]*vector4f[1])+(matrix4f[14]*vector4f[2])+(matrix4f[15]*vector4f[3])
		};
		return transformed;
	}
	
	public static float[] normalizeVector3f(float[] vector) {
		float length = (float) Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		float[] result = {
			vector[0] / length,
			vector[1] / length,
			vector[2] / length
		};
		return result;
	}
}
