package edu.smith.cs.csc212.FPGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Graph data structure!
 * @author Minh Phuong
 *
 * @param <V> vertices type
 * @param <E> edges type
 */
public class Graph<V,E> {

	/**
	 * list of edges
	 */
	protected ArrayList<Edge> edges;
	
	/**
	 * list of nodes
	 */
	protected ArrayList<Node> nodes;
	
	/**
	 * Construct an empty graph.
	 */
	public Graph() {
		this.edges = new ArrayList<Edge>();
		this.nodes = new ArrayList<Node>();
	}
	
	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}
	
	/**
	 * Accessor for node.
	 * @param i - index of node
	 * @return Node at index i
	 */
	public Node getNode(int i) {
		return this.nodes.get(i);
	}
	
	/**
	 * Accessor for edge.
	 * @param i - index of edge
	 * @return Edge at index i
	 */
	public Edge getEdge(int i) {
		return this.edges.get(i);
	}
	
	/**
	 * Get a specific edge from head and tail node.
	 * @param head - head of the edge
	 * @param tail - tail of the edge
	 * @return edge connecting head node and tail node, null if no such edge exists
	 */
	public Edge getEdgeRef(Node head, Node tail) {
		checkNodeValidity(head);
		checkNodeValidity(tail);
		return head.edgeTo(tail);
	}

	/**
	 * Accessor for number of edges
	 * @return number of edges
	 */
	public int numEdges() {
		return edges.size();
	}
	
	/**
	 *  Accessor for number of nodes
	 * @return number of nodes
	 */
	public int numNodes() {
		return nodes.size();
	}
	
	/**
	 * @return the nodes
	 */
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	/**
	 * @return the edges
	 */
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	/**
	 * Add a node (disconnected).
	 * @param data - data stored in a node
	 * @return the node that was added
	 */
	public Node addNode(V data) {
		Node node = new Node(data);
		this.nodes.add(node);
		return node;
	}
	
	/**
	 * Add an edge to current graph. Automatically add nodes to graph if nodes are not already in the graph.
	 * @param data - data stored in node
	 * @param head - node to connect
	 * @param tail - node to connect
	 * @return the edge that was added
	 */
	public Edge addEdge(E data, Node head, Node tail) {
		preventNullNode(head);
		preventNullNode(tail);
		if (!this.hasNode(head)) {
			this.nodes.add(head);
		}
		if (!this.hasNode(tail)) {
			this.nodes.add(tail);
		}
		Edge edge = new Edge(data, head, tail);
		if (!this.hasEdge(edge)) {
			head.addEdgeRef(edge);
			tail.addEdgeRef(edge);
			this.edges.add(edge);
		}
		return edge;
	}
	
	/**
	 * Remove a node from graph. All edges connected to this node are also removed.
	 * @param node to remove
	 * @return {@code true} if remove successfully, {@code false} if node is not in graph.
	 */
	public boolean removeNode(Node node) {
		if (!this.nodes.remove(node)) {
			return false;
		}
		for (Edge edge : node.getEdgeRef()) {
			this.edges.remove(edge);
			if (edge.getHead() != node) {
				edge.getHead().removeEdgeRef(edge);
			} else if (edge.getTail() != node) {
				edge.getTail().removeEdgeRef(edge);
			}
		}
		return true;
	}
	
	/**
	 * Remove an edge
	 * @param edge to remove.
	 * @return {@code true} if remove successfully, {@code false} if edge doesn't exist.
	 */
	public boolean removeEdge(Edge edge) {
		if (!this.edges.remove(edge)) {
			return false;
		}
		edge.getHead().removeEdgeRef(edge);
		if (edge.isSelfConnected()) {
			return true;
		}
		edge.getTail().removeEdgeRef(edge);
		return true;
	}
	
	/**
	 * Remove an edge.
	 * @param head - head node connected to this edge.
	 * @param tail - tail node connected to this edge.
	 * @return {@code true} if remove successfully, {@code false} if edge doesn't exist.
	 */
	public boolean removeEdge(Node head, Node tail) {
		Edge edge = head.edgeTo(tail);
		if (edge == null) {
			return false;
		}
		return this.removeEdge(edge);
	}

	/**
	 * Return nodes not on the given list.
	 * @param group - list of node to exclude
	 * @return Set of nodes not in group
	 */
	public HashSet<Node> otherNodes(HashSet<Node> group) {
		HashSet<Node> result = new HashSet<Node>();
		for (Node node : this.nodes) {
			if (!group.contains(node)) {
				result.add(node);
			}
		}
		return result;
	}
	
	/**
	 * Return endpoints of list of edges
	 * @param edges - list of edges we want to find endpoints of
	 * @return end Nodes of these edges.
	 */
	public HashSet<Node> endpoints(HashSet<Edge> edges) {
		HashSet<Node> result = new HashSet<Node>();
		for (Edge edge : edges) {
			if (!this.hasEdge(edge)) {
				continue;
			}
			Node head = edge.getHead();
			Node tail = edge.getTail();
			if (this.hasNode(head)) {
				result.add(head);
			}
			if (this.hasNode(tail)) {
				result.add(tail);
			}
		}
		return result;
	}
	
	/**
	 * Check if edge exist
	 * @param edge - edge to check
	 * @return {@code true} if edge exists in graph
	 */
	public boolean hasEdge(Edge edge) {
		if (this.edges.contains(edge)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if node exists
	 * @param node - node to check
	 * @return {@code true} if node exists in graph
	 */
	public boolean hasNode(Node node) {
		if (this.nodes.contains(node)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if node exists in this graph.
	 * @param node - node to check.
	 * @throws IllegalArgumentException if node doesn't exist in this graph.
	 */
	protected void checkNodeValidity(Node node) {
		if (!hasNode(node)) {
			throw new IllegalArgumentException("Node: " + node + " doesn't exist.");
		}
	}
	
	protected void preventNullNode(Node node) {
		if (node == null) {
			throw new IllegalArgumentException("Null node not supported.");
		}
	}
	
	/**
	 * Breadth-first traversal of graph.
	 * @param start - Node to start on
	 * @return set of nodes in this graph traversed breadth-first
	 * @throws IllegalArgumentException if start node doesn't exist
	 */
	public HashSet<Node> BFT(Node start) {
		checkNodeValidity(start);
		HashSet<Node> visited = new HashSet<Node>();
		LinkedList<Node> toVisit = new LinkedList<Node>();
		toVisit.offerLast(start);
		
		while (!toVisit.isEmpty()) {
			Node here = toVisit.removeFirst();
			visited.add(here);
			for (Node neighbor : here.getNeighbors()) {
				if (!visited.contains(neighbor)) {
					toVisit.offerLast(neighbor);
				}
			}
		}
		return visited;
	}
	
	/**
	 * Depth-first traversal of graph.
	 * @param start - Node to start on
	 * @return set of nodes in this graph traversed depth-first
	 * @throws IllegalArgumentException if start node doesn't exist
	 */
	public HashSet<Node> DFT(Node start) {
		checkNodeValidity(start);
		HashSet<Node> visited = new HashSet<Node>();
		LinkedList<Node> toVisit = new LinkedList<Node>();
		toVisit.offerLast(start);
		
		while (!toVisit.isEmpty()) {
			Node here = toVisit.removeFirst();
			visited.add(here);
			for (Node neighbor : here.getNeighbors()) {
				if (!visited.contains(neighbor)) {
					toVisit.offerFirst(neighbor);
				}
			}
		}
		return visited;
	}
	
	/**
	 * Dijkstra's shortest-path algorithm to compute distances to nodes
	 * @param start - where to start from
	 * @throws IllegalArgumentException if edges are not of Number type.
	 */
	public HashMap<Node, Double> distances(Node start) {
		throw new IllegalArgumentException("To store weighted edges please use WeightedEdgeGraph.");
	}
	
	/**
	 * Dijkstra's shortest-path algorithm to compute distances between two nodes
	 * @param start - node to start from
	 * @param end - destination
	 * @throws IllegalArgumentException if edges are not of Number type.
	 */
	public ArrayList<Graph<String, Double>.Edge> distanceBetween(Node start, Node end) {
		throw new IllegalArgumentException("To store weighted edges please use WeightedEdgeGraph.");
	}
	
	/** 
     * Prints a representation of the graph O(n^3).
     */
     public void print() {
    	 if (numNodes() == 0) {
    		 return;
    	 }
    	 String nTab = "\t\t";
    	 for (Node node : this.nodes) {
    		 System.out.print(nTab + node.getData());
    	 }
    	 for (int i=0; i<nodes.size(); i++) {
    		 System.out.println();
    		 System.out.print(nodes.get(i).getData() + nTab);
    		 
    		 Node col = nodes.get(i);
    		 for (int j=0; j<nodes.size(); j++) {
    			 Node row = nodes.get(j);
    			 Edge edge = col.edgeTo(row);
    			 if (edge != null) {
    				 String edgeValue = edge.getData() == null ? "null" : edge.getData().toString();
    				 System.out.print(edgeValue + nTab);
    				 continue;
    			 }
    			 System.out.print("null" + nTab);
    		 }
    	 }
    	 System.out.println();
     }
     
     /**
      * Check if edges points to null and check if graph contains null node.
      */
     public void check() {
    	 for (int i=0; i<numEdges(); i++) {
    		 if (edges.get(i).getHead() == null || edges.get(i).getTail() == null) {
    			 throw new NullPointerException("Edge at position " + i + " doesn't point to anywhere.");
    		 }
    	 }
    	 for (int i=0; i<numNodes(); i++) {
    		 if (nodes.get(i) == null) {
    			 throw new NullPointerException("Node at position " + i + " is null.");
    		 }
    	 }
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
		public Node getHead() { return head; }
		
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
		
		public boolean isSelfConnected() {
			return this.head == this.tail;
		}
	
		@Override
		public int hashCode() {
			return Objects.hash(head, tail) + Objects.hash(tail, head);
		}
		
		@Override
		public String toString() {
			String headStr = this.head == null ? "null" : head.toString();
			String tailStr = this.tail == null ? "null" : tail.toString();
			String dataStr = this.data == null ? "null" : data.toString();
			return "<" + headStr + "--->" + tailStr + ">: " + dataStr;
		}
	}
	
	/**
	 * Implement node in a graph
	 * @author Minh Phuong
	 *
	 */
	public class Node {
		/**
		 * data stored in this node
		 */
		private V data;
		
		/**
		 * List of edges connected to this node
		 */
		private HashSet<Edge> edgeRef;
		
		/**
		 * Create a new disconnected node
		 * @param data - data to store in Node
		 */
		public Node(V data) {
			this.data = data;
			this.edgeRef = new HashSet<Edge>();
		}
		
		/**
		 * Setter for data stored in node
		 * @param data - data to set
		 */
		public void setData(V data) { this.data = data; }
		
		/**
		 * Getter for data stored in this node
		 * @return data stored in this node
		 */
		public V getData() { return this.data; }
		
		/**
		 * 
		 */
		public String getDataString() { return this.data == null ? "null" : this.data.toString(); }
		
		/**
		 * Getter for edges connected to this node
		 * @return edges connected to this node
		 */
		public HashSet<Edge> getEdgeRef() { return this.edgeRef; }
		
		/**
		 * Return a list of neighbor nodes
		 * @return a list of neighbor nodes
		 */
		public LinkedList<Node> getNeighbors() {
			LinkedList<Node> neighbors = new LinkedList<Node>();
			
			for (Edge edge : this.edgeRef) {
				if (edge.getHead() == this) {
					neighbors.offerLast(edge.getTail());
				} else if (edge.getTail() == this) {
					neighbors.offerLast(edge.getHead());
				}
			}
			return neighbors;
		}
		
		/**
		 *  Returns true if there is an edge to the node in question
		 * @param node - node to check neighbor status
		 * @return {@code false} if not neighbor, {@code true} if is neighbor
		 */
		public boolean isNeighbor(Node node) {
			return getNeighbors().contains(node);
		}
		
		/**
		 * Returns the edge to a specified node, or null if there is none
		 * @param neighbor - the Node we are checking
		 * @return the edge connecting this node to neighbor node
		 */
		public Edge edgeTo(Node neighbor) {
			Edge test = new Edge(null, this, neighbor);
			for (Edge edge : this.edgeRef) {
				if (edge.equals(test)) {
					return edge;
				}
			}
			return null;
		}
		
		/**
		 * Adds an edge to the edge list
		 * @param edge - edge to add
		 * @throws IllegalArgumentException if edge is not connected to this node
		 */
		protected void addEdgeRef(Edge edge) {
			if (edge.getHead() == this || edge.getTail() == this) {
				this.edgeRef.add(edge);
				return;
			}
			throw (new IllegalArgumentException ("Can't add: Edge " + edge + " does not connect to this node " + this.toString() +"."));
		}
		
		/**
		 * Remove an edge from the edge list
		 * @param edge - edge to remove
		 * @throws IllegalArgumentException if edge is not connected to this node
		 */
		protected void removeEdgeRef(Edge edge) {
			if (!this.edgeRef.remove(edge)) {
				throw (new IllegalArgumentException ("Can't remove: Edge " + edge + " from this node " + this.toString() +"."));
			}
		}
		
		@Override
		public String toString() {
			return "Node@" + Integer.toHexString(hashCode()) + "=" + getDataString();
		}
	}
}
