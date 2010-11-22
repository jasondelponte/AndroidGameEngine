package com.midlandroid.apps.android.gameengine.v1.custcollections;

/**
 * Double linked list node.
 * @author Jason Del Ponte
 *
 * @param <T> type of data
 */
public class DoubleLLNode<T> {
	//private static final String LOG_TAG = DoubleLLNode.class.getSimpleName();
	
	private DoubleLLNode<T> _next;
	private DoubleLLNode<T> _prev;
	private T _data;

	/**
	 * Returns the reference to the next storage container.
	 * If there is no next node null will be returned.
	 * @return
	 */
	protected DoubleLLNode<T> getNext() { return _next; }
	

	/**
	 * Returns the reference to the previous storage container.
	 * If there is no previous node null will be returned.
	 * @return
	 */
	protected DoubleLLNode<T> getPrev() { return _prev; }
	

	/**
	 * Returns the reference to the data object
	 * @return
	 */
	public T getData() { return _data; }
	
	/**
	 * Adds a reference to the provided storage container to the end
	 * of this node in the linked list.
	 * @param newNext
	 */
	protected void insertNext(DoubleLLNode<T> newNext) {
		DoubleLLNode<T> currNext = null;
				
		// If this is an insert update the current next node
		if (_next != null) {
			// Save off the current next node reference so it can be
			// set to the new next node that was just received.
			currNext = _next;
			
			// Update the current next node's previous reference so it 
			// can know about the new node being added in the chain.
			currNext._prev = newNext;
		}
		
		// If there were a current node reference update the new next
		// node with it's reference
		if (newNext != null) {
			// Update the new next node's references to reflect it's links
			newNext._next = currNext;
			newNext._prev = this;
		}
		
		
		// Add the new next node
		_next = newNext;
	}
	
	
	/**
	 * Adds a reference to the provided storage container prior to this 
	 * node in the linked list.
	 * @param newPrev
	 */
	protected void insertPrev(DoubleLLNode<T> newPrev) {
		DoubleLLNode<T> currPrev = null;
		
		// if this is an insert update the current previous node
		if (_prev != null) {
			// Save off the current previous node reference so it can be
			// set to the new previous node that was just received.
			currPrev = _prev;
			
			// update the current previous node's next reference so it
			// can know about the new node being added in the chain.
			currPrev._next = newPrev;
		}
		
		// If there were a current node reference update the new previous
		// node with it's reference
		if (newPrev != null) {
			// Update the new previous node's references to reflect it's links
			newPrev._prev = currPrev;
			newPrev._next = this;
		}
		
		// Add the new previous node
		_prev = newPrev;
	}
	
	
	/**
	 * Removes this node from the linked list if there is any linkage.
	 */
	protected void remove() {
		DoubleLLNode<T> currPrev = _prev;
		DoubleLLNode<T> currNext = _next;
		
		// Set the next node's previous reference to point to our previous.
		if (currNext != null) 
			currNext._prev = currPrev;
		
		// Set the previous node's next reference to point to our next.
		if (currPrev != null)
			currPrev._next = currNext;
		
		_prev = null;
		_next = null;
	}
	
	
	/**
	 * Sets the data payload to the storage container
	 * @param data
	 */
	protected void setData(T data) { _data = data; }
}
