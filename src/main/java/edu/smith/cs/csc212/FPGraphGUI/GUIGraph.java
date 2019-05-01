package edu.smith.cs.csc212.FPGraphGUI;

import edu.smith.cs.csc212.FPGraph.Graph;
import edu.smith.cs.csc212.FPGraph.WeightedEdgeGraph;

/**
 * Sole existence is that this allow me to write GUIGraph.Node instead of WeightedEdgeGraph<String, Double>.Node, which is longer.
 * I bet there is a better way to do this but I don't know :)<p>
 * 
 * <em>29 April 2019</em> - I added ability to create GUIGraph from existing graph and modify check() so that GUIGraph doesn't allow self connected node UPON CREATION ONLY.
 * I'm only ever going to use this along with the interface thing anyway.<p>
 * 
 * TODO: Ability to read graph from a text file? Gonna be hell, especially with how I structure my edges to store distances between nodes.
 * @author Minh Phuong
 *
 */
public class GUIGraph extends WeightedEdgeGraph<String, Double>{
	
	/**
	 * Construct an empty GUIGraph.
	 */
	public GUIGraph() {
		super();
	}
	
	/**
	 * Create a GUI graph from an existing graph
	 * @param graph - graph to pass in. Value has to be String - Double.
	 */
	public GUIGraph(Graph<String, Double> graph) {
		this();
		for (Node node : graph.getNodes()) {
			addNode(node.getData());
		}
		
		for (Edge edge : graph.getEdges()) {
			addEdge(edge.getData(), edge.getHead(), edge.getTail());
		}
		check();
	}
	
	/**
	 * Check if there's any node in the graph pointing to itself.
	 * @throws IllegalStateException if there's any node pointing to itself.
	 * @throws NullPointerException if there's any {@code null} node (node that IS {@code null}, not node that stores null value).
	 */
	@Override
	public void check() {
		super.check();
		for (Edge edge : edges) {
			if (edge.isSelfConnected()) {
				throw new IllegalStateException("GraphGUI doesn't support nodes that point to itself. Error edge: " + edge);
			}
		}
	}
	
}
