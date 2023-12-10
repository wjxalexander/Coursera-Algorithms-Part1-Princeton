/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A shortest ancestral path is an ancestral path of minimum total length. We refer to the common
 * ancestor in a shortest ancestral path as a shortest common ancestor.
 */
public class SAP {
    private final Digraph digraph;
    private final HashMap<HashSet<Integer>, int[]> cache = new HashMap<>();

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Input is illegal.");
        }
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return readCacheAndUpdateCacheIfNotExist(v, w)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return readCacheAndUpdateCacheIfNotExist(v, w)[1];
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);
        return shortestAncestralPath(pathV, pathW)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);
        return shortestAncestralPath(pathV, pathW)[1];
    }

    private int[] readCacheAndUpdateCacheIfNotExist(int v, int w) {
        HashSet<Integer> key = new HashSet<>();
        key.add(v);
        key.add(w);
        if (!cache.containsKey(key)) {
            sap(v, w);
        }
        return cache.get(key);
    }

    private void sap(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        HashSet<Integer> key = new HashSet<>();
        key.add(v);
        key.add(w);
        BreadthFirstDirectedPaths pathV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths pathW = new BreadthFirstDirectedPaths(digraph, w);
        int[] ditanceAndAncestor = shortestAncestralPath(pathV, pathW);
        cache.put(key, ditanceAndAncestor);
    }

    private int[] shortestAncestralPath(BreadthFirstDirectedPaths pathV,
                                        BreadthFirstDirectedPaths pathW) {
        int ditance = Integer.MAX_VALUE;
        int acsestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (pathV.hasPathTo(i) && pathW.hasPathTo(i) && (pathV.distTo(i) + pathW.distTo(i)
                    < ditance)) {
                ditance = pathV.distTo(i) + pathW.distTo(i);
                acsestor = i;
            }
        }
        return ditance == Integer.MAX_VALUE ? new int[] { -1, -1 } :
               new int[] { ditance, acsestor };
    }

    private void validateVertex(int v) {
        int n = digraph.V();
        if (v < 0 || v >= n)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (n - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int n = digraph.V();
        for (int v : vertices) {
            if (v < 0 || v >= n) {
                throw new IllegalArgumentException(
                        "vertex " + v + " is not between 0 and " + (n - 1));
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
