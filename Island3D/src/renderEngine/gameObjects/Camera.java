package renderEngine.gameObjects;

public class Camera {

	private float posX = 0, posY = 0, posZ = 0;
	private static float centerX = 15, centerY = -5, centerZ = 15;
	
	private float pitch = 0;
	private float yaw = 0;	
	
	private float distanceFromCenter = 10;
	private float rotationAngle;
	
	
	public void move(float dx, float dy, float dz) {
		calculateZoom(dz);	
		calculatePitch(dy);		
		calculateRotationAngle(dx);	
		
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		this.yaw = (float) (180 - Math.toRadians(rotationAngle*100 + 55));
	}
	
	private void calculatePitch(float dy) {
		pitch += dy*0.005f;	
		if(Math.toDegrees(pitch)<=-90 || Math.toDegrees(pitch)>=90)
			pitch -= dy*0.005f;
	}

	private void calculateZoom(float dz) {
		distanceFromCenter += dz;
		if(distanceFromCenter >= 175)
			distanceFromCenter = 175;
		if(distanceFromCenter <= 10)
			distanceFromCenter = 10;
	}

	private void calculateRotationAngle(float dx) {
		rotationAngle += dx;
	}

	private float calculateHorizontalDistance() {
		return (float)(distanceFromCenter * Math.cos(Math.toRadians(pitch * 40)));
	}
	
	private float calculateVerticalDistance() {
		return (float)(distanceFromCenter * Math.sin(Math.toRadians(pitch * 40)));
	}
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance) {
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(rotationAngle*100)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(rotationAngle*100)));
		
		posX = centerX - offsetX;
		posY = centerY + verticalDistance;
		posZ = centerZ - offsetZ;		
	}
	
	public float[] getCameraPosition() {
		float[] position = {
				posX, posY, posZ
		};
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}
	
	public static void setCenterPosition(float X, float Y, float Z) {
		centerX = X;
		centerY = Y;
		centerZ = Z;
	}
	public void invertPitch() {
		this.pitch = -pitch;
	}
}
