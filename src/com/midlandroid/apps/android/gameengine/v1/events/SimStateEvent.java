package com.midlandroid.apps.android.gameengine.v1.events;

/**
 * Simulation state change event container
 * 
 * @author Jason Del Ponte
 *
 */
public class SimStateEvent {
	private int _state;
	
	/**
	 * Set the simulation state
	 * @param state
	 */
	public void setState(final int state) { _state = state; }
	
	/**
	 * Returns the set simulation state
	 * @return
	 */
	public int getState() { return _state; }
}
