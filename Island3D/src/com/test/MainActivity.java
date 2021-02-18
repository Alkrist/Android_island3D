package com.test;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import renderEngine.postProcessing.PostProcessing;

public class MainActivity extends Activity {

	private GLSurfaceView gLView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }
    
	@Override
    public void onPause() {
    	super.onPause();
    	if(AppMasterRenderer.getRunState() != AppMasterRenderer.RunState.MENU 
    			|| AppMasterRenderer.getRunState() != AppMasterRenderer.RunState.ENDGAME)
    		AppMasterRenderer.pause();
    }
   
    @Override
    public void onStop() {
    	super.onStop();
    	((MyGLSurfaceView)gLView).getRenderer().cleanUp();
    	PostProcessing.cleanUp();
    }
}

