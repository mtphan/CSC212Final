package edu.smith.cs.csc212.FPGraphGUI;

import edu.smith.cs.csc212.FPGraph.Graph;
import edu.smith.cs.csc212.FPGraph.WeightedEdgeGraph;

/**
 * Sole existence is that this allow me to write GUIGraph.Node instead of WeightedEdgeGraph<String, Double>.Node, which is longer.
 * I bet there is a better way to do this but I don't know :)
 * @author Minh Phuong
 *
 */
public class GUIGraph extends WeightedEdgeGraph<String, Double>{

	public GUIGraph() {
		super();
	}
	
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

	public void check() {
		super.check();
		for (Edge edge : edges) {
			if (edge.isSelfConnected()) {
				throw new IllegalStateException("GraphGUI doesn't support nodes that point to itself. Error edge: " + edge);
			}
		}
	}
	
}
