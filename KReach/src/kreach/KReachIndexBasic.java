package kreach;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 *
 * @author Helmond
 */
public class KReachIndexBasic extends KReachIndex {

    private WeightedGraph index;

    public KReachIndexBasic(Graph g, int k) {
        super(g, k, 4);
        construct(g);
    }

    @Override
    protected Object getIndex() {
        return index.k1;
    }
    
    

    @Override
    protected boolean query2(int s, int t) {
        Graph gI = index.k1;
        HashMap<DirectedEdge, Integer> wI = index.k2;
        HashSet<Integer> VI = gI.vertices();
        HashSet<DirectedEdge> EI = gI.edges();
        //System.out.println("query; (" + s + ", " + t+ ")");
        // case 1: both s and t are in the vertex cover
        if (VI.contains(s) && VI.contains(t)) {
            Case(1);
            return EI.contains(new DirectedEdge(s, t));
        }
        // case 2: only s is in the vertex cover
        if (VI.contains(s) && !VI.contains(t)) {
            Case(2);
            for (int v : g.in(t)) {
                DirectedEdge e = new DirectedEdge(s, v);
                if (EI.contains(e) && wI.get(e) <= k - 1) {
                    return true;
                }
            }
            return false;
        }

        // case 3: only t is in the vertex cover
        if (!VI.contains(s) && VI.contains(t)) {
            Case(3);
            for (int v : g.out(s)) {
                DirectedEdge e = new DirectedEdge(v, t);
                if (EI.contains(e) && wI.get(e) <= k - 1) {
                    return true;
                }
            }
            return false;
        } // case 4: both s and t are not in the vertex cover
        else//(!VI.contains(s) && !VI.contains(t))
        {
            Case(4);
            for (int u : g.out(s)) {

                for (int v : g.in(t)) {
                    DirectedEdge e = new DirectedEdge(u, v);
                    if (EI.contains(e) && wI.get(e) <= k - 2) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    protected final void construct2(Graph g) {
        Set<Integer> S = VertexCoverAlgorithms.computeBasic2AproxVertexCover(g);
        Graph I = new Graph();
        System.out.println("Found vertex cover of size" + S.size());
        HashMap<DirectedEdge, Integer> wI = new HashMap<>();
        int i = 0;
        for (Integer u : S) {
            if (i++ % 100 == 0) {
                System.out.println(i);
            }
            HashMap<Integer, Integer> Sku = BFSu(g, u, k);
            for (Map.Entry<Integer, Integer> e : Sku.entrySet()) {
                int d = e.getValue();
                int v = e.getKey();
                I.addEdge(u, v);
                if (d <= k - 2) {
                    wI.put(new DirectedEdge(u, v), k - 2);
                } else if (d <= k - 1) {
                    wI.put(new DirectedEdge(u, v), k - 1);
                } else {
                    wI.put(new DirectedEdge(u, v), k);
                }
            }
        }
        this.index = new WeightedGraph(I, wI);
    }

    public static HashMap<Integer, Integer> BFSu(Graph g, int source, int k) {
        Queue<Integer> Q = new LinkedList<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        Q.add(source);
        dist.put(source, 0);
        while (!Q.isEmpty()) {

            int u = Q.poll();
            int d = dist.get(u);
            if (d == k) {
                break;
            }
            List<Integer> N = g.out(u);
            for (Integer v : N) {
                if (!(dist.containsKey(v))) {
                    dist.put(v, d + 1);
                    Q.add(v);
                }
            }
        }
        return dist;
    }

    
    
    

}
