/**
 * 
 */
package edu.smith.cs.csc212.FPGraph;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import edu.smith.cs.csc212.FPGraphGUI.GUIGraph;

/**
 * Test for graph
 * @author Minh Phuong
 *
 */
public class GraphTest {
		
	/**
	 * Make an empty graph
	 * @return an empty graph
	 */
	private static <E, V> Graph<E, V> makeEmptyGraph() {
		Graph<E, V> empty = new Graph<E, V>();
		return empty;
	}
	
	/**
	 * Add all nodes and edges to data/Reset nodes and edges in data.<p>
	 * <em>Node index:</em> 0="a", 1="b", 2="c", 3="d", 4="e".<p>
	 * <em>Edge index:</em> 0="ab", 1="ca", 2="ad", 3="db", 4="ce", 5="ee".
	 */
	private static void makeFullGraph() {
		addNodesToData();
		addEdgesToData();
	}
	
	/**
	 * A full graph.<p>
	 * <em>Node index:</em> 0="a", 1="b", 2="c", 3="d", 4="e".<p>
	 * <em>Edge index:</em> 0="ab", 1="ca", 2="ad", 3="db", 4="ce", 5="ee".
	 */
	private static Graph<String, String> data = makeEmptyGraph();
	/**
	 * Node of data
	 */
	private static Graph<String, String>.Node a, b, c, d, e;
	
	/**
	 * Edge of data
	 */
	private static Graph<String, String>.Edge ab, ca, ad, db, ce, ee;
	
	/**
	 * Add edges to existed data and nodes.<p>
	 * Only use when nodes are added but no edges are connected.
	 */
	private static void addEdgesToData() {
		clearEdgesFromData();
		ab = data.addEdge("ab", a, b);
		ca = data.addEdge("ca", c, a);
		ad = data.addEdge("ad", a, d);
		db = data.addEdge("db", d, b);
		ce = data.addEdge("ce", c, e);
		ee = data.addEdge("ee", e, e);
	}
	
	/**
	 * Add nodes to data/Reset data's nodes/Recover data's nodes. <p>
	 * Will clear all edges. Use {@link #makeFullGraph()} to recover both nodes and edges.
	 */
	private static void addNodesToData() {
		clearNodesFromData();
		a = data.addNode("a");
		b = data.addNode("b");
		c = data.addNode("c");
		d = data.addNode("d");
		e = data.addNode("e");
	}
	
	/**
	 * Clear all nodes from data. Called when addNodesToData are called.
	 */
	private static void clearNodesFromData() {
		int nNodes = data.numNodes();
		for (int i=nNodes-1; i>=0; i--) {
			data.removeNode(data.getNode(i));
		}
	}
	
	/**
	 * Clear all edges from data. Called when addEdgesToData are called.
	 */
	private static void clearEdgesFromData() {
		int nEdges = data.numEdges();
		for (int i=nEdges-1; i>=0; i--) {
			data.removeEdge(data.getEdge(i));
		}
	}
	
	@Test
	public void testGetNode() {
		addNodesToData();
		assertEquals(5, data.numNodes());
		assertEquals(a, data.getNode(0));
		assertEquals(b, data.getNode(1));
		assertEquals(c, data.getNode(2));
		assertEquals(d, data.getNode(3));
		assertEquals(e, data.getNode(4));
		assertEquals(0, data.numEdges());
	}
	
	@Test
	public void testGetEdge() {
		makeFullGraph();
		assertEquals(data.new Edge(null, b, a), data.getEdge(0));
		assertEquals(data.new Edge(null, c, a), data.getEdge(1));
		assertEquals(data.new Edge("ae", d, a), data.getEdge(2));
		assertEquals(data.new Edge("ae", b, d), data.getEdge(3));
		assertEquals(data.new Edge("ae", c, e), data.getEdge(4));
		assertEquals(data.new Edge("ae", e, e), data.getEdge(5));
		assertEquals(6, data.numEdges());
	}
	
	@Test
	public void testGetEdgeRef() {
		makeFullGraph();
		assertEquals(data.new Edge(null, b, a), data.getEdgeRef(a,b));
		assertEquals(data.new Edge(null, c, a), data.getEdgeRef(a,c));
		assertEquals(data.new Edge("ae", d, a), data.getEdgeRef(a,d));
		assertEquals(data.new Edge("ae", b, d), data.getEdgeRef(b,d));
		assertEquals(data.new Edge("ae", c, e), data.getEdgeRef(c,e));
		assertEquals(data.new Edge("ae", e, e), data.getEdgeRef(e,e));
		assertNull(data.getEdgeRef(a,a));
		assertNull(data.getEdgeRef(d,e));
		assertNull(data.getEdgeRef(b,e));
		assertNull(data.getEdgeRef(b,b));
		assertEquals(6, data.numEdges());
	}
	
	@Test
	public void testAddEdge() {
		// Try a different data type
		Graph<String, Integer> test = makeEmptyGraph();
		assertEquals(0, test.numNodes());
		Graph<String, Integer>.Node a1 = test.addNode("a");
		Graph<String, Integer>.Node b1 = test.addNode("b");
		Graph<String, Integer>.Node c1 = test.addNode("c");
		
		assertEquals(0, test.numEdges());
		assertEquals(test.new Edge(null, b1, a1), test.addEdge(1, a1, b1));
		assertEquals(1, test.numEdges());
		
		Graph<String, Integer>.Edge bc = test.new Edge(null, b1, c1);
		assertEquals(bc, test.addEdge(0, b1, c1));
		assertEquals(bc, test.addEdge(134, c1, b1));
		assertEquals(0, (int) test.getEdge(1).getData());
		assertEquals(2, test.numEdges());
		
		assertEquals(test.new Edge(null, c1, c1), test.addEdge(0, c1, c1));
		assertEquals(3, test.numEdges());
	}
	
	@Test
	public void testAddEdgeNew() {
		makeFullGraph();
		Graph<String, String>.Node smith = data.new Node("smith");
		Graph<String, String>.Node mh = data.new Node("mount holyoke");
		Graph<String, String>.Node umass = data.new Node("umass");
		
		assertEquals(data.new Edge(null, smith, mh), data.addEdge("s-mh", smith, mh));
		assertEquals(7, data.numEdges());
		assertEquals(7, data.numNodes());
		assertEquals(data.new Edge(null, smith, smith), data.addEdge("s-s", smith, smith));
		assertEquals(8, data.numEdges());
		assertEquals(7, data.numNodes());
		assertEquals(data.new Edge(null, umass, umass), data.addEdge("u-u", umass, umass));
		assertEquals(8, data.numNodes());
		assertEquals(9, data.numEdges());
	}
	
	@Test
	public void testRemoveNode() {
		makeFullGraph();
		assertTrue(data.removeNode(a));
		assertFalse(data.removeNode(a));
		assertEquals(4, data.numNodes());
		assertEquals(3, data.numEdges());
		assertEquals(b, data.getNode(0));
		assertEquals(data.new Edge(null, d, b), data.getEdge(0));
		
		assertTrue(data.removeNode(b));
		assertFalse(data.removeNode(b));
		assertEquals(3, data.numNodes());
		assertEquals(2, data.numEdges());
		assertEquals(c, data.getNode(0));
		assertEquals(data.new Edge(null, c, e), data.getEdge(0));
		
		assertTrue(data.removeNode(d));
		assertFalse(data.removeNode(d));
		assertEquals(2, data.numNodes());
		assertEquals(2, data.numEdges());
		assertEquals(c, data.getNode(0));
		assertEquals(e, data.getNode(1));
		assertEquals(data.new Edge(null, e, c), data.getEdge(0));
		
		assertTrue(data.removeNode(c));
		assertFalse(data.removeNode(c));
		assertEquals(1, data.numNodes());
		assertEquals(1, data.numEdges());
		assertEquals(e, data.getNode(0));
		assertEquals(data.new Edge(null, e, e), data.getEdge(0));
		
		assertTrue(data.removeNode(e));
		assertFalse(data.removeNode(e));
		assertEquals(0, data.numNodes());
		assertEquals(0, data.numEdges());
	}
	
	@Test
	public void testRemoveEdgeByEdge() {
		makeFullGraph();
		assertTrue(c.isNeighbor(e));
		assertTrue(data.removeEdge(ce));
		assertFalse(c.isNeighbor(e));
		assertEquals(5, data.numEdges());
		assertEquals(ee, data.getEdge(4));
		assertFalse(c.getEdgeRef().contains(data.new Edge(null, c, e)));
		assertFalse(e.getEdgeRef().contains(data.new Edge(null, c, e)));
		assertFalse(data.removeEdge(data.new Edge(null, c, e)));
		
		assertTrue(e.isNeighbor(e));
		assertTrue(data.removeEdge(data.new Edge(null, e, e)));
		assertFalse(e.isNeighbor(e));
		assertFalse(e.getEdgeRef().contains(data.new Edge(null, e, e)));
		assertEquals(4, data.numEdges());
	}
	
	@Test
	public void testRemoveEdgeByNodes() {
		makeFullGraph();
		assertTrue(a.isNeighbor(b));
		assertTrue(b.isNeighbor(a));
		assertTrue(data.removeEdge(b, a));
		assertFalse(data.removeEdge(a, b));
		assertFalse(a.isNeighbor(b));
		assertEquals(5, data.numEdges());
		assertEquals(ca, data.getEdge(0));
		assertFalse(b.getEdgeRef().contains(data.new Edge(null, a, b)));
		assertFalse(a.getEdgeRef().contains(data.new Edge(null, a, b)));
		
		assertTrue(e.isNeighbor(e));
		assertTrue(data.removeEdge(e,e));
		assertFalse(data.removeEdge(e,e));
		assertFalse(e.isNeighbor(e));
		assertFalse(e.getEdgeRef().contains(data.new Edge(null, e, e)));
		assertEquals(4, data.numEdges());
	}
	
	@Test
	public void testOtherNodes() {
		HashSet<Graph<String, String>.Node> testList = new HashSet<Graph<String, String>.Node>();
		clearNodesFromData();
		assertEquals(0, data.numNodes());
		assertEquals(new HashSet<Graph<String, String>.Node>(), data.otherNodes(testList));
		assertEquals(0, data.otherNodes(testList).size());
		
		addNodesToData();
		testList.addAll(Arrays.asList(a, d, e));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(b, c)), data.otherNodes(testList));
		assertEquals(2, data.otherNodes(testList).size());
		
		testList.addAll(Arrays.asList(b, c));
		assertEquals(new HashSet<Graph<String, String>.Node>(), data.otherNodes(testList));
		assertEquals(0, data.otherNodes(testList).size());
		
		testList.clear();
		testList.add(data.new Node("a"));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(b, c, d, a, e)), data.otherNodes(testList));
		assertEquals(5, data.otherNodes(testList).size());
	}
	
	@Test
	public void testEndpoints() {
		HashSet<Graph<String, String>.Edge> testList = new HashSet<Graph<String, String>.Edge>(Arrays.asList(ab, ad));
		addNodesToData();
		assertEquals(0, data.numEdges());
		assertEquals(new HashSet<Graph<String, String>.Node>(), data.endpoints(testList));
		assertEquals(0, data.endpoints(testList).size());
		
		addEdgesToData();
		assertEquals(6, data.numEdges());
		testList.addAll(Arrays.asList(ab, ad));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, b, d)), data.endpoints(testList));
		assertEquals(3, data.endpoints(testList).size());
		
		testList.clear();
		testList.add(ee);
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(e)), data.endpoints(testList));
		assertEquals(1, data.endpoints(testList).size());
	}
	
	@Test
	public void testHasEdge() {
		makeFullGraph();
		assertTrue(data.hasEdge(ab));
		assertTrue(data.hasEdge(ca));
		assertTrue(data.hasEdge(ad));
		assertTrue(data.hasEdge(data.new Edge(null, e, e)));
		assertFalse(data.hasEdge(data.new Edge(null, a, a)));
		assertFalse(data.hasEdge(data.new Edge(null, d, e)));
		assertEquals(6, data.numEdges());
	}
	
	@Test
	public void testHasNode() {
		addNodesToData();
		assertTrue(data.hasNode(a));
		assertTrue(data.hasNode(b));
		assertEquals(5, data.numNodes());
		clearNodesFromData();
		assertFalse(data.hasNode(a));
		assertEquals(0, data.numNodes());
	}
	
	@Test
	public void testBFT() {
		makeFullGraph();
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, b, c, d, e)), data.BFT(a));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(e, c, a, b, d)), data.BFT(e));
		
		data.removeEdge(a, b);
		data.removeEdge(a, d);
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, c, e)), data.BFT(a));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(c, a, e)), data.BFT(c));
		Graph<String, String>.Node f = data.addNode("f");
		data.addEdge("fc", f, c);
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, c, e, f)), data.BFT(a));
		data.addEdge("fd", f, b);
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, b, c, d, e, f)), data.BFT(f));
		
		clearEdgesFromData();
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a)), data.BFT(a));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(e)), data.BFT(e));
	}
	
	@Test
	public void testDFT() {
		makeFullGraph();
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, b, c, d, e)), data.DFT(a));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(e, c, a, b, d)), data.DFT(e));
		
		data.removeEdge(a, b);
		data.removeEdge(a, d);
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, c, e)), data.DFT(a));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(c, a, e)), data.DFT(c));
		Graph<String, String>.Node f = data.addNode("f");
		data.addEdge("fc", f, c);
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, c, e, f)), data.DFT(a));
		data.addEdge("fd", f, b);
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a, b, c, d, e, f)), data.DFT(f));
		
		clearEdgesFromData();
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(a)), data.DFT(a));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(e)), data.DFT(e));
	}
	
	@Test
	public void testPrint() {
		makeFullGraph();
		//		a		b		c		d		e
		//a		null	ab		ca		ad		null		
		//b		ab		null	null	db		null		
		//c		ca		null	null	null	ce		
		//d		ad		db		null	null	null		
		//e		null	null	ce		null	ee
		data.print();

		System.out.println();
		data.removeEdge(db);
		data.addEdge("new", c, d);
		data.print();

		System.out.println();
		clearEdgesFromData();
		data.print();

		System.out.println();
		clearNodesFromData();
		data.print();
	}
	
	@Test
	public void testDistances() {
		GUIGraph distances = new GUIGraph();
		
		GUIGraph.Node smith = distances.addNode("smith");
		GUIGraph.Node moho = distances.addNode("moho");
		GUIGraph.Node umass = distances.addNode("umass");
		GUIGraph.Node hampshire = distances.addNode("hampshire");
		GUIGraph.Node amherst = distances.addNode("amherst");
		
		distances.addEdge(10.0, smith, moho);
		distances.addEdge(3.0, umass, smith);
		distances.addEdge(8.0, smith, hampshire);
		distances.addEdge(1.0, hampshire, moho);
		distances.addEdge(7.0, umass, amherst);
		distances.addEdge(5.0, amherst, amherst);
		
		Map<Graph<String, Double>.Node, Double> results = Map.of(smith, 0.0, moho, 9.0, umass, 3.0, amherst, 10.0, hampshire, 8.0);
		assertEquals(results, distances.distances(smith));
		
		results = Map.of(smith, 10.0, moho, 19.0, umass, 7.0, hampshire, 18.0, amherst, 0.0);
		assertEquals(results, distances.distances(amherst));
	}
	
	@Test
	public void testDistanceInfinity() {
		Graph<String, Double> distances = new GUIGraph();
		GUIGraph.Node smith = distances.addNode("smith");
		GUIGraph.Node moho = distances.addNode("moho");
		GUIGraph.Node umass = distances.addNode("umass");
		GUIGraph.Node hampshire = distances.addNode("hampshire");
		GUIGraph.Node amherst = distances.addNode("amherst");
		assertEquals(Map.of(smith, 0.0,
				moho, Double.POSITIVE_INFINITY,
				umass, Double.POSITIVE_INFINITY,
				amherst, Double.POSITIVE_INFINITY,
				hampshire, Double.POSITIVE_INFINITY), distances.distances(smith));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDistanceCrash() {
		makeFullGraph();
		data.distances(a);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getNodeCrashEmpty() {
		Graph<String, String> empty = makeEmptyGraph();
		assertEquals(0, empty.numNodes());
		empty.getNode(0);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getNodeCrashHigh() {
		addNodesToData();
		assertEquals(5, data.numNodes());
		data.getNode(data.numNodes());
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getNodeCrashLow() {
		addNodesToData();
		assertEquals(5, data.numNodes());
		data.getNode(-1);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getEdgeCrashEmpty() {
		Graph<String, String> empty = makeEmptyGraph();
		assertEquals(0, empty.numEdges());
		empty.getEdge(0);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getEdgeCrashHigh() {
		makeFullGraph();
		assertEquals(6, data.numEdges());
		data.getEdge(6);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getEdgeCrashLow() {
		makeFullGraph();
		assertEquals(6, data.numEdges());
		data.getEdge(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetEdgeRefCrash() {
		makeFullGraph();
		data.removeNode(a);
		data.removeNode(c);
		data.getEdgeRef(a, c);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBFTCrash() {
		makeFullGraph();
		data.removeNode(a);
		data.BFT(a);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDFTCrash() {
		makeFullGraph();
		data.removeNode(e);
		data.BFT(e);
	}
	
	@Test
	public void testEdgeHashCode() {
		makeFullGraph();
		assertEquals(ad.hashCode(), data.new Edge(null, d, a).hashCode());
	}

	@Test
	public void testNodeGetEdgeRef() {
		makeFullGraph();
		assertEquals(new HashSet<Graph<String, String>.Edge>(Arrays.asList(ee, ce)), e.getEdgeRef());
		assertEquals(new HashSet<Graph<String, String>.Edge>(Arrays.asList(ab, ca, ad)), a.getEdgeRef());
	}
	
	@Test
	public void testNodeGetNeighbors() {
		makeFullGraph();
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(e, c)), new HashSet<Graph<String, String>.Node>(e.getNeighbors()));
		assertEquals(new HashSet<Graph<String, String>.Node>(Arrays.asList(d, b, c)), new HashSet<Graph<String, String>.Node>(a.getNeighbors()));
		clearEdgesFromData();
		assertEquals(new LinkedList<Graph<String, String>.Node>(), a.getNeighbors());
	}
	
	@Test
	public void testNodeIsNeighbors() {
		makeFullGraph();
		assertTrue(a.isNeighbor(d));
		assertTrue(e.isNeighbor(e));
		assertFalse(e.isNeighbor(b));
		data.addEdge("be", b, e);
		assertTrue(e.isNeighbor(b));
		data.removeEdge(ad);
		assertFalse(a.isNeighbor(d));
	}
	
	@Test
	public void testNodeEdgeTo() {
		makeFullGraph();
		assertEquals(ab, a.edgeTo(b));
		assertEquals(data.new Edge("ee1", e, e), e.edgeTo(e));
		assertNull(a.edgeTo(a));
		assertNull(b.edgeTo(c));
	}
}