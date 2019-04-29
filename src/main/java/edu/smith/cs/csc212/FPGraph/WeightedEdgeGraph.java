package edu.smith.cs.csc212.FPGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
	 * @return a HashMap of Node vs corresponding distance of that node from source node.
	 * Distance is default to {@link Double.POSITIVE_INFINITY} if two node is not connected.
	 * @bug Can't handle edges with value larger than {@link Double.POSITIVE_INFINITY}.
	 */
	@Override
	public HashMap<Node, Double> distances(Node start) {
		checkNodeValidity(start);
		
		HashMap<Node, Double> state = new HashMap<Node, Double>(numNodes());
		HashSet<Node> notVisited = new HashSet<Node>();
		
		nodes.stream().forEach((Node node) -> {
			state.put(node, Double.POSITIVE_INFINITY);
			notVisited.add(node);
		});
		state.replace(start, 0.0);
		
		while (!notVisited.isEmpty()) {
			Node current = setCurrentNode(state, notVisited);
			LinkedList<Node> neighbors = current.getNeighbors();
			for (Node neighbor : neighbors) {
				Double currentD = state.get(neighbor);
				Double compare = state.get(current) + neighbor.edgeTo(current).getData().doubleValue();
				if (compare < currentD) {
					state.put(neighbor, compare);
				}
			}
			notVisited.remove(current);
		}	
		return state;
	}
	
	@Override
	public ArrayList<Graph<String, Double>.Edge> distanceBetween(Node start, Node end) {
		//TODO: Implement distance between maybe?
		return null;
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