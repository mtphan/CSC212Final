package edu.smith.cs.csc212.FPGraphGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.HashMap;
import javax.swing.JComponent;

public class GraphComponent extends JComponent {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Internal graph
	 */
	protected GUIGraph graph;
	
	/**
	 * Graph of node vs its position.
	 */
	protected HashMap<GUIGraph.Node, Point2D> nodePosition;
	

	/**
	 * Radius of node.
	 */
	protected static final int radius = 15;
	
	public GraphComponent() {
		this.graph = new GUIGraph();
		nodePosition = new HashMap<>();
		setBackground(Color.white);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		g2D.setBackground(Color.white);
		g2D.clearRect(getX(), getY(), getWidth(), getHeight());
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (GUIGraph.Node node : graph.getNodes()) {
			Double x = nodePosition.get(node).getX();
			Double y = nodePosition.get(node).getY();
			
			Shape nodeCircle = new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2);	
			g2D.setColor(Color.black);
			g2D.draw(nodeCircle);
			g2D.setColor(Color.yellow);
			g2D.fill(nodeCircle);
		}
		
		for (GUIGraph.Edge edge : graph.getEdges()) {
			if (edge.isSelfConnected()) {
				continue;
			}
			Point2D start = nodePosition.get(edge.getHead());
			Point2D end = nodePosition.get(edge.getTail());
			Shape edgeLine = new Line2D.Double(start, end);
			g2D.setColor(Color.black);
			g2D.draw(edgeLine);
		}
	}
	
	/**
	 * Remove node in this graph.
	 * @param node - node to remove.
	 */
	public void removeNode(GUIGraph.Node node) {
		if (!graph.removeNode(node)) {
			return;
		};
		nodePosition.remove(node);
	}
	
	/**
	 * Add node in this graph.
	 * @param data - data of node.
	 * @param p - position of node.
	 */
	public void addNode(String data, Point2D p) {
		GUIGraph.Node newNode = graph.addNode(data);
		nodePosition.put(newNode, p);
	}
	
	/**
	 * Add an edge to this graph. Value of edge is the distance between two nodes
	 * @param head - head node
	 * @param tail - tail node
	 */
	public void addEdge(GUIGraph.Node head, GUIGraph.Node tail) {
		Double distance = nodePosition.get(head).distance(nodePosition.get(tail));
		graph.addEdge(distance, head, tail);
	}
	
	/**
	 * Remove an edge from this graph.
	 * @param head - head node of said edge
	 * @param tail - tail node of said edge
	 */
	public void removeEdge(GUIGraph.Node head, GUIGraph.Node tail) {
		graph.removeEdge(head, tail);
	}
	
	/**
	 * Update Node position for dragging nodes around
	 * @param node - node to update
	 * @param p - point to update node to
	 */
	protected void updateNodePosition(GUIGraph.Node node, Point2D p) {
		nodePosition.put(node, p);
		for (GUIGraph.Edge edge : node.getEdgeRef()) {
			Double distance = nodePosition.get(edge.getHead()).distance(nodePosition.get(edge.getTail()));
			edge.setData(distance);
		};
		this.repaint();
	}
	
	@Override
    public Dimension getMinimumSize() {
        return new Dimension(500,300);
    }
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 500);
	}
}
