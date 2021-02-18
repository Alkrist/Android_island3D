package com.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import renderEngine.gameObjects.Camera;

public class MyGLSurfaceView extends GLSurfaceView{
	
	private final AppMasterRenderer renderer;
	private final ScaleGestureDetector scaleDetector;
	
	private Camera camera;
	
	private float scaleFactor = 1;
	private float previousX, previousY;
	private boolean isScaling = false;
	
	public MyGLSurfaceView(Context context) {
		super(context);
		camera = new Camera();
		setEGLContextClientVersion(3);
		renderer = new AppMasterRenderer(context, camera);
		scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		setRenderer(renderer);       
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		super.onTouchEvent(event);
		scaleDetector.onTouchEvent(event);
		
		if(isScaling) {
			isScaling=false;
			return true;
		}
		
		float x = event.getX();
		float y = event.getY();
				
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				renderer.checkIntersection(x, y, super.getWidth(), super.getHeight());
				break;
				
			case MotionEvent.ACTION_MOVE:				
				float dx = x - previousX;
				float dy = y - previousY;

				camera.move(dx*0.005f, dy, 0);
			break;
			case MotionEvent.ACTION_CANCEL:
				return true;
		}
		invalidate();
		
		previousX = x;
		previousY = y;
						
		return true;
	}
	
	public AppMasterRenderer getRenderer() {
		return renderer;
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
		
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scaleFactor *= detector.getScaleFactor();
						
			if(detector.getCurrentSpan()>detector.getPreviousSpan())
				camera.move(0, 0, scaleFactor * 0.4f);
			else
				camera.move(0, 0, scaleFactor * -0.4f);
			
			invalidate();
			isScaling = true;
			scaleFactor = 1;
			return true;
		}
	}
}
