package com.midlandroid.apps.android.gameengine.v1.events;

/**
 * User touch event storage container.
 * 
 * @author Jason Del Ponte
 *
 */
public class UserTouchEvent {
	private float _xCoord;
	private float _yCoord;
	
	private int _motionEvent;
	
	/**
	 * Stores the coordinates provided
	 * @param f
	 * @param g
	 */
	public void setCoords(final float f, final float g) {
		_xCoord = f;
		_yCoord = g;
	}
	
	/**
	 * Stores the motion event
	 * @param motion
	 */
	public void setMotionEvent(final int motion) { _motionEvent = motion; }
	
	
	/**
	 * Returns the stored X coordinate value
	 * @return
	 */
	public float getX() { return _xCoord; }
	
	
	/**
	 * Returns the stored Y coordinate value
	 * @return
	 */
	public float getY() { return _yCoord; }
	
	
	/**
	 * Returns the stored motion event value
	 * @return
	 */
	public int getMotionEvent() { return _motionEvent; }
}
