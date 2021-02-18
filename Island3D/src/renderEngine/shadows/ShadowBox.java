package renderEngine.shadows;

import java.util.ArrayList;
import java.util.List;

import com.test.AppMasterRenderer;

import android.opengl.Matrix;
import renderEngine.AppTools;
import renderEngine.gameObjects.Camera;

public class ShadowBox {

	private static final float OFFSET = 10;
	private static final float[] UP = {0, 1, 0, 0};
	private static final float[] FORWARD = {0, 0, -1, 0};
	private static final float SHADOW_DISTANCE = 100;
	
	private float minX, maxX;
	private float minY, maxY;
	private float minZ, maxZ;
	private float[] lightViewMatrix;
	private Camera camera;
	
	private float farHeight, farWidth, nearHeight, nearWidth;
	
	public ShadowBox(float[] lightViewMatrix, Camera camera) {
		this.lightViewMatrix = lightViewMatrix;
		this.camera = camera;
		calculateWidthsAndHeights();
	}
	
	private void calculateWidthsAndHeights() {
		farWidth = (float) (SHADOW_DISTANCE * Math.tan(Math.toRadians(AppMasterRenderer.FOV)));
		nearWidth = (float) (AppMasterRenderer.NEAR_PLANE
				* Math.tan(Math.toRadians(AppMasterRenderer.FOV)));
		farHeight = farWidth / getAspectRatio();
		nearHeight = nearWidth / getAspectRatio();
	}
	
	private float[] calculateCameraRotationMatrix() {
		float[] rotation = new float[16];
		Matrix.rotateM(rotation, 0, (float) Math.toDegrees(-camera.getPitch()), 1, 0, 0);
		Matrix.rotateM(rotation, 0, (float) Math.toDegrees(-camera.getYaw()), 0, 1, 0);
		return rotation;
	}
	
	private float[] calculateLightSpaceFrustumCorner(float[] startPoint, float[] direction, float width) {
		float[] point = {
			startPoint[0] + (direction[0]*width),	
			startPoint[1] + (direction[1]*width),
			startPoint[2] + (direction[2]*width),
			1f
		};		
		float[] pointTransformed = AppTools.transformMatrix4f(lightViewMatrix, point);
		return pointTransformed;
	}
	
	private List<float[]> calculateFrustumVertices(float[] rotation, float[] forwardVector,
			float[] centerNear, float[] centerFar) {
		float[] upVector = AppTools.transformMatrix4f(rotation, UP);
		float[] rightVector = {
				forwardVector[1]*upVector[2] - forwardVector[2]*upVector[1],
				upVector[0]*forwardVector[2] - upVector[2]*forwardVector[0],
				forwardVector[0]*upVector[1] - forwardVector[1]*upVector[0]
		};
		float[] downVector = {
				-upVector[0],
				-upVector[1],
				-upVector[2]
		};
		float[] leftVector = {
				-rightVector[0],
				-rightVector[1],
				-rightVector[2]
		};		
		float[] upVectorFarHeight = {
				upVector[0] * farHeight,
				upVector[1] * farHeight,
				upVector[2] * farHeight
		};
		float[] downVectorFarHeight = {
				downVector[0] * farHeight,
				downVector[1] * farHeight,
				downVector[2] * farHeight
		};
		float[] upVectorNearHeight = {
				upVector[0] * nearHeight,
				upVector[1] * nearHeight,
				upVector[2] * nearHeight
		};
		float[] downVectorNearHeight = {
				downVector[0] * nearHeight,
				downVector[1] * nearHeight,
				downVector[2] * nearHeight
		};
		float[] farTop = AppTools.addVector3f(centerFar, upVectorFarHeight);
		float[] farBottom = AppTools.addVector3f(centerFar, downVectorFarHeight);
		float[] nearTop = AppTools.addVector3f(centerNear, upVectorNearHeight);
		float[] nearBottom = AppTools.addVector3f(centerNear, downVectorNearHeight);
		
		List<float[]> points = new ArrayList<float[]>();
		points.add(calculateLightSpaceFrustumCorner(farTop, rightVector, farWidth));
		points.add(calculateLightSpaceFrustumCorner(farTop, leftVector, farWidth));
		points.add(calculateLightSpaceFrustumCorner(farBottom, rightVector, farWidth));
		points.add(calculateLightSpaceFrustumCorner(farBottom, leftVector, farWidth));
		points.add(calculateLightSpaceFrustumCorner(nearTop, rightVector, nearWidth));
		points.add(calculateLightSpaceFrustumCorner(nearTop, leftVector, nearWidth));
		points.add(calculateLightSpaceFrustumCorner(nearBottom, rightVector, nearWidth));
		points.add(calculateLightSpaceFrustumCorner(nearBottom, leftVector, nearWidth));
		return points;
	}
	
	public void update() {
		float[] rotation = calculateCameraRotationMatrix();
		float[] forwardVector = AppTools.transformMatrix4f(rotation, FORWARD);
		float[] toFar = {
			forwardVector[0] * SHADOW_DISTANCE,
			forwardVector[1] * SHADOW_DISTANCE,
			forwardVector[2] * SHADOW_DISTANCE
		};
		float[] toNear = {
			forwardVector[0] * AppMasterRenderer.NEAR_PLANE,
			forwardVector[1] * AppMasterRenderer.NEAR_PLANE,
			forwardVector[2] * AppMasterRenderer.NEAR_PLANE
		};
		float[] centerNear = AppTools.addVector3f(toNear, camera.getCameraPosition());
		float[] centerFar = AppTools.addVector3f(toFar, camera.getCameraPosition());
		
		List<float[]> points = calculateFrustumVertices(rotation, forwardVector, centerNear, centerFar);
		boolean first = true;
		for(float[] point: points) {
			if(first) {
				minX = point[0];
				maxX = point[0];
				minY = point[1];
				maxY = point[1];
				minZ = point[2];
				maxZ = point[2];
				first = false;
				continue;
			}
			if (point[0] > maxX) {
				maxX = point[0];
			} else if (point[0] < minX) {
				minX = point[0];
			}
			if (point[1] > maxY) {
				maxY = point[1];
			} else if (point[1] < minY) {
				minY = point[1];
			}
			if (point[2] > maxZ) {
				maxZ = point[2];
			} else if (point[2] < minZ) {
				minZ = point[2];
			}
		}
		maxZ += OFFSET;
	}
	
	private float getAspectRatio() {
		return (float) AppMasterRenderer.Width / (float) AppMasterRenderer.Height;
	}
	
	public float getWidth() {
		return maxX - minX;
	}
	
	public float getHeight() {
		return maxY - minY;
	}
	
	public float getLength() {
		return maxZ - minZ;
	}
	
	public float[] getCenter() {
		float x = (minX + maxX) / 2f;
		float y = (minY + maxY) / 2f;
		float z = (minZ + maxZ) / 2f;
		float[] cen = {x, y, z, 1};
		float[] invertedLight = new float[16];
		Matrix.invertM(lightViewMatrix, 0, invertedLight, 0);
		float[] center = AppTools.transformMatrix4f(invertedLight, cen);
		return center;
	}
}
