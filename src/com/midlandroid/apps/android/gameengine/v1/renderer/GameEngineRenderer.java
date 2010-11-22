package com.midlandroid.apps.android.gameengine.v1.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * The OpenGl renderer.  This class will run in a thread created by
 * the OpenGL surface view.  Any data accessed or set by this class should be
 * protected.
 * 
 * @author Jason Del Ponte
 *
 */
public class GameEngineRenderer implements GLSurfaceView.Renderer {
	private static final String LOG_TAG = GameEngineRenderer.class.getSimpleName();

	@Override
	public void onDrawFrame(GL10 gl) {
		// define the color we want to be displayed as the "clipping wall"
		gl.glClearColor(0f, 0f, 0f, 1.0f);
		
		// reset the matrix
		gl.glLoadIdentity();
		
		// clear the color buffer to show the ClearColor we called above...
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// Do rendering of objects provided by the 
		// TODO render drawables provided by the simulator
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.d(LOG_TAG, "Surface changed, width="+width+" height="+height+".");
		
		// Resize the surface to match that of the screen.
		gl.glViewport(0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.d(LOG_TAG, "Suface created, hopefully for the first time.");

		// Enable the differentiation of which side may be visible
		gl.glEnable(GL10.GL_CULL_FACE);
		
		// which is the front?  The one which is drawn counter clockwise
		gl.glFrontFace(GL10.GL_CCW);
		
		// Which one should not be drawn
		gl.glCullFace(GL10.GL_BACK);
		
		// Set the client states
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		// Initialize the renderer
		initRenderer();
	}
	
	
	/**
	 * Initialize the renderer by allocating memory, and configuring this object
	 */
	private void initRenderer() {
		// TODO initialize
	}
}
