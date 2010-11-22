package com.midlandroid.apps.android.gameengine.v1.custcollections;

import android.util.Log;

/**
 * Stack collection with a linked list for nodes.
 * @author Jason Del Ponte
 *
 * @param <T> class type of data
 */
public class LinkedListStack<T> {
	private static final String LOG_TAG = LinkedListStack.class.getSimpleName();
	
	private final Class<T> _dataCls;
	
	private int _nodeCount;
	private DoubleLLNode<T> _headNode;
	
	
	/**
	 * Create a new instance of this stack, and initialize
	 * the node data class type that will be used during allocations.
	 * 
	 * @param dataCls
	 */
	public LinkedListStack(final Class<T> dataCls) {
		_dataCls = dataCls;
	}
	
	
	/**
	 * Allocates a specific number of nodes that will be added to the
	 * stack of nodes.  
	 * @param count The number of nodes to be created
	 * @param dataCls  The class type of the data object
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
			
			// Add the node to the stack
			push(node);
		}
	}
	
	
	/**
	 * Releases all known references to the nodes.
	 */
	public void freeNodes() {
		// Loop over all nodes popping each from the stack
		// removing our reference from the them.
		while (_headNode != null) {
			pop();
		}
	}
		
	
	/**
	 * Removes the current head available node from the stack and
	 * returns a reference to it.
	 * @return
	 */
	public DoubleLLNode<T> pop() {
		DoubleLLNode<T> popNode = null;
		
		if (_headNode != null) {
			popNode = _headNode;
			DoubleLLNode<T> nextNode = popNode.getNext();
			
			// Update the current head node
			_headNode = nextNode;
			
			// Clear the popped node's references and linkage
			popNode.remove();
			
			// Decrement the number of available nodes, and return the previous head
			_nodeCount--;
		}
		
		return popNode;
	}
	
	
	/**
	 * Adds a node to the head of the available node stack.
	 * @param node
	 */
	public void push(DoubleLLNode<T> node) {
		DoubleLLNode<T> prevHeadNode = _headNode;
		
		// If the current head is valid add the new node before it
		if (prevHeadNode != null) {
			prevHeadNode.insertPrev(node);
		}
		
		// Update available node head and count
		_headNode = node;
		_nodeCount++;
	}
	
	
	/**
	 * Returns the current size in nodes of the stack
	 * @return
	 */
	public int size() {
		return _nodeCount;
	}
}
