package edu.smith.cs.csc212.FPGraphGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GraphGUI {

	private JLabel instruction;
	
	private GraphComponent graphComponent;
	
	Subject subject = Subject.NODES;
	
	public static void main(String[] args) {
		final GraphGUI GUI = new GraphGUI();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI.createAndShowGUI();
	        }
	    });
	}
	
	private void createAndShowGUI() {
        JFrame f = new JFrame("Graph Interface");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(250,250);
        addComponents(f);
        f.pack();
        f.setVisible(true);
    }
	
	private void addComponents(JFrame f) {
		instruction = new JLabel("Left-click to add nodes, right-click to remove nodes.");
		f.add(instruction, BorderLayout.PAGE_START);
		
		graphComponent = new GraphComponent();
		MouseClickListener mouseListener = new MouseClickListener();
		graphComponent.addMouseListener(mouseListener);
		graphComponent.addMouseMotionListener(mouseListener);
		f.add(graphComponent, BorderLayout.CENTER);
		
		f.add(new ButtonPanel(), BorderLayout.PAGE_END);
	}
	
	public GUIGraph.Node getNodeAt(Point2D p) {
		double eps = GraphComponent.radius;
		for (Entry<GUIGraph.Node, Point2D> pair : graphComponent.nodePosition.entrySet()) {
			if (p.distance(pair.getValue()) < eps) {
				return pair.getKey();
			}
		}
		return null;
	}
	
	enum Subject {NODES, EDGES, EDIT};
	
	private class ButtonPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		private String[] BUTTON_TEXT = new String[] {"Manipulate nodes", "Manipulate edges", "Edit node content"}; 
		
		public ButtonPanel() {
			setLayout(new GridLayout(1, BUTTON_TEXT.length));
			addButtons();
		}
		
		private void addButtons() {
			JButton nodesButton = new JButton(BUTTON_TEXT[0]);
			
			nodesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					subject = Subject.NODES;
					instruction.setText("Left-click to add nodes, right-click to remove nodes.");
				}
			});
			this.add(nodesButton);
			
			JButton edgesButton = new JButton(BUTTON_TEXT[1]);
			edgesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					subject = Subject.EDGES;
					instruction.setText("Drag between two nodes to add an edge or click two nodes to remove edges between them.");
				}
			});
			this.add(edgesButton);
			
			JButton editButton = new JButton(BUTTON_TEXT[2]);
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					subject = Subject.EDIT;
					instruction.setText("Click on a node to edit its content.");
				}
			});
			this.add(editButton);
		}
	}
	
	private class MouseClickListener extends MouseAdapter {
		/**
		 * Used for removing edge.
		 */
		GUIGraph.Node edgeNode = null;
		
		/**
		 * Used for node to drag + start node for edge connecting.
		 */
		GUIGraph.Node startNode;
		
		@Override
		public void mouseClicked(MouseEvent me) {
			Point2D currentPoint = me.getPoint();
			
			// Left-click - add stuff
			if (subject == Subject.NODES) {
				// ADD NODE CASE (LEFT-CLICK)
				edgeNode = null;
				if (me.getButton() == MouseEvent.BUTTON1) {
					graphComponent.addNode(null, currentPoint);
				}
				
				//REMOVE NODE CASE (RIGHT-CLICK)
				else if (me.getButton() == MouseEvent.BUTTON3) {
					graphComponent.removeNode(getNodeAt(currentPoint));
				}
			}
			
			// REMOVE NODE BY CLICKING, ADD NODE BY DRAGGING
			GUIGraph.Node nodeRightNow = getNodeAt(currentPoint);
			if (subject == Subject.EDGES && nodeRightNow != null) {
				// If no previous node is recorded, record the current node.
				if (edgeNode == null) {
					edgeNode = getNodeAt(currentPoint);
				// If there is a previous node recorded, add edges and wipe that node.
				} else {
					graphComponent.removeEdge(edgeNode, getNodeAt(currentPoint));
					edgeNode = null;
				}
			}
			
			// POP UP EDIT CONTENT
			if (subject == Subject.EDIT && nodeRightNow != null) {
				nodeRightNow.setData(JOptionPane.showInputDialog(graphComponent, "Change node content to: ", nodeRightNow.getDataString()));
			}
			graphComponent.repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent me) {
			startNode = getNodeAt(me.getPoint());
		}
		
		@Override
		public void mouseReleased(MouseEvent me) {
			GUIGraph.Node destNode = getNodeAt(me.getPoint());
			
			// ADD NODE BY DRAGGING
			if (subject == Subject.EDGES && startNode != null && destNode != null) {
				graphComponent.addEdge(startNode, destNode);
				graphComponent.repaint();
			}
			startNode = null;
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			Point2D currentPoint = me.getPoint();
			// DRAG NODES
			if (subject == Subject.NODES && startNode != null) {
				graphComponent.updateNodePosition(startNode, currentPoint);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}	
}