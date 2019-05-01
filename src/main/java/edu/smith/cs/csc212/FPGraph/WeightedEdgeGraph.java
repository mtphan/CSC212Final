package edu.smith.cs.csc212.FPGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * Graph to store weighted edge. Allows computing distances using Dijkstra's algorithm and that's it.
 * @author Minh Phuong
 *
 * @param <V> - vertice type. Any type
 * @param <E> - edge type. Has to extends number.
 */
public class WeightedEdgeGraph<V, E extends Number> extends Graph<V, E>{

	/**
	 * Dijkstra's shortest-path algorithm to compute distances to nodes
	 * @param start - where to start from
	 * @throws IllegalArgumentException if start node is not in graph
	 * @return a HashMap of Node vs corresponding distance of that node from source node.
	 * Distance is default to {@link Double.POSITIVE_INFINITY} if two node is not connected.
	 * @bug Can't handle edges with value larger than {@link Double.POSITIVE_INFINITY}.
	 */
	public HashMap<Node, Double> distances(Node start) {
		checkNodeValidity(start);
		
		HashMap<Node, Double> state = new HashMap<Node, Double>(numNodes());
		HashSet<Node> notVisited = new HashSet<Node>();
		
		// Initialize all node with Infinity value, add all not to "not visited" state
		nodes.stream().forEach((Node node) -> {
			state.put(node, Double.POSITIVE_INFINITY);
			notVisited.add(node);
		});
		// Set distance to start node as 0.0
		state.replace(start, 0.0);
		
		// While there're still node to explore
		while (!notVisited.isEmpty()) {
			// Find the node with smallest distance and set it as current node
			Node current = setCurrentNode(state, notVisited);
			// Loop through all edges connected to current node
			HashSet<Edge> edgeRef = current.getEdgeRef();
			for (Edge edge : edgeRef) {
				// Find neighbour node
				Node neighbor = edge.getHead() == current ? edge.getTail() : edge.getHead();
				// Compare the distance stored at neighbour node vs distance at current node + edge distance.
				Double compare = state.get(current) + edge.getData().doubleValue();
				if (compare < state.get(neighbor)) {
					state.put(neighbor, compare);
				}
			}
			notVisited.remove(current);
		}	
		return state;
	}

	/**
	 * Dijkstra's shortest-path algorithm to compute paths between two nodes
	 * @param start - node to start from
	 * @param end - destination
	 * @throws IllegalArgumentException if start node or end node doesn't belong to graph.
	 * @return A list of edges that make up the shortest path between two nodes
	 */
	public ArrayList<Edge> pathBetween(Node start, Node end) {
		checkNodeValidity(start);
		checkNodeValidity(end);

		HashMap<Node, Double> state = new HashMap<Node, Double>(numNodes());
		HashSet<Node> notVisited = new HashSet<Node>();
		HashMap<Node, ArrayList<Edge>> paths = new HashMap<>(numNodes());
		
		nodes.stream().forEach((Node node) -> {
			state.put(node, Double.POSITIVE_INFINITY);
			notVisited.add(node);
			paths.put(node, new ArrayList<Edge>());
		});
		state.replace(start, 0.0);
		Node current = start;
		
		while (current != end && !notVisited.isEmpty()) {
			current = setCurrentNode(state, notVisited);
			HashSet<Edge> edgeRef = current.getEdgeRef();
			for (Edge edge : edgeRef) {
				Node neighbor = edge.getHead() == current ? edge.getTail() : edge.getHead();
				Double compare = state.get(current) + edge.getData().doubleValue();
				if (compare < state.get(neighbor)) {
					state.put(neighbor, compare);
					ArrayList<Edge> currentPath = new ArrayList<>(paths.get(current));
					currentPath.add(edge);
					paths.put(neighbor, currentPath);
				}
			}
			notVisited.remove(current);
		}	
		
		return paths.get(end);
	}
	
	/**
	 * Find the node with smallest distance and set it as current node. Used for {@link distances} method.
	 * @param state - HashMap of nodes vs distances
	 * @param notVisited - nodes that weren't visited
	 * @return node to set to current
	 */
	private Node setCurrentNode(HashMap<Node, Double> state, HashSet<Node> notVisited) {
		Double min = Double.POSITIVE_INFINITY;
		Node minNode = new Node(null);
		for (Entry<Node, Double> pair : state.entrySet()) {
			Node node = pair.getKey();
			if (notVisited.contains(node) && pair.getValue() <= min) {
				minNode = node;
				min = pair.getValue();
			}
		}
		return minNode;
	}
}
