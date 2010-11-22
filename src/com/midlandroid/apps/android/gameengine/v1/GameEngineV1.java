package com.midlandroid.apps.android.gameengine.v1;


import com.midlandroid.apps.android.gameengine.v1.custcollections.DoubleLLNode;
import com.midlandroid.apps.android.gameengine.v1.events.SimStateEvent;
import com.midlandroid.apps.android.gameengine.v1.events.UserTouchEvent;
import com.midlandroid.apps.android.gameengine.v1.renderer.GameEngineSurfaceView;
import com.midlandroid.apps.android.gameengine.v1.simulation.SimulationState;
import com.midlandroid.apps.android.gameengine.v1.simulation.Simulator;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * First version of the game engine starter class.  This activity will create
 * the simulation thread, Open gl renderer, and input handlers.
 * 
 * @author Jason Del Ponte
 *
 */
public class GameEngineV1 extends Activity{
	private static final String LOG_TAG = GameEngineV1.class.getSimpleName();
	private static final int ONTOUCH_SLEEP = 62;
	
	private GameEngineSurfaceView _glSurfaceView = null;
	private Simulator _simulator;
	private Thread _simThread;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(LOG_TAG, "onCreate");	
		
		// Create and set the Open GL Surface view that will be used 
		// to draw the contents of this game to.
		_glSurfaceView = new GameEngineSurfaceView(this);
		setContentView(_glSurfaceView);
		
		
		/////////////////////////////////////////////////////////
		// Create the handlers for non-touch user inputs
		/////////////////////////////////////////////////////////
		// Create a touch listener
		_glSurfaceView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				DoubleLLNode<UserTouchEvent> container = _simulator.requestUserTouchEventNode();
				
				// only able to use containers if there are any available
				if (container != null) {
					UserTouchEvent data = container.getData();
					data.setCoords(event.getX(), event.getY());
					data.setMotionEvent(event.getAction());
					
					_simulator.queueTouchEvent(container);
				} else {
					Log.e(LOG_TAG, "Failed to request a user touch event container");
				}
				
				// we need to sleep for just a little bit to keep from flooding
				// the simulator with touch events
				try {
					synchronized(GameEngineV1.this) {
						GameEngineV1.this.wait(ONTOUCH_SLEEP);
					}
				} catch (InterruptedException e) {
					Log.e(LOG_TAG, "Wait interrupted while, sleeping at on Touch listener", e);
				}
				
				return true;
			}
		});
	}
	
	
//	/**
//	 * A key press event has occurred.  All but BACK and HOME events 
//	 * will be processed by the simulator.
//	 * 
//	 * @param keyCode key value of the event
//	 * @param event event object
//	 * @return
//	 */
//	public boolean onKey(final int keyCode, final KeyEvent event) {
//		Log.d(LOG_TAG, "onKey keyCode "+keyCode);
//		// TODO Fix key receiver, currently not working at all
//		
//		// Handle all key presses except HOME and BACK
//		if (keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_HOME) {
//			_simulator.queueEvent(new Runnable() {
//				@Override
//				public void run() {
//					Log.d(LOG_TAG, "_simulator.queueEvent - keyCode: "+keyCode);
//					// TODO Auto-generated method stub
//					
//				}
//			});
//			
//			return true;
//		}
//		
//		return false;
//	}
	
	
	/** The View has been brought back into focus. */
	@Override
	public void onResume() {
		super.onResume();
		
		Log.d(LOG_TAG, "onResume");
		
		// Create the simulation background thread
		_simulator = new Simulator(this, _glSurfaceView);
		_simThread = new Thread(_simulator);
		_simThread.start();
	}
	
	
	/** The view is being taken out of focus.  */
	@Override
	public void onPause() {
		super.onPause();
		
		Log.d(LOG_TAG, "onPause");
		
		// Get two simulation state nodes.  One to save the current state and
		// the other to tell the simulation to exit.
		DoubleLLNode<SimStateEvent> saveNode = _simulator.requestSimStateEventNode();
		DoubleLLNode<SimStateEvent> exitNode = _simulator.requestSimStateEventNode();
		
		// make sure the nodes provided are valid
		if (saveNode != null && exitNode != null) {
			SimStateEvent data = null;
			
			// Set, and queue the save current state event
			data = saveNode.getData();
			data.setState(SimulationState.SAVE_CURRENT_STATE);
			_simulator.queueSimStateEvent(saveNode);
			
			// Set and queue the exit state event
			data = exitNode.getData();
			data.setState(SimulationState.EXIT_SIM);
			_simulator.queueSimStateEvent(exitNode);
		} else {
			Log.e(LOG_TAG, "Failed to notify the simulator to exit. No SimStateEvent nodes were available.");
		}
	}
}
