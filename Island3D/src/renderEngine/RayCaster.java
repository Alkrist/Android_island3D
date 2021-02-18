package renderEngine;

import com.test.AppMasterRenderer;

import android.opengl.Matrix;
import game.entities.Tile;
import renderEngine.gameObjects.Camera;

public class RayCaster {

	private static final int RECURSION_MAX_COUNT = 200;
	private static final float RAY_RANGE = 500;
	private static final float HEIGHT = -5f; //TODO: change it to softcoded later
	
	private Camera camera;
	private float[] ray; // rayX, rayY, rayZ, IS NORMALIZED
	private float[] pointingPosition; //The x, level_height, z position where the ray points in the world
	
	private float[] viewMatrix;
	private float[] projectionMatrix;
	
	public RayCaster(Camera camera, float[] projection) {
		this.camera = camera;
		this.projectionMatrix = projection;
		this.viewMatrix = AppTools.createViewMatrix(camera);
	}
	
	public float[] getCurrentRay() {
		return ray;
	}
	
	public float[] getPointingPosition() {
		return pointingPosition;
	}
	
	public void setValues(float[] projection, int width, int height) {
		this.projectionMatrix = projection;
	}
	
	public void cast(float x, float y, int width, int height, AppMasterRenderer renderer) {
		viewMatrix = AppTools.createViewMatrix(camera);
		ray = calculateRay(x, y, width, height);
		
		if(isInRange(0, RAY_RANGE, ray))
			pointingPosition = binarySearch(0, 0, RAY_RANGE, ray, renderer);
		else pointingPosition = null;
	}
	
	public float[] UICast(float x, float y, int width, int height) {
		return getNormalizedDeviceCoords(x, y, width, height);
	}
	
	private float[] calculateRay(float x, float y, int width, int height) {
		float[] normalizedDeviceCoords = getNormalizedDeviceCoords(x, y, width, height);
		float[] clipCoords = {				
				normalizedDeviceCoords[0],
				normalizedDeviceCoords[1],
				-1f,
				1f				
		};
		float[] eyeCoords = toEyeCoords(clipCoords);
		//Mind it's normalized
		float[] worldRay = toWorldCoords(eyeCoords);
		return  worldRay;
	}
	
	private float[] getNormalizedDeviceCoords(float touchX, float touchY, int width, int height) {
		float x = (2f * touchX) / width - 1;
		float y = (2f * touchY) / height - 1;
		float[] coords = {x, -y};
		return coords;
	}
	
	private float[] toEyeCoords(float[] clipCoords) {
		float[] invertedProjection = new float[16];
		Matrix.invertM(invertedProjection, 0, projectionMatrix, 0);
		
		//Transformation to vector 4f
		float[] eyeCoords = {
		(invertedProjection[0]*clipCoords[0])+(invertedProjection[1]*clipCoords[1])+(invertedProjection[2]*clipCoords[2])+(invertedProjection[3]*clipCoords[3]),
		(invertedProjection[4]*clipCoords[0])+(invertedProjection[5]*clipCoords[1])+(invertedProjection[6]*clipCoords[2])+(invertedProjection[7]*clipCoords[3]),
		//invertedProjection[8]*clipCoords[0]+invertedProjection[9]*clipCoords[1]+invertedProjection[10]*clipCoords[2]+invertedProjection[11]*clipCoords[3],
		//invertedProjection[12]*clipCoords[0]+invertedProjection[13]*clipCoords[1]+invertedProjection[14]*clipCoords[2]+invertedProjection[15]*clipCoords[3]
		-1f,
		0f
		};
		
		return eyeCoords;
	}

	private float[] toWorldCoords(float[] eyeCoords) {
		float[] invertedView = new float[16];
		Matrix.invertM(invertedView, 0, viewMatrix, 0);
		
		//Transform
		float[]worldCoords = {
		(invertedView[0]*eyeCoords[0])+(invertedView[4]*eyeCoords[1])+(invertedView[8]*eyeCoords[2])+(invertedView[12]*eyeCoords[3]),
		(invertedView[1]*eyeCoords[0])+(invertedView[5]*eyeCoords[1])+(invertedView[9]*eyeCoords[2])+(invertedView[13]*eyeCoords[3]),
		(invertedView[2]*eyeCoords[0])+(invertedView[6]*eyeCoords[1])+(invertedView[10]*eyeCoords[2])+(invertedView[14]*eyeCoords[3])
		//invertedView[12]*eyeCoords[0]+invertedView[13]*eyeCoords[1]+invertedView[14]*eyeCoords[2]+invertedView[15]*eyeCoords[3]
		};
		
		
		//Normalize
		float length = (float) Math.sqrt(
				(worldCoords[0]*worldCoords[0])
				+(worldCoords[1]*worldCoords[1])
				+(worldCoords[2]*worldCoords[2]));
		
		float[] ray = {
				worldCoords[0]/length,
				worldCoords[1]/length,
				worldCoords[2]/length
		};
		return ray;
	}
	
	// ***** INTERSECTION CHECK CODE BELOW *****
	
	private float[] getPointOnRay(float[] currentRay, float distance) {
		float[] camPos = camera.getCameraPosition();
		float[] scaledRay = {
				currentRay[0] * distance,
				currentRay[1] * distance,
				currentRay[2] * distance
		};
		
		float[] point = {
			camPos[0] + scaledRay[0],
			camPos[1] + scaledRay[1],
			camPos[2] + scaledRay[2]
		};		
		return point;
	}
	
	private boolean isInRange(float start, float end, float[] currentRay) {
		float[] startPoint = getPointOnRay(currentRay, start);
		float[] endPoint = getPointOnRay(currentRay, end);
		
		if(!isUnderTerrain(startPoint) && isUnderTerrain(endPoint))
			return true;
		else return false;
	}
	
	private boolean isUnderTerrain(float[] point) {
		if(point[1] < HEIGHT)
			return true;
		else return false;
	}
	
	private float[] binarySearch(int count, float start, float end, float[] currentRay, AppMasterRenderer renderer) {
		float half = start + ((end - start) / 2f);
		if(count >= RECURSION_MAX_COUNT) {
			float[] endPoint = getPointOnRay(currentRay, half);
			Tile tile = AppMasterRenderer.getGame().getLevel().getTile(endPoint[0], endPoint[2]);
			if(tile!=null)
				return endPoint;
			else return null;
		}
		if (isInRange(start, half, currentRay))
			return binarySearch(count + 1, start, half, currentRay, renderer);
		else return binarySearch(count + 1, half, end, currentRay, renderer);
	}
}
