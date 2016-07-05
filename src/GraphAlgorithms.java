import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Your implementations of various graph algorithms.
 *
 * @author jredston3
 * @version 1.0
 */
public class GraphAlgorithms {

    /**
     * Perform breadth first search on the given graph, starting at the start
     * Vertex. You will return a List of the vertices in the order that you
     * visited them. Make sure to include the starting vertex at the beginning
     * of the list.
     *
     * When exploring a Vertex, make sure you explore in the order that the
     * adjacency list returns the neighbors to you. Failure to do so may cause
     * you to lose points.
     *
     * You may import/use {@code java.util.Queue}, {@code java.util.Set},
     * {@code java.util.Map}, {@code java.util.List}, and any classes that
     * implement the aforementioned interfaces.
     *
     * @throws IllegalArgumentException
     *             if any input is null, or if {@code start} doesn't exist in
     *             the graph
     * @param start
     *            the Vertex you are starting at
     * @param graph
     *            the Graph we are searching
     * @param <T>
     *            the data type representing the vertices in the graph.
     * @return a List of vertices in the order that you visited them
     */
    public static <T> List<Vertex<T>> breadthFirstSearch(Vertex<T> start,
            Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("Either start or graph is null");
        }
        if (graph.getAdjacencyList().get(start) == null) {
            throw new IllegalArgumentException("Start does not exist in graph");
        }
        List<Vertex<T>> list = new ArrayList<Vertex<T>>();
        Queue<Vertex<T>> queue = new LinkedList<Vertex<T>>();

        queue.add(start);
        while (queue.size() != 0) {
            Vertex<T> temp = queue.poll();
            if (!list.contains(temp)) {
                list.add(temp);
            }
            for (VertexDistancePair<T> i : graph.getAdjacencyList().get(temp)) {
                if (!list.contains(i.getVertex())) {
                    queue.add(i.getVertex());
                }
            }
        }
        return list;

    }

    /**
     * Perform depth first search on the given graph, starting at the start
     * Vertex. You will return a List of the vertices in the order that you
     * visited them. Make sure to include the starting vertex at the beginning
     * of the list.
     *
     * When exploring a Vertex, make sure you explore in the order that the
     * adjacency list returns the neighbors to you. Failure to do so may cause
     * you to lose points.
     *
     * You MUST implement this method recursively. Do not use any data structure
     * as a stack to avoid recursion. Implementing it any other way WILL cause
     * you to lose points!
     *
     * You may import/use {@code java.util.Set}, {@code java.util.Map},
     * {@code java.util.List}, and any classes that implement the aforementioned
     * interfaces.
     *
     * @throws IllegalArgumentException
     *             if any input is null, or if {@code start} doesn't exist in
     *             the graph
     * @param start
     *            the Vertex you are starting at
     * @param graph
     *            the Graph we are searching
     * @param <T>
     *            the data type representing the vertices in the graph.
     * @return a List of vertices in the order that you visited them
     */
    public static <T> List<Vertex<T>> depthFirstSearch(Vertex<T> start,
            Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("Either start or graph is null");
        }
        if (graph.getAdjacencyList().get(start) == null) {
            throw new IllegalArgumentException("Start does not exist in graph");
        }
        Stack<Vertex<T>> stack = new Stack<Vertex<T>>();
        List<Vertex<T>> list = depthFirstSearch(graph, stack, start);
        return list;

    }

    /**
     * recursive part of dfs
     * 
     * @param graph
     *            graph given
     * @param stack
     *            stack of vertex to search
     * @param vertex
     *            current vertex
     * @param <T>
     *            the data type representing the vertices in the graph.
     * @return list of vertex searched thus far
     */
    private static <T> List<Vertex<T>> depthFirstSearch(Graph<T> graph,
            Stack<Vertex<T>> stack, Vertex<T> vertex) {
        List<Vertex<T>> list = new ArrayList<Vertex<T>>();
        stack.add(vertex);
        list.add(vertex);
        for (VertexDistancePair<T> pair : graph
                .getAdjacencyList().get(vertex)) {
            if (!stack.contains(pair.getVertex())) {
                list.addAll(depthFirstSearch(graph, stack, pair.getVertex()));
            }
        }
        return list;

    }

    /**
     * Find the shortest distance between the start vertex and all other
     * vertices given a weighted graph where the edges only have positive
     * weights.
     *
     * Return a map of the shortest distances such that the key of each entry is
     * a node in the graph and the value for the key is the shortest distance to
     * that node from start, or Integer.MAX_VALUE (representing infinity) if no
     * path exists. You may assume that going from a vertex to itself has a
     * distance of 0.
     *
     * There are guaranteed to be no negative edge weights in the graph.
     *
     * You may import/use {@code java.util.PriorityQueue}, {@code java.util.Map}
     * , and any class that implements the aforementioned interface.
     *
     * @throws IllegalArgumentException
     *             if any input is null, or if {@code start} doesn't exist in
     *             the graph
     * @param start
     *            the Vertex you are starting at
     * @param graph
     *            the Graph we are searching
     * @param <T>
     *            the data type representing the vertices in the graph.
     * @return a map of the shortest distances from start to every other node in
     *         the graph.
     */
    public static <T> Map<Vertex<T>, Integer> dijkstras(Vertex<T> start,
            Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("Either start or graph is null");
        }
        if (graph.getAdjacencyList().get(start) == null) {
            throw new IllegalArgumentException("Start does not exist in graph");
        }
        // priority queue
        PriorityQueue<VertexDistancePair<T>> queue =
                new PriorityQueue<VertexDistancePair<T>>();
        Map<Vertex<T>, Integer> distances = new HashMap<Vertex<T>, Integer>();
        List<Vertex<T>> visted = new ArrayList<Vertex<T>>();
        queue.add(new VertexDistancePair<T>(start, 0));
        distances.put(start, 0);
        while (queue.size() != 0) {
            VertexDistancePair<T> temp = queue.poll();
            visted.add(temp.getVertex());

            for (VertexDistancePair<T> i : graph.getAdjacencyList().get(
                    temp.getVertex())) {
                if (!visted.contains(i.getVertex())) {
                    if (distances.get(i.getVertex()) == null
                            || distances.get(i.getVertex()) > i.getDistance()
                                    + distances.get(temp.getVertex())) {
                        distances.put(i.getVertex(), i.getDistance()
                                + distances.get(temp.getVertex()));

                    }
                    queue.add(new VertexDistancePair<T>(i.getVertex(),
                            distances.get(i.getVertex())));
                }

            }

        }
        return distances;

    }

    /**
     * Run Prim's algorithm on the given graph and return the minimum spanning
     * tree in the form of a set of Edges. If the graph is disconnected, and
     * therefore there is no valid MST, return null.
     *
     * When exploring a Vertex, make sure you explore in the order that the
     * adjacency list returns the neighbors to you. Failure to do so may cause
     * you to lose points.
     *
     * You may assume that for a given starting vertex, there will only be one
     * valid MST that can be formed. In addition, only an undirected graph will
     * be passed in.
     *
     * You may import/use {@code java.util.PriorityQueue}, {@code java.util.Set}
     * , and any class that implements the aforementioned interface.
     *
     * @throws IllegalArgumentException
     *             if any input is null, or if {@code start} doesn't exist in
     *             the graph
     * @param start
     *            the Vertex you are starting at
     * @param graph
     *            the Graph we are searching
     * @param <T>
     *            the data type representing the vertices in the graph.
     * @return the MST of the graph; null if no valid MST exists.
     */
    public static <T> Set<Edge<T>> prims(Vertex<T> start, Graph<T> graph) {
        if (start == null || graph == null) {
            throw new IllegalArgumentException("Either start or graph is null");
        }
        if (graph.getAdjacencyList().get(start) == null) {
            throw new IllegalArgumentException("Start does not exist in graph");
        }

        Set<Edge<T>> set = new HashSet<Edge<T>>();
        List<Vertex<T>> visited = new ArrayList<Vertex<T>>();

        PriorityQueue<Edge<T>> queue2 = new PriorityQueue<Edge<T>>();
        queue2.add(new Edge<T>(null, start, 0, false));
        int graphSize = 0;
        Set<Vertex<T>> sizeArray = new HashSet<Vertex<T>>();
        for (Edge<T> edge : graph.getEdgeList()) {
            if (!sizeArray.contains(edge.getU())) {
                sizeArray.add(edge.getU());
                graphSize++;
            }
            if (!sizeArray.contains(edge.getV())) {
                sizeArray.add(edge.getV());
                graphSize++;
            }
        }
        while (queue2.size() != 0 && visited.size() < graphSize) {
            Edge<T> tempEdge = queue2.poll();
            VertexDistancePair<T> tempPair = new VertexDistancePair<T>(
                    tempEdge.getV(), tempEdge.getWeight());
            if (tempEdge.getU() != null
                    && !visited.contains(tempPair.getVertex())) {
                set.add(tempEdge);
            }
            visited.add(tempPair.getVertex());

            for (VertexDistancePair<T> pair : graph.getAdjacencyList().get(
                    tempPair.getVertex())) {
                if (!visited.contains(pair.getVertex())) {
                    queue2.add(new Edge<T>(tempPair.getVertex(), pair
                            .getVertex(), pair.getDistance(), false));
                }
            }

        }

        if (visited.size() < graphSize) {
            return null;
        }

        return set;

    }



}
