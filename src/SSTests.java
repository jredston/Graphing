import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * SSTests
 *
 * @author Shashank Singh
 * @version 1.0
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SSTests {

    // region [+] GRAPH VISUALIZATION
    /*
--------------------------------------------------------------------------------

                   GRAPH 1 (Random Graph)

                   ,---- E --------,
                  A     / \         `----,
                 /  ,- C   `- F --,       \
                /  /   | ,---'     `- G -- D
               H -'    |/                 /
               '------ B ----------------'

          EDGES  |  AE  AH  CE  CH  DE  BH  BC  BD  DG  EF  BF  FG
         WEIGHTS |  13   8   9  11  24  19   5  21   7  12  10  16

--------------------------------------------------------------------------------

                   GRAPH 2 (Complete Graph)

                        A ------- B
                       /           \
                      /             \     Not shown here but all vertices
                     E               C    are internally connected to
                      `-,         ,-'     every other Vertex
                         `--, ,--'
                             D

          EDGES  |  AB  AC  AD  AE  BC  BD  BE  CD  CE  DE
         WEIGHTS |  22  14  11  24  21  19   7  12   8  16

--------------------------------------------------------------------------------

                   GRAPH 3 (MST Graph)

                   U --- V --- X --- Y
                               |
                               W
                               |
                               Z

          EDGES  |   UV   VX   XY   WX   WZ
         WEIGHTS |  341  200  143  653  384

--------------------------------------------------------------------------------

                   GRAPH 4 (Disconnected Graph)

                   P --- Q
                   |  ,-'
                   | /      S --- T
                   R'

          EDGES  |    PQ    QR    PR    ST
         WEIGHTS |  8452  3595  6742  9543

--------------------------------------------------------------------------------
     */
    // endregion

    // region [$] VARIABLES
    private Map<Character, Vertex<Character>> vertices;

    private Graph<Character> graph;
    private Map<String, Edge<Character>> edges;
    private String expectedSearchResult;

    private List<Vertex<Character>> searchResult;
    private Map<Vertex<Character>, Integer> dijkstraResult;
    private Set<Edge<Character>> primResult;

    private static final int TIMEOUT = 500;
    private static final int LONG_TIMEOUT = 8000;
    // endregion

    // region [*] SETUP
    @Before public void setup() {
        createVertices();
    }
    // endregion

    // region [A] EXCEPTIONS
    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a00BFSNullStart() {
        setupGraph(1);
        GraphAlgorithms.breadthFirstSearch(null, graph);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a01BFSNullGraph() {
        setupGraph(1);
        GraphAlgorithms.breadthFirstSearch(v('A'), null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a02BFSInvalidStart() {
        setupGraph(1);
        GraphAlgorithms.breadthFirstSearch(new Vertex<>('Z'), graph);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a10DFSNullStart() {
        setupGraph(1);
        GraphAlgorithms.depthFirstSearch(null, graph);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a11DFSNullGraph() {
        setupGraph(1);
        GraphAlgorithms.depthFirstSearch(v('A'), null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a12DFSInvalidStart() {
        setupGraph(1);
        GraphAlgorithms.depthFirstSearch(new Vertex<>('Z'), graph);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a20DijkstraNullStart() {
        setupGraph(1);
        GraphAlgorithms.dijkstras(null, graph);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a21DijkstraNullGraph() {
        setupGraph(1);
        GraphAlgorithms.dijkstras(v('A'), null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a22DijkstraInvalidStart() {
        setupGraph(1);
        GraphAlgorithms.dijkstras(new Vertex<>('Z'), graph);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a30PrimNullStart() {
        setupGraph(1);
        GraphAlgorithms.prims(null, graph);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a31PrimNullGraph() {
        setupGraph(1);
        GraphAlgorithms.prims(v('A'), null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void a32PrimInvalidStart() {
        setupGraph(1);
        GraphAlgorithms.prims(new Vertex<>('Z'), graph);
    }
    // endregion

    // region [B] BREADTH FIRST SEARCH
    @Test(timeout = TIMEOUT)
    public void b00BfsGraph1() {
        setupGraph(1);
        searchResult = GraphAlgorithms.breadthFirstSearch(v('C'), graph);
        verifyBFSSearch(1);
    }

    @Test(timeout = TIMEOUT)
    public void b01BfsGraph2() {
        setupGraph(2);
        searchResult = GraphAlgorithms.breadthFirstSearch(v('E'), graph);
        verifyBFSSearch(2);
    }

    @Test(timeout = TIMEOUT)
    public void b02BfsGraph3() {
        setupGraph(3);
        searchResult = GraphAlgorithms.breadthFirstSearch(v('X'), graph);
        verifyBFSSearch(3);
    }

    @Test(timeout = TIMEOUT)
    public void b03BfsGraph4() {
        setupGraph(4);
        searchResult = GraphAlgorithms.breadthFirstSearch(v('Q'), graph);
        verifyBFSSearch(4);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void b04BfsRandomized() {
        StringBuilder sb = new StringBuilder("AB");
        Random r = new Random();
        int[] weights = new int[400];
        for (int i = 1; i < weights.length; ++i) {
            sb.append(',');
            sb.append((char) ('A' + r.nextInt(26)));
            sb.append((char) ('A' + r.nextInt(26)));
            weights[i] = 1;
        }
        createEdges(sb.toString(), weights);
        graph = new Graph<>(new LinkedHashSet<>(edges.values()));
        searchResult = GraphAlgorithms.breadthFirstSearch(
                v((char) ('A' + r.nextInt(26))), graph);
        assertEquals("All Vertices in search result were not unique",
                (new HashSet<>(searchResult)).size(), searchResult.size());
    }
    // endregion

    // region [C] DEPTH FIRST SEARCH
    @Test(timeout = TIMEOUT)
    public void c00DfsGraph1() {
        setupGraph(1);
        searchResult = GraphAlgorithms.depthFirstSearch(v('C'), graph);
        verifyDFSSearch(1);
    }

    @Test(timeout = TIMEOUT)
    public void c01DfsGraph2() {
        setupGraph(2);
        searchResult = GraphAlgorithms.depthFirstSearch(v('E'), graph);
        verifyDFSSearch(2);
    }

    @Test(timeout = TIMEOUT)
    public void c02DfsGraph3() {
        setupGraph(3);
        searchResult = GraphAlgorithms.depthFirstSearch(v('X'), graph);
        verifyDFSSearch(3);
    }

    @Test(timeout = TIMEOUT)
    public void c03DfsGraph4() {
        setupGraph(4);
        searchResult = GraphAlgorithms.depthFirstSearch(v('Q'), graph);
        verifyDFSSearch(4);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void c04DfsRandomized() {
        StringBuilder sb = new StringBuilder("AB");
        Random r = new Random();
        int[] weights = new int[400];
        for (int i = 1; i < weights.length; ++i) {
            sb.append(',');
            sb.append((char) ('A' + r.nextInt(26)));
            sb.append((char) ('A' + r.nextInt(26)));
            weights[i] = 1;
        }
        createEdges(sb.toString(), weights);
        graph = new Graph<>(new LinkedHashSet<>(edges.values()));
        searchResult = GraphAlgorithms.depthFirstSearch(
                v((char) ('A' + r.nextInt(26))), graph);
        assertEquals("All Vertices in search result were not unique",
                (new HashSet<>(searchResult)).size(), searchResult.size());
    }
    // endregion

    // region [D] DIJKSTRA'S ALGORITHM
    @Test(timeout = TIMEOUT)
    public void d00DijkstraGraph1() {
        setupGraph(1);
        dijkstraResult = GraphAlgorithms.dijkstras(v('C'), graph);
        verifyDijkstraResult(1);
    }

    @Test(timeout = TIMEOUT)
    public void d01DijkstraGraph2() {
        setupGraph(2);
        dijkstraResult = GraphAlgorithms.dijkstras(v('E'), graph);
        verifyDijkstraResult(2);
    }

    @Test(timeout = TIMEOUT)
    public void d02DijkstraGraph3() {
        setupGraph(3);
        dijkstraResult = GraphAlgorithms.dijkstras(v('X'), graph);
        verifyDijkstraResult(3);
    }

    @Test(timeout = TIMEOUT)
    public void d03DijkstraGraph4() {
        setupGraph(4);
        dijkstraResult = GraphAlgorithms.dijkstras(v('Q'), graph);
        verifyDijkstraResult(4);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void d04DijkstraRandomized() {
        StringBuilder sb = new StringBuilder("AB");
        Random r = new Random();
        int[] weights = new int[400];
        for (int i = 1; i < weights.length; ++i) {
            sb.append(',');
            sb.append((char) ('A' + r.nextInt(26)));
            sb.append((char) ('A' + r.nextInt(26)));
            weights[i] = r.nextInt(1000);
        }
        createEdges(sb.toString(), weights);
        graph = new Graph<>(new LinkedHashSet<>(edges.values()));
        dijkstraResult = GraphAlgorithms.dijkstras(
                v((char) ('A' + r.nextInt(26))), graph);
        assertEquals("All Vertices in search result were not unique",
                (new HashSet<>(dijkstraResult.keySet())).size(),
                dijkstraResult.size());
    }
    // endregion

    // region [E] PRIM'S ALGORITHM
    @Test(timeout = TIMEOUT)
    public void e00PrimGraph1() {
        setupGraph(1);
        primResult = GraphAlgorithms.prims(v('C'), graph);
        verifyPrimResult(1);
    }

    @Test(timeout = TIMEOUT)
    public void e01PrimGraph2() {
        setupGraph(2);
        primResult = GraphAlgorithms.prims(v('E'), graph);
        verifyPrimResult(2);
    }

    @Test(timeout = TIMEOUT)
    public void e02PrimGraph3() {
        setupGraph(3);
        primResult = GraphAlgorithms.prims(v('X'), graph);
        verifyPrimResult(3);
    }

    @Test(timeout = TIMEOUT)
    public void e03PrimGraph4() {
        setupGraph(4);
        primResult = GraphAlgorithms.prims(v('Q'), graph);
        verifyPrimResult(4);
    }

    @Test(timeout = LONG_TIMEOUT)
    public void e04PrimRandomized() {
        StringBuilder sb = new StringBuilder("AB");
        Random r = new Random();
        int[] weights = new int[400];
        for (int i = 1; i < weights.length; ++i) {
            sb.append(',');
            sb.append((char) ('A' + r.nextInt(26)));
            sb.append((char) ('A' + r.nextInt(26)));
            weights[i] = r.nextInt(1000);
        }
        createEdges(sb.toString(), weights);
        graph = new Graph<>(new LinkedHashSet<>(edges.values()));
        primResult = GraphAlgorithms.prims(
                v((char) ('A' + r.nextInt(26))), graph);
        // TODO - implement this
    }
    // endregion

    // region [!] SPECIAL
    @Test(timeout = TIMEOUT)
    public void i01() {
    }
    // endregion

    // region [?] HELPER CONSTRUCTS
    /**
     * Helper method for verifying correct output of a BFS
     * @param version choose from 4 graphs to test with
     */
    private void verifyBFSSearch(int version) {
        switch (version) {
        case 2:
            expectedSearchResult = "EDCBA";
            break;
        case 3:
            expectedSearchResult = "XYWVZU";
            break;
        case 4:
            expectedSearchResult = "QPR";
            break;
        default:
            expectedSearchResult = "CBEHDFAG";
        }
        assertEquals("Unexpected number of vertices in search result",
                expectedSearchResult.length(), searchResult.size());
        for (int i = 0; i < expectedSearchResult.length(); ++i) {
            assertEquals("Unexpected Vertex at index " + i,
                    v(expectedSearchResult.charAt(i)), searchResult.get(i));
        }
    }

    /**
     * Helper method for verifying correct output of a DFS
     * @param version choose from 4 graphs to test with
     */
    private void verifyDFSSearch(int version) {
        switch (version) {
        case 2:
            expectedSearchResult = "EDCBA";
            break;
        case 3:
            expectedSearchResult = "XYWZVU";
            break;
        case 4:
            expectedSearchResult = "QPR";
            break;
        default:
            expectedSearchResult = "CBDEFGAH";
        }
        assertEquals("Unexpected number of vertices in search result",
                expectedSearchResult.length(), searchResult.size());
        for (int i = 0; i < expectedSearchResult.length(); ++i) {
            assertEquals("Unexpected Vertex at index " + i,
                    v(expectedSearchResult.charAt(i)), searchResult.get(i));
        }
    }

    /**
     * Helper method for verifying correct output of Dijkstra's algorithm
     * @param version choose from 4 graphs to test with
     */
    private void verifyDijkstraResult(int version) {
        Map<Character, Integer> expectedDistances = new HashMap<>(
                graph.getAdjacencyList().size() + 1, 1);

        switch (version) {
        case 2:
            expectedDistances.put('A', 22);
            expectedDistances.put('B', 7);
            expectedDistances.put('C', 8);
            expectedDistances.put('D', 16);
            expectedDistances.put('E', 0);
            break;
        case 3:
            expectedDistances.put('U', 541);
            expectedDistances.put('V', 200);
            expectedDistances.put('W', 653);
            expectedDistances.put('X', 0);
            expectedDistances.put('Y', 143);
            expectedDistances.put('Z', 1037);
            break;
        case 4:
            expectedDistances.put('P', 8452);
            expectedDistances.put('Q', 0);
            expectedDistances.put('R', 3595);
            expectedDistances.put('S', Integer.MAX_VALUE);
            expectedDistances.put('T', Integer.MAX_VALUE);
            break;
        default:
            expectedDistances.put('A', 19);
            expectedDistances.put('B', 5);
            expectedDistances.put('C', 0);
            expectedDistances.put('D', 26);
            expectedDistances.put('E', 9);
            expectedDistances.put('F', 15);
            expectedDistances.put('G', 31);
            expectedDistances.put('H', 11);
            break;
        }
        assertEquals("Unexpected number of vertices in search result",
                expectedDistances.size(), dijkstraResult.size());
        for (int i = 0; i < 26; ++i) {
            assertEquals("Unexpected distance for Vertex " + ((char) ('A' + i)),
                    expectedDistances.get((char) ('A' + i)),
                    dijkstraResult.get(v((char) ('A' + i))));
        }
    }

    /**
     * Helper method for verifying correct output of Prim's algorithm
     * @param version choose from 4 graphs to test with
     */
    private void verifyPrimResult(int version) {
        Set<Edge<Character>> expectedPrimMST
                = new HashSet<>(graph.getAdjacencyList().size() + 1, 1);

        switch (version) {
        case 2:
            expectedPrimMST.add(e("CE"));
            expectedPrimMST.add(e("AD"));
            expectedPrimMST.add(e("BE"));
            expectedPrimMST.add(e("CD"));
            break;
        case 3:
            expectedPrimMST.add(e("UV"));
            expectedPrimMST.add(e("VX"));
            expectedPrimMST.add(e("XY"));
            expectedPrimMST.add(e("WX"));
            expectedPrimMST.add(e("WZ"));
            break;
        case 4:
            expectedPrimMST = null;
            break;
        default:
            expectedPrimMST.add(e("AH"));
            expectedPrimMST.add(e("CH"));
            expectedPrimMST.add(e("CE"));
            expectedPrimMST.add(e("BC"));
            expectedPrimMST.add(e("BF"));
            expectedPrimMST.add(e("FG"));
            expectedPrimMST.add(e("DG"));
            break;
        }
        assertEquals("Incorrect MST", expectedPrimMST, primResult);
    }

    /**
     * Helper method for generating 1 of many Graphs for testing
     * @param version choose from 4 graphs to test with
     */
    private void setupGraph(int version) {
        switch (version) {
        case 2:
            createEdges("AB,AC,AD,AE,BC,BD,BE,CD,CE,DE", new int[]
                {22, 14, 11, 24, 21, 19, 7, 12, 8, 16});
            break;
        case 3:
            createEdges("UV,VX,XY,WX,WZ", new int[]
                {341, 200, 143, 653, 384});
            break;
        case 4:
            createEdges("PQ,QR,PR,ST", new int[]
                {8452, 3595, 6742, 9543});
            break;
        default:
            createEdges("AE,AH,CE,CH,DE,BH,BC,BD,DG,EF,BF,FG", new int[]
                {13, 8, 9, 11, 24, 19, 5, 21, 7, 12, 10, 16});
            break;
        }
        graph = new Graph<>(new LinkedHashSet<>(edges.values()));
    }

    /**
     * Helper method for getting the Vertex corresponding to the Character
     * @param vertex Character of the Vertex
     * @return Vertex corresponding to the Character
     */
    private Vertex<Character> v(Character vertex) {
        return vertices.get(vertex);
    }

    /**
     * Helper method for getting the Edge corresponding to the Character pair
     * @param edge Character pair of the Edge
     * @return Edge corresponding to the Character pair
     */
    private Edge<Character> e(String edge) {
        return edges.get(edge);
    }

    /**
     * Helper method for creating a Mapping of Vertices
     */
    private void createVertices() {
        vertices = new HashMap<>(27, 1);
        for (int i = 0; i < 26; ++i) {
            vertices.put((char) ('A' + i), new Vertex<>((char) ('A' + i)));
        }
    }

    /**
     * Helper method for creating a Mapping of Edges
     * @param input string of comma-separated edges to create
     * @param weights weights of the corresponding edges
     */
    private void createEdges(String input, int[] weights) {
        String[] verticesPairs = input.split("\\s*,\\s*");
        edges = new HashMap<>(verticesPairs.length + 1, 1);
        for (int i = 0; i < verticesPairs.length; ++i) {
            edges.put(verticesPairs[i],
                    new Edge<>(v(verticesPairs[i].charAt(0)),
                            v(verticesPairs[i].charAt(1)), weights[i], false));
        }
    }
    // endregion
}
