package edu.smith.cs.csc212.FPGraphGUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.HashSet;

import edu.smith.cs.csc212.FPGraph.WeightedEdgeGraph;

/**
 * Graph Panel. Control and display internal graph or something.
 * @author Minh Phuong
 *
 */
public class GUIGraph extends WeightedEdgeGraph<String, Double> {
	
	/**
	 * Toggle information display on the canvas. True at start-up.
	 */
	protected boolean toggleNodeData, toggleEdgeData;
	
	/**
	 * Graph of node vs its position.
	 */
	protected HashMap<GUIGraph.Node, Point2D> nodePosition;
	
	/**
	 * 
	 */
	protected HashSet<GUIGraph.Edge> highlightEdges;
	
	/**
	 * Radius of node.
	 */
	protected final int radius = 20;
	
	/**
	 * Create a canvas to draw graph on
	 */
	public GUIGraph() {
		super();
		nodePosition = new HashMap<>();
		highlightEdges = new HashSet<>();
		toggleNodeData = true;
		toggleEdgeData = true;
	}
	
	public void draw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (GUIGraph.Edge edge : this.edges) {
			drawEdge(g, edge, highlightEdges.contains(edge));
		}
		
		for (GUIGraph.Node node : this.nodes) {
			drawNode(g, node, nodePosition.get(node).getX(), nodePosition.get(node).getY());
		}
		if (!highlightEdges.isEmpty()) highlightEdges.clear();
	}
	
	/**
	 * Remove node in this graph.
	 * @param node - node to remove.
	 */
	@Override
	public boolean removeNode(Node node) {
		if (!super.removeNode(node)) {
			return false;
		};
		nodePosition.remove(node);
		return true;
	}
	
	/**
	 * Add node in this graph.
	 * @param data - data of node.
	 * @param p - position of node.
	 */
	public void addNode(String data, Point2D p) {
		GUIGraph.Node newNode = super.addNode(data);
		nodePosition.put(newNode, p);
	}
	
	/**
	 * Add an edge to this graph. Value of edge is the distance between two nodes
	 * @param head - head node
	 * @param tail - tail node
	 */
	public void addEdge(Node head, Node tail) {
		if (head == tail) {
			return;
		}
		super.addEdge(getDistanceBetween(head, tail), head, tail);
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
	}
	
	/**
	 * Highlight the shortest path between two node found by Dijkstra's algorithm
	 * @param start - node to start on
	 * @param end - node to end on
	 */
	protected void highlightPathBetween(GUIGraph.Node start, GUIGraph.Node end) {
		highlightEdges.addAll(this.pathBetween(start, end));
	}
	
	/**
	 * A method to draw node
	 * @param g2D - canvas
	 * @param node - node to draw
	 * @param x - x position to draw node
	 * @param y - y position to draw node
	 */
	private void drawNode(Graphics2D g, GUIGraph.Node node, double x, double y) {
		Graphics2D g2D = (Graphics2D) g.create();
		Ellipse2D nodeCircle = new Ellipse2D.Double(x-radius, y-radius, radius*2, radius*2);	
		g2D.setColor(Color.black);
		g2D.draw(nodeCircle);
		g2D.setColor(Color.red);
		g2D.fill(nodeCircle);
		
		g2D.setColor(Color.white);
		String data = node.getDataString();
		if (toggleNodeData)
			drawCenteredString(g2D, null, data.length() >= 4 ? data.substring(0,4) : data, nodeCircle.getBounds2D(), new Font("TimesRoman", Font.BOLD, 10));
		g2D.dispose();
	}
	
	/**
	 * A method to draw edge + its data.
	 * @param g2D - canvas
	 * @param edge - edge to draw
	 */
	private void drawEdge(Graphics2D g, GUIGraph.Edge edge, boolean needHighlighting) {
		Graphics2D g2D = (Graphics2D) g.create();
		Point2D start = nodePosition.get(edge.getHead());
		Point2D end = nodePosition.get(edge.getTail());
		Shape edgeLine = new Line2D.Double(start, end);
		g2D.setColor(Color.black);
		if (needHighlighting) {
			g2D.setColor(Color.blue);
			g2D.setStroke(new BasicStroke(2f));
		}
		g2D.draw(edgeLine);
		if (toggleEdgeData) {
			g2D.setColor(Color.red.darker());
			double slope = (double) (start.getY() - end.getY())/(start.getX() - end.getX());
			drawCenteredString(g2D, Math.atan(slope), String.format("%.4f", edge.getData()), edgeLine.getBounds2D(), new Font("TimesRoman", Font.BOLD, 15));
		}
		g2D.dispose();
	}
	
	/**
	 * Draw string at the very center of node. I have to do this or else it won't be centered. Very annoying >:<
	 * @param g - canvas
	 * @param text - text that will be center
	 * @param rectangle - I initially used an ellipse, but any shapes can be converted to a rectangle, so it's a better choice.
	 * @param font - the font we want to use
	 */
	private static void drawCenteredString(Graphics2D g, Double theta, String text, Rectangle2D rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    g.translate(rect.getCenterX(), rect.getCenterY());
	    int x = - metrics.stringWidth(text) / 2;
	    int y = - (metrics.getHeight() / 2) + metrics.getAscent();
	    g.setFont(font);
	    // Theta = null for nodes. Theta =/= null for edges.
	    if (theta != null) {
	    	g.rotate(theta);
	    	g.translate(0, -10);
	    }
	    g.drawString(text, x, y);
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
}
