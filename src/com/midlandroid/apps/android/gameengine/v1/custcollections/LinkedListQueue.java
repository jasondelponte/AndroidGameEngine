package com.midlandroid.apps.android.gameengine.v1.custcollections;

import android.util.Log;

/**
 * Queue collection with a linked list for nodes.
 * 
 * @author Jason Del Ponte
 *
 * @param <T> Class type of data
 */
public class LinkedListQueue<T> {
	private static final String LOG_TAG = LinkedListQueue.class.getSimpleName();
	
	private final Class<T> _dataCls;
	
	private int _nodeCount;
	private DoubleLLNode<T> _headNode;
	private DoubleLLNode<T> _tailNode;
	
	/**
	 * Create a new instance of the queue with the provided class data type.
	 * @param dataCls data class data type.
	 */
	public LinkedListQueue(final Class<T> dataCls) {
		_dataCls = dataCls;
	}
	
	
	/**
	 * Pre allocate nodes and store them in the queue.
	 * @param count number of nodes to create
	 */
	public void allocateNodes(final int count) {
		final Class<T> dataCls = _dataCls;
		
		for (int idx=0; idx < count; idx++) {
			DoubleLLNode<T> node = new DoubleLLNode<T>();

			T nodeData = null;
			try {
				nodeData = (T)dataCls.newInstance();
			} catch (IllegalAccessException e) {
				Log.e(LOG_TAG, "Failed to create new instance of node data class", e);
			} catch (InstantiationException e) {
				Log.e(LOG_TAG, "Failed ot create new instance of node data class", e);
			}
			
			// Set the node data reference to the node
			node.setData(nodeData);
			
			// Add the node to the queue
			push(node);
		}
	}

	
	/**
	 * Free the references to the nodes in the queue
	 */
	public void freeNodes() {
		// Loop over all nodes popping each from the stack
		// removing our reference from the them.
		while (_headNode != null) {
			pop();
		}
	}
	
	
	/**
	 * Removes and returns the head node of the queue
	 * @return
	 */
	public DoubleLLNode<T> pop() {
		DoubleLLNode<T> popNode = null;
		
		// If there is a valid node at the head of the queue get it, and 
		// set the new head
		if (_headNode != null) {
			popNode = _headNode;
			
			// set the new head
			_headNode = popNode.getNext();
			
			// clear the tail if this was the last node in the queue
			if (_headNode == null)
				_tailNode = null;
			
			// Remove the previous head node from the linked list.
			popNode.remove();
			
			// Decrement the queue node count
			_nodeCount--;
		}
		
		return popNode;
	}
	
	
	/**
	 * Adds a new node to the tail of the queue
	 * @param node
	 */
	public void push(DoubleLLNode<T> node) {
		if (_tailNode == null && _headNode == null) {  // First node in the queue
			_headNode = _tailNode = node;
			
		} else if (_tailNode == null || _headNode == null) {  // The queue is corrupted.
			Log.e(LOG_TAG, "push: Queue Linked List tail or head is corrupted! _headNode="+_headNode+" _tailNode="+_tailNode);
			return;
			
		} else { // Adding node to the tail.
			_tailNode.insertNext(node);
			_tailNode = node;
		}
		
		// Increment the number of our nodes in the queue
		_nodeCount++;
	}
	
	
	/**
	 * Returns the current size in nodes of the queue
	 * @return
	 */
	public int size() {
		return _nodeCount;
	}
}
