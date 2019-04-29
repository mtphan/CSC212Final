package edu.smith.cs.csc212.FPGraphGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.HashMap;
import javax.swing.JPanel;

public class GraphComponent extends JPanel {

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
	protected static final int radius = 20;
	
	public GraphComponent() {
		this.graph = new GUIGraph();
		nodePosition = new HashMap<>();
		setBackground(Color.white);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (GUIGraph.Edge edge : graph.getEdges()) {
			drawEdge(g2D, edge);
		}
		
		for (GUIGraph.Node node : graph.getNodes()) {
			drawNode(g2D, node);
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
		if (head == tail) {
			return;
		}
		graph.addEdge(getDistanceBetween(head, tail), head, tail);
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
			Double distance = getDistanceBetween(edge.getHead(), edge.getTail());
			edge.setData(distance);
		};
		this.repaint();
	}
	
	/**
	 * A method to draw node
	 * @param g2D - canvas
	 * @param node - node to draw
	 */
	private void drawNode(Graphics2D g2D, GUIGraph.Node node) {
		Double x = nodePosition.get(node).getX();
		Double y = nodePosition.get(node).getY();
		
		Ellipse2D nodeCircle = new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2);	
		g2D.setColor(Color.black);
		g2D.draw(nodeCircle);
		g2D.setColor(Color.red);
		g2D.fill(nodeCircle);
		
		g2D.setColor(Color.white);
		String data = node.getDataString().toUpperCase();
		drawCenteredString(g2D, data.length() >= 4 ? data.substring(0,4) : data, nodeCircle, new Font("TimesRoman", Font.BOLD, 10));
	}
	
	/**
	 * 
	 * @param g - canvas
	 * @param text - text that will be center
	 * @param circle - circle that text will be centered around
	 * @param font - the font we want to use
	 */
	private static void drawCenteredString(Graphics2D g, String text, Ellipse2D circle, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = (int) circle.getCenterX() - metrics.stringWidth(text) / 2;
	    int y = (int) circle.getCenterY() - (metrics.getHeight() / 2) + metrics.getAscent();
	    g.setFont(font);
	    g.drawString(text, x, y);
	}
	
	/**
	 * A method to draw edge
	 * @param g2D - canvas
	 * @param edge - edge to draw
	 */
	private void drawEdge(Graphics2D g2D, GUIGraph.Edge edge) {
		Point2D start = nodePosition.get(edge.getHead());
		Point2D end = nodePosition.get(edge.getTail());
		Shape edgeLine = new Line2D.Double(start, end);
		g2D.setColor(Color.black);
		g2D.draw(edgeLine);
	}
	
	/**
	 * Return distance between two nodes. Allow me to manipulate distance from one place.
	 * @param start - start node
	 * @param end - end node
	 * @return distance between two nodes (pixels/c - c being a constant)
	 */
	private double getDistanceBetween(GUIGraph.Node start, GUIGraph.Node end) {
		return nodePosition.get(start).distance(nodePosition.get(end))/100;
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
