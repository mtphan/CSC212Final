package edu.smith.cs.csc212.FPGraphGUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Interface of a graph. I'm too tired to write meaningful things right now.
 * @author Minh Phuong
 *
 */
public class GraphGUI {
	
	/**
	 * An instruction that appears at the top to tell the user what to do.
	 */
	private JLabel instruction;
	
	/**
	 * A JPanel where graphs are drawn.
	 * It is a panel because I initially wanted Nodes and Edges to be JComponent of this panel, but then I found that it was too much of a hassle.
	 */
	private GraphCanvas graphCanvas;
	
	/**
	 * Internal graph.
	 */
	private GUIGraph graph;
	
	/**
	 * A variable which tells me what mode I'm on right now.
	 * @see {@link Subject}
	 */
	Subject subject;
	
	public static void main(String[] args) {
		final GraphGUI GUI = new GraphGUI();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI.createAndShowGUI();
	        }
	    });
	}

	/**
	 * Show the interface. Apparently I have to do this because Swing is not "thread-safe", whatever that means.
	 */
	private void createAndShowGUI() {
        JFrame f = new JFrame("Graph Interface");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
        addComponents(f);
        f.pack();
        f.setVisible(true);
    }
	
	/**
	 * Add all components to frame f
	 * @param f - the frame, duh.
	 */
	private void addComponents(JFrame f) {
		graphCanvas = new GraphCanvas();
		graph = new GUIGraph();
		instruction = new JLabel("WELCOME TO GRAPH INTERFACE! CLICK ANY BUTTONS TO SEE INSTRUCTIONS!", JLabel.CENTER);
		subject = Subject.NODES;
		
		f.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		instruction.setForeground(Color.BLUE.darker());
		layout.fill = GridBagConstraints.HORIZONTAL;
		layout.weightx = 0.5; layout.weighty = 0;
		layout.gridx = 0; layout.gridy = 0;
		layout.insets = new Insets(10,10,0,0);
		f.add(instruction, layout);
		layout.gridy = 1;
		f.add(Box.createRigidArea(new Dimension(40, 0)));
		
		MouseClickListener mouseListener = new MouseClickListener();
		graphCanvas.addMouseListener(mouseListener);
		graphCanvas.addMouseMotionListener(mouseListener);

		layout.insets = new Insets(10,10,10,10);
		layout.weightx = 0.8; layout.weighty = 1;
		layout.gridx = 0; layout.gridy = 1;
		layout.fill = GridBagConstraints.BOTH;
		f.add(graphCanvas, layout);
		
		layout.weightx = 0; layout.weighty = 1;
		layout.gridx = 1; layout.gridy = 1;
		f.add(new ButtonPanel(), layout);
		
		layout.insets = new Insets(0,10,0,0);
		layout.weightx = 1; layout.weighty = 0;
		layout.gridx = 0; layout.gridy = 2;
		f.add(new CheckboxPanel(), layout);
	}
	
	/**
	 * Return node residing at a point.
	 * @param p - point in question
	 * @return the Node at the point in question.
	 */
	public GUIGraph.Node getNodeAt(Point2D p) {
		double eps = graph.radius;
		for (Entry<GUIGraph.Node, Point2D> pair : graph.nodePosition.entrySet()) {
			if (p.distance(pair.getValue()) < eps) {
				return pair.getKey();
			}
		}
		return null;
	}
	
	/**
	 * Constant for modes. Named subject because initially it only has NODES and EDGES, which ARE subjects.
	 * @author Minh Phuong
	 *
	 */
	enum Subject {NODES, EDGES, EDIT, DISTANCE};
	
	private class GraphCanvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public GraphCanvas() {
			super();
			setBackground(Color.white);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			graph.draw((Graphics2D) g); 
		}
		
		@Override
	    public Dimension getMinimumSize() {
	        return new Dimension(500,300);
	    }
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(800, 500);
		}
	}
	
	/**
	 * Button Panel, where all button resides, <em>Display Node Content</em> and <em>Display Edge Content</em>. Should I make it {@code static}?
	 * It might be a better idea to use JRadioButton instead, but I have found that JButton looks nicer.
	 * @author Minh Phuong
	 */
	private class ButtonPanel extends JPanel {
		
		/**
		 * Dunno what this is.
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Create a button panel with 4 buttons
		 */
		public ButtonPanel() {
			setLayout(new GridBagLayout());
			addButtons();
		}
		
		GridBagConstraints layout = new GridBagConstraints();
		/**
		 * A function to add buttons to current panel.
		 */
		private void addButtons() {
			
			JButton nodesButton = new JButton("Manipulate nodes");
			
			nodesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					edgeNode = null;
					subject = Subject.NODES;
					instruction.setText("Left-click to add nodes, right-click to remove nodes.");
				}
			});
			
			JButton edgesButton = new JButton("Manipulate edges");
			edgesButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					edgeNode = null;
					subject = Subject.EDGES;
					instruction.setText("Drag between two nodes to add an edge or click two nodes to remove edges between them.");
				}
			});
			
			JButton editButton = new JButton("Edit node content");
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					edgeNode = null;
					subject = Subject.EDIT;
					instruction.setText("Click on a node to edit its content.");
				}
			});
			
			JButton distanceButton = new JButton("Get shortest path");
			distanceButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					edgeNode = null;
					subject = Subject.DISTANCE;
					instruction.setText("Select two nodes to find shortest paths.");
				}
			});
			
			int y = 0;
			y = addAButton(nodesButton, y);
			y = addAButton(edgesButton, y);
			y = addAButton(editButton, y);
			y = addAButton(distanceButton, y);
		}
		/**
		 * Format adding button I guess.
		 * @param button - button to add
		 */
		private int addAButton(JButton button, int y) {
			layout.gridx = 0; layout.gridy = y; layout.gridheight = 2;
			layout.insets = new Insets(10,10,10,10);
			layout.weighty = 1;
			layout.fill = GridBagConstraints.REMAINDER;
			this.add(Box.createRigidArea(new Dimension(0, 100)));
			
			layout.fill = GridBagConstraints.VERTICAL;
			layout.gridy = y+1; layout.weighty = 0;
			this.add(button, layout);
			return y+2;
		}
	}
	
	/**
	 * A panel with two checkboxes, <em>Display Node Content</em> and <em>Display Edge Content</em>. Should I make it {@code static}?
	 * @author Minh Phuong
	 */
	private class CheckboxPanel extends JPanel{
		/**
		 * Serial Version UID? No idea what it is.
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Create a panel with two checkboxes: <em>Display Node Content</em> and <em>Display Edge Content</em>
		 */
		public CheckboxPanel() {
			super();
			addCheckboxes();
		}
		
		/**
		 * Add <em>Display Node Content</em> and <em>Display Edge Content</em> checkboxes to this panel.
		 */
		private void addCheckboxes() {
			JCheckBox toggleNode = new JCheckBox("Display Node Content");
			JCheckBox toggleEdge = new JCheckBox("Display Edge Content");
			toggleNode.setSelected(graph.toggleNodeData);
			toggleEdge.setSelected(graph.toggleEdgeData);
			
			toggleNode.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					graph.toggleNodeData = e.getStateChange() == 1 ? true : false;
					graphCanvas.repaint();
				}
			});
			toggleEdge.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					graph.toggleEdgeData = e.getStateChange() == 1 ? true : false;
					graphCanvas.repaint();
				}
			});
			this.add(toggleNode);
			this.add(toggleEdge);
		}
	}
	
	/**
	 * Used for removing edge and find distance. Flushed once new mode is selected.
	 */
	protected static GUIGraph.Node edgeNode = null;
	
	/**
	 * A thing to listen to mouse click.
	 * @author Minh Phuong
	 *
	 */
	private class MouseClickListener extends MouseAdapter {
		
		/**
		 * Used for node to drag + start node for edge connecting.
		 */
		GUIGraph.Node startNode;
		
		@Override
		public void mouseClicked(MouseEvent me) {
			Point2D currentPoint = me.getPoint();
		
			// REMOVE NODE BY CLICKING, ADD EDGE BY DRAGGING
			GUIGraph.Node nodeRightNow = getNodeAt(currentPoint);
			if (subject == Subject.EDGES && nodeRightNow != null) {
				// If no previous node is recorded, record the current node.
				if (edgeNode == null) {
					edgeNode = getNodeAt(currentPoint);
				// If there is a previous node recorded, add edges and wipe that node.
				} else {
					graph.removeEdge(edgeNode, getNodeAt(currentPoint));
					edgeNode = null;
				}
			}
			
			if (subject == Subject.DISTANCE && nodeRightNow != null) {
				// If no previous node is recorded, record the current node.
				if (edgeNode == null) {
					edgeNode = getNodeAt(currentPoint);
				// If there is a previous node recorded, compute path and wipe that node.
				} else {
					graph.highlightPathBetween(edgeNode, nodeRightNow);
					edgeNode = null;
				}
			}
			
			if (subject == Subject.NODES) {
				// ADD NODE CASE (LEFT-CLICK)
				if (me.getButton() == MouseEvent.BUTTON1) {
					graph.addNode(null, currentPoint);
				}
				
				//REMOVE NODE CASE (RIGHT-CLICK)
				else if (me.getButton() == MouseEvent.BUTTON3) {
					graph.removeNode(getNodeAt(currentPoint));
				}
			}
			
			// POP UP EDIT CONTENT
			if (subject == Subject.EDIT && nodeRightNow != null) {
				nodeRightNow.setData(JOptionPane.showInputDialog(graphCanvas, "Change node content to: ", nodeRightNow.getDataString()));
			}
			graphCanvas.repaint();
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
				graph.addEdge(startNode, destNode);
				graphCanvas.repaint();
			}
			startNode = null;
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			Point2D currentPoint = me.getPoint();
			// DRAG NODES
			if (subject == Subject.NODES && startNode != null) {
				graph.updateNodePosition(startNode, currentPoint);
				graphCanvas.repaint();
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