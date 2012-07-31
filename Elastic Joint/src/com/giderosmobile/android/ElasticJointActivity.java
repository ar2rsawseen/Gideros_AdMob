package com.giderosmobile.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

import com.giderosmobile.android.player.*;

import com.google.ads.*;
import com.yourdomain.yourapp.R;

public class ElasticJointActivity extends Activity implements OnTouchListener
{
	private AdView adView;
	private GLSurfaceView mGLView;
	private AudioDevice audioDevice = new AudioDevice();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        mGLView = new GiderosGLSurfaceView(this);
		//setContentView(mGLView);
        //set new main layout
      	setContentView(R.layout.main);
      	
		mGLView.setOnTouchListener(this);
                
        JavaNativeBridge.setActivity(this);
		JavaNativeBridge.onCreate();
		
		// Create the adView
	    adView = new AdView(this, AdSize.BANNER, "a14f6250f698606");

	    //get main layout
	    FrameLayout layout = (FrameLayout)findViewById(R.id.layout_main);
	    
	    //this can be used to change position of advertisement
	    //change Gravity.TOP to Gravity.BOTTOM if you want ads to appear in the bottom
	    FrameLayout.LayoutParams adParams = new FrameLayout.LayoutParams(
	    			FrameLayout.LayoutParams.WRAP_CONTENT ,
	    			FrameLayout.LayoutParams.WRAP_CONTENT ,
	    			Gravity.TOP);

	    
	    // Add the Gideros view to main layout
	    layout.addView(mGLView);
	    // Add the adView to main layout
	    layout.addView(adView, adParams);

	    // Initiate a generic request to load it with an ad
	    adView.loadAd(new AdRequest());
	}

	int[] id = new int[256];
	int[] x = new int[256];
	int[] y = new int[256];

	@Override
	public void onStart()
	{
		super.onStart();
		audioDevice.start();
	}

	@Override
	public void onRestart()
	{
		super.onRestart();
		JavaNativeBridge.onRestart();
	}

	@Override
	public void onStop()
	{
		audioDevice.stop();
		JavaNativeBridge.onStop();
		super.onStop();
	}

	@Override
	public void onDestroy()
	{
		//destroy add
		if (adView != null) {
			adView.destroy();
		}
		JavaNativeBridge.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	public boolean onTouch(View v, MotionEvent event)
	{
		int size = event.getPointerCount();
		for (int i = 0; i < size; i++)
		{
			id[i] = event.getPointerId(i);
			x[i] = (int) event.getX(i);
			y[i] = (int) event.getY(i);
		}

		int actionMasked = event.getActionMasked();
		boolean isPointer = (actionMasked == MotionEvent.ACTION_POINTER_DOWN || actionMasked == MotionEvent.ACTION_POINTER_UP);	
		int actionIndex = isPointer ? event.getActionIndex() : 0;
		int actionId = id[actionIndex];
				
		if (actionMasked == MotionEvent.ACTION_DOWN || actionMasked == MotionEvent.ACTION_POINTER_DOWN)
		{
			JavaNativeBridge.onTouchesBegin(size, id, x, y, actionId);
		} else if (actionMasked == MotionEvent.ACTION_MOVE)
		{
			JavaNativeBridge.onTouchesMove(size, id, x, y);
		} else if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_POINTER_UP)
		{
			JavaNativeBridge.onTouchesEnd(size, id, x, y, actionId);
		} else if (actionMasked == MotionEvent.ACTION_CANCEL)
		{
			JavaNativeBridge.onTouchesCancel(size, id, x, y);
		}

		return true;
	}
}

class GiderosGLSurfaceView extends GLSurfaceView
{
	public GiderosGLSurfaceView(Context context)
	{
		super(context);
		mRenderer = new GiderosRenderer();
		setRenderer(mRenderer);
	}

	GiderosRenderer mRenderer;
}

class GiderosRenderer implements GLSurfaceView.Renderer
{
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		JavaNativeBridge.onSurfaceCreated();
	}

	public void onSurfaceChanged(GL10 gl, int w, int h)
	{
		JavaNativeBridge.onSurfaceChanged(w, h);
	}

	public void onDrawFrame(GL10 gl)
	{
		JavaNativeBridge.onDrawFrame();
	}
}
