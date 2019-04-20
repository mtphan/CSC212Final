package edu.smith.cs.csc212.FPGraph;

import java.util.LinkedList;

/**
 * Graph data structure!
 * @author Minh Phuong
 *
 * @param <V> vertices
 * @param <E> edges
 */
public class Graph<V,E> {

	/**
	 * list of edges
	 */
	protected LinkedList<Edge> edges;
	
	/**
	 * list of nodes
	 */
	protected LinkedList<Node> nodes;
	
	public Graph() {
		this.edges = new LinkedList<Edge>();
		this.nodes = new LinkedList<Node>();
	}
	
	/**
	 * Implements an edge in a graph
	 * @author Minh Phuong
	 *
	 */
	public class Edge {
		/**
		 * Value stored in edge
		 */
		private E data;
		
		/**
		 * Head node
		 */
		private Node head;
		
		/**
		 * Tail node
		 */
		private Node tail;
		/**
		 * Create a new edge.
		 * @param data Value of edge
		 * @param head The node before
		 * @param tail The node after
		 */
		public Edge(E data, Node head, Node tail) {
			this.data = data;
			this.head = head;
			this.tail = tail;
		}
		
		/**
		 * Getter for edge's value
		 * @return value at edge
		 */
		public E getData() { return data; }
		
		/**
		 * Getter for head node
		 * @return head node (endpoint #1)
		 */
		public Node getHead() {	return head; }
		
		/**
		 * Getter for tail node
		 * @return tail node (endpoint #2)
		 */
		public Node getTail() { return tail; }
		
		/**
		 * Setter for data
		 * @param data - data to set
		 */
		public void setData(E data) { this.data = data; }
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object o) {
			if (o instanceof Graph.Edge) {
				Node compareHead = ((Graph<V,E>.Edge) o).getHead();
				Node compareTail = ((Graph<V,E>.Edge) o).getTail();
				return ((compareHead == this.head  && 
						 compareTail == this.tail) ||
						(compareHead == this.tail  &&
						 compareTail == this.head));
			}
			return false;
		}
	}
	
	public class Node {
		
	}
}
