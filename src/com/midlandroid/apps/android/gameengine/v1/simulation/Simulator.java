package com.midlandroid.apps.android.gameengine.v1.simulation;

import com.midlandroid.apps.android.gameengine.v1.custcollections.LinkedListQueue;
import com.midlandroid.apps.android.gameengine.v1.custcollections.LinkedListStack;
import com.midlandroid.apps.android.gameengine.v1.custcollections.DoubleLLNode;
import com.midlandroid.apps.android.gameengine.v1.events.SimStateEvent;
import com.midlandroid.apps.android.gameengine.v1.events.UserTouchEvent;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

/**
 * All game engine simulation will be performed by this class
 * 
 * @author Jason Del Ponte
 *
 */
public class Simulator implements Runnable {
	private static final String LOG_TAG = Simulator.class.getSimpleName();
	
	// Provides the simulation time step
	private static final long SIMULATION_STEP_MILLS = 50;
	
	private static final int TOUCHEVENT_STACK_SIZE = 10;
	private static final int SIMSTATEEVENT_STACK_SIZE = 3;

	private GLSurfaceView _surfaceView;
	private boolean _simRunning;
	
	// Simulation State Events
	private LinkedListStack<SimStateEvent> _availSimStateEvents;
	private LinkedListQueue<SimStateEvent> _queuedSimStateEvents;
	
	// User Touch Events
	private LinkedListStack<UserTouchEvent> _availUserTouchEvents;
	private LinkedListQueue<UserTouchEvent> _queuedUserTouchEvents;
	
	
	/**
	 * Creates a new instance of the simulator
	 * @param context parent context
	 * @param surfaceView 
	 */
	public Simulator(Context context, GLSurfaceView surfaceView) {
		Log.d(LOG_TAG, "Simulator object created.");
		
		// Create the incoming simulation state events
		_availSimStateEvents = new LinkedListStack<SimStateEvent>(SimStateEvent.class);
		_queuedSimStateEvents = new LinkedListQueue<SimStateEvent>(SimStateEvent.class);
		
		// Store off the OpenGL surface view
		_surfaceView = surfaceView;
		
		// Create incoming user touch events
		_availUserTouchEvents = new LinkedListStack<UserTouchEvent>(UserTouchEvent.class);
		_queuedUserTouchEvents = new LinkedListQueue<UserTouchEvent>(UserTouchEvent.class);

		_simRunning = true;
	}
	
	
	//////////////////////////////////////////////////////
	// Simulation Main Loop
	//////////////////////////////////////////////////////
	/**
	 * Thread main loop
	 */
	@Override
	public void run() {
		boolean areDrawables = false;
		
		// Pre-allocate memory
		preAllocateMemory();
		
		// Start the simulation processor loop. We will stay in this
		// loop until the simulation is stopped.
		while(_simRunning) {
		
			///////////////////////////////////////////
			// Pre-processing
			///////////////////////////////////////////
			// Process the incoming event queue before running the
			// simulation's next time step
			synchronized(_queuedSimStateEvents) {
				// Only one simulation state event can be processed per sim loop
				DoubleLLNode<SimStateEvent> event = _queuedSimStateEvents.pop();
				if (event != null) {
					// Process and release the event
					processSimStateEvent(event.getData().getState());
					releaseSimStateEvent(event);
				}
			}
			
			// Process the incoming user touch event queue before continuing
			synchronized(_queuedUserTouchEvents) {
				DoubleLLNode<UserTouchEvent> event = null;
				while((event = _queuedUserTouchEvents.pop()) != null) {
					//UserTouchEvent data = event.getData();
					// Process and release the event
					// TODO handle the user touch event
					releaseUserTouchEventNode(event);
				}
			}
				
			
			
			///////////////////////////////////////////
			// Process the current simulation step
			///////////////////////////////////////////
			
			
			

			///////////////////////////////////////////
			// Post Processing
			///////////////////////////////////////////
			// Drawables need to be sent to the surface view.
			if (areDrawables) {
				_surfaceView.queueEvent(new Runnable() {
					@Override
					public void run() {
						Log.d(LOG_TAG, "Send drawables to the surfaceview's renderer");
						
					}
				});
			}
			
			// Finished the current simulation step, put in a wait until
			// the next simulation step
			try {
				synchronized(this) {
					wait(SIMULATION_STEP_MILLS);
				}
			} catch (InterruptedException e) {
				Log.e(LOG_TAG, "Failed to set the simulation step wait.", e);
			}
		}
		
		// Free references to the preallocated memory
		freePreAllocatedMemory();
		
		Log.d(LOG_TAG, "Simulator exiting.");
	}
	
	
	///////////////////////////////////////////////////
	// Simulation Processors
	///////////////////////////////////////////////////
	
	/**
	 * Using the state provided the simulation state machine
	 * is updated to set the current simulation processing
	 * mode.
	 * @param state
	 */
	private void processSimStateEvent(final int state) {
		switch (state) {
		case SimulationState.SAVE_CURRENT_STATE:
			saveCurrentState();
			break;
		case SimulationState.EXIT_SIM:
			finishSim();
			break;
		}
	}


	/**
	 * The current state of the simulation will be stored in the background.
	 */
	public void saveCurrentState() {
		Log.d(LOG_TAG, "Saving the current simulation state.");
		
		// TODO Auto-generated method stub		
	}

	
	/**
	 * The simulator is being told to finish and exit.
	 */
	public void finishSim() {
		Log.d(LOG_TAG, "Finish the simulation.");
		
		_simRunning = false;
	}
	
	
	/////////////////////////////////////////////////////
	// Memory Management 
	/////////////////////////////////////////////////////
	/**
	 * Allocates memory needed for the simulation.
	 */
	private void preAllocateMemory() {
		// User Touch Event preallocated nodes
		synchronized (_availUserTouchEvents) {
			_availUserTouchEvents.allocateNodes(TOUCHEVENT_STACK_SIZE);
			// User Touch Event queue, Nothing to preallocate, the nodes from
			// the _availUserTouchEvents will be added to the event queue as
			// they are used.  Once they are processed they will be removed
			// from the queue and added back to the avail stack.
		}
		
		// Simulation state events preallocated nodes
		synchronized(_availSimStateEvents) {
			_availSimStateEvents.allocateNodes(SIMSTATEEVENT_STACK_SIZE);
			// Simulation State Event queue, Nothing to preallocate, the nodes from
			// the _availSimStateEvents will be added to the event queue as
			// they are used.  Once they are processed they will be removed
			// from the queue and added back to the avail stack.
		}
	}
	
	
	/**
	 * Release the references to the preallocated memory.
	 */
	private void freePreAllocatedMemory() {
		synchronized(_availUserTouchEvents) {
			_availUserTouchEvents.freeNodes();
		}
		
		synchronized(_queuedUserTouchEvents) {
			_queuedUserTouchEvents.freeNodes();
		}
		
		synchronized(_availSimStateEvents) {
			_availSimStateEvents.freeNodes();
		}
		
		synchronized(_queuedSimStateEvents) {
			_queuedSimStateEvents.freeNodes();
		}
	}
	
	
	////////////////////////////////////////////////
	// Queue/Request/release Event nodes
	////////////////////////////////////////////////
	/**
	 * Add a new simulation touch event to the queue that will be processed
	 * by the simulator next time step.
	 * @param node The simulation state event to be processed
	 */
	public void queueSimStateEvent(DoubleLLNode<SimStateEvent> node) {
		synchronized(_queuedSimStateEvents) {
			Log.d(LOG_TAG, "New event added to the queue.  Current count: "+_queuedSimStateEvents.size());
			_queuedSimStateEvents.push(node);
		}
	}
	
	
	/**
	 * Add a new user touch event to the queue that will be processed
	 * by the simulator next time step.
	 * @param node  The user touch event to be processed
	 */
	public void queueTouchEvent(DoubleLLNode<UserTouchEvent> node) {
		synchronized(_queuedUserTouchEvents) {
			Log.d(LOG_TAG, "New user touch event queued.  Current count: "+_queuedUserTouchEvents.size());
			_queuedUserTouchEvents.push(node);
		}
	}
	
	/**
	 * Provides a user touch event node to the requester.  If there are no
	 * nodes available a null reference will be returned.
	 * @return
	 */
	public DoubleLLNode<UserTouchEvent> requestUserTouchEventNode() {
		DoubleLLNode<UserTouchEvent> node = null;
		synchronized (_availUserTouchEvents) {
			node = _availUserTouchEvents.pop();
		}
		
		return node;
	}
	
	
	/**
	 * Release a no longer needed user touch event node back to the stack of
	 * available nodes.
	 * @param node
	 */
	public void releaseUserTouchEventNode(DoubleLLNode<UserTouchEvent> node) {
		synchronized (_availUserTouchEvents) {
			_availUserTouchEvents.push(node);
		}
	}
	
	
	/**
	 * Provides a simulation state event node to the requester. If there are no
	 * nodes available a null reference will be returned.
	 * @return
	 */
	public DoubleLLNode<SimStateEvent> requestSimStateEventNode() {
		DoubleLLNode<SimStateEvent> node;
		synchronized(_availSimStateEvents) {
			node = _availSimStateEvents.pop();
		}
		return node;
	}
	
	
	/**
	 * Releases a no longer needed simulation state node back to the stack
	 * of available nodes.
	 * @param node
	 */
	public void releaseSimStateEvent(DoubleLLNode<SimStateEvent> node) {
		synchronized(_availSimStateEvents) {
			_availSimStateEvents.push(node);
		}
	}
}
