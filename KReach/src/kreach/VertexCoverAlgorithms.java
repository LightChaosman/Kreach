package kreach;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Helmond
 */
public class VertexCoverAlgorithms {

    public static Set<Integer> computeBasic2AproxVertexCover(Graph G) {
        //System.out.println("input for 2aprox vcover: " + G);
        //System.out.println("undirected variant: " + G);
        Set<DirectedEdge> edges = new HashSet<>();
        edges.addAll(G.edges());
        //System.out.println("ammount of edges: " + edges.size());
        Set<Integer> cover = new HashSet<>();
        int i = 0;
        //System.out.println(edges);
        while (!edges.isEmpty()) {
            DirectedEdge e = edges.iterator().next();
            int u = e.getU(), v = e.getV();
            cover.add(u);
            cover.add(v);
            HashSet<DirectedEdge> toRemove = new HashSet<>();
            
            for (int up : G.out(u)) {
                toRemove.add(new DirectedEdge(u, up));
            }
            for (int up : G.in(u)) {
                toRemove.add(new DirectedEdge(up, u));
            }
            for (int vp : G.out(v)) {
                toRemove.add(new DirectedEdge(v, vp));
            }
            for (int vp : G.in(v)) {
                toRemove.add(new DirectedEdge(vp, v));
            }
            edges.removeAll(toRemove);
            //System.out.println("Selected " + e + ", Removing " + toRemove );
            
        }
        return cover;
    }

}
