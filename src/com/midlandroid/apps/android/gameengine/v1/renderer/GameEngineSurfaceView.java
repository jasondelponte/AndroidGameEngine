package com.midlandroid.apps.android.gameengine.v1.renderer;



import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * Creates the base OpenGL surface view.  The Open GL view's renderer will be
 * created here.  User inputs will be also be handled here
 * 
 * @author Jason Del Ponte
 *
 */
public class GameEngineSurfaceView extends GLSurfaceView {
	private static final String LOG_TAG = GameEngineSurfaceView.class.getName();
	private GameEngineRenderer _renderer;

	
	/**
	 * Constructor
	 */
	public GameEngineSurfaceView(Context context) {
		super(context);
		
		Log.d(LOG_TAG, "Creating, and starting OpenGL renderer");
		
		// Create and set the game engine renderer.
		_renderer = new GameEngineRenderer();
		setRenderer(_renderer);
	}
}
