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
		MouseAdapter mouseListener = new MouseClickListener();
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
	
	enum Subject {NODES, EDGES};
	
	private class ButtonPanel extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		private String[] BUTTON_TEXT = new String[] {"Manipulate nodes", "Manipulate edges"}; 
		
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
					instruction.setText("Click two nodes to add edges, right-click to remove edges.");
				}
			});
			this.add(edgesButton);
		}
	}
	
	private class MouseClickListener extends MouseAdapter {
		GUIGraph.Node edgeNode = null;
		GUIGraph.Node dragNode;
		
		@Override
		public void mouseClicked(MouseEvent me) {
			Point2D currentPoint = me.getPoint();
			// Left-click - add stuff
			if (subject == Subject.NODES) {
				// ADD NODE CASE
				edgeNode = null;
				if (me.getButton() == MouseEvent.BUTTON1) {
					graphComponent.addNode(null, currentPoint);
				}
				
				//REMOVE NODE CASE
				if (me.getButton() == MouseEvent.BUTTON3) {
					graphComponent.removeNode(getNodeAt(currentPoint));
				}
			}
			
			GUIGraph.Node nodeRightNow = getNodeAt(currentPoint);
			if (subject == Subject.EDGES && nodeRightNow != null) {
				if (edgeNode == null) {
					edgeNode = getNodeAt(currentPoint);
				} else {
					if (me.getButton() == MouseEvent.BUTTON1) {
						graphComponent.addEdge(edgeNode, getNodeAt(currentPoint));
					}
					if (me.getButton() == MouseEvent.BUTTON3) {
						graphComponent.removeEdge(edgeNode, getNodeAt(currentPoint));
					}
					edgeNode = null;
				}
			}
			graphComponent.repaint();
		}
		
		@Override
		public void mousePressed(MouseEvent me) {
			Point2D currentPoint = me.getPoint();
			dragNode = getNodeAt(currentPoint);
		}
		
		@Override
		public void mouseReleased(MouseEvent me) {
			dragNode = null;
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			Point2D currentPoint = me.getPoint();
			if (subject == Subject.NODES && dragNode != null) {
				graphComponent.updateNodePosition(dragNode, currentPoint);
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