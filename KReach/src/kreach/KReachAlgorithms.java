package kreach;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import temporary.Triple;
import temporary.Tuple;

/**
 *
 * @author Helmond
 */
public class KReachAlgorithms {

    public static Tuple<Graph, HashMap<DirectedEdge, Integer>> computeOriginalKReachGraph(Graph g, int k) {
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
                } else if (d < k - 1) {
                    wI.put(new DirectedEdge(u, v), k - 1);
                } else {
                    wI.put(new DirectedEdge(u, v), k);
                }
            }
        }
        return new Tuple<>(I, wI);
    }

    public static boolean queryKReach1(Graph g, int s, int t, Tuple<Graph, HashMap<DirectedEdge, Integer>> kreach, int k) {
        Graph gI = kreach.k1;
        HashMap<DirectedEdge, Integer> wI = kreach.k2;
        HashSet<Integer> VI = gI.vertices();
        HashSet<DirectedEdge> EI = gI.edges();
        System.out.println("query...");
        // case 1: both s and t are in the vertex cover
        if (VI.contains(s) && VI.contains(t)) {
            System.out.println("case 1");
            return EI.contains(new DirectedEdge(s, t));
        }
        // case 2: only s is in the vertex cover
        if (VI.contains(s) && !VI.contains(t)) {
            System.out.println("case 2");
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
            System.out.println("case 3");
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
            System.out.println("case 4");
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

    public static Triple<
            WeightedGraph,
            WeightedGraph,
            Graph> algorithm3(Graph g, int k, int b) {
        HashSet<Integer> S = new HashSet<>();
        Graph D1 = new Graph();
        HashMap<DirectedEdge,Integer> w1 = new HashMap<>();
        int i = 0;
        DegreeStructure ds = new DegreeStructure(g);
        int mv = ds.popMax();
        while (i < b && mv != -1) {
            khopbfs(g,mv,k,S,D1,w1);
            khopbfs2(g,mv,k,S,D1,w1);
            S.add(mv);
            mv = ds.popMax();
            i++;
        }
        Graph Gprime = new Graph();
        for(int v:g.vertices())
        {
            if(!S.contains(v))
                Gprime.addVertex(v);
        }
        for(DirectedEdge e:g.edges())
        {
            if(S.contains(e.u) || S.contains(e.v))continue;
            Gprime.addEdge(e.u, e.v);
        }
        Graph D2 = new Graph();
        HashSet<Integer> SPrime = new HashSet<>();
        HashMap<DirectedEdge,Integer> w2 = new HashMap<>();
        DegreeStructure dsPrime = new DegreeStructure(Gprime);
        int mvPrime = dsPrime.popMax();
        boolean enoughmem = Math.random()>0.01;
        while(enoughmem)//TODO
        {
            khopbfs(Gprime,mvPrime,k,SPrime,D2,w2);
            khopbfs2(Gprime,mvPrime,k,SPrime,D2,w2);
            SPrime.add(mvPrime);
            mvPrime = ds.popMax();
            enoughmem = Math.random()>0.01;
        }
        HashSet<Integer> VD1capSprime = new HashSet<>(SPrime);
        VD1capSprime.retainAll(D1.vertices());
        for(int u:VD1capSprime)
        {
            for(int v:VD1capSprime)
            {
                if(u==v)continue;
                DirectedEdge uv = new DirectedEdge(u,v);
                if(D2.edges().contains(uv))
                {
                    int weight2 = w2.get(uv);
                    int minw = weight2;
                    for(int x:S)
                    {
                        for(int y:S)
                        {
                            if(!(g.hasVertex(x)))
                            {
                                System.out.println("x not present in g");
                            }
                            if(!(g.hasVertex(y)))
                            {
                                System.out.println("y not present in g");
                            }
                            if(!(g.hasVertex(u)))
                            {
                                System.out.println("u not present in g");
                            }
                            if(!(g.hasVertex(v)))
                            {
                                System.out.println("v not present in g");
                            }
                            int xw1 = w1.get(new DirectedEdge(u,x));
                            int xw2 = (x==y?0:w1.get(new DirectedEdge(x,y)));
                            int xw3 = w1.get(new DirectedEdge(y,v));
                            int thisweight = xw1+xw2+xw3;
                            minw=Math.min(minw,thisweight);
                        }
                    }
                    w2.put(uv, minw);
                }else
                {
                    int d =999999999;
                    for(int x:S)
                    {
                        for(int y:S)
                        {
                            int xw1 = w1.get(new DirectedEdge(u,x));
                            int xw2 = (x==y?0:w1.get(new DirectedEdge(x,y)));
                            int xw3 = w1.get(new DirectedEdge(y,v));
                            int thisweight = xw1+xw2+xw3;
                            d=Math.min(d,thisweight);
                        }
                    }
                    if(d<=k)
                    {
                        Gprime.addEdge(u, v);
                        w2.put(uv, d);
                    }
                }
            }
        }
        Graph D3 = new Graph();
        for(int v:Gprime.vertices())
        {
            if(!SPrime.contains(v))
                D3.addVertex(v);
        }
        for(DirectedEdge e:Gprime.edges())
        {
            if(SPrime.contains(e.u) || SPrime.contains(e.v))continue;
            D3.addEdge(e.u, e.v);
        }
        return new Triple<>(new WeightedGraph(D1,w1),new WeightedGraph(D2,w2),D3);//TODO
    }

    public static void khopbfs(Graph g, int source, int k, HashSet<Integer> S, Graph d1, HashMap<DirectedEdge,Integer> w1) {
        Queue<Integer> Q = new LinkedList<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer,Integer> parents = new HashMap<>();
        Q.add(source);
        parents.put(source, source);
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
                    parents.put(v,u);
                }
            }
            if(S.contains(u))
            {
                d1.addEdge(source, u);
                w1.put(new DirectedEdge(source,u), d);
            }else
            {
                Integer parent = parents.get(u);
                boolean noneinS = true;
                while(parent!=source)
                {
                   if(S.contains(parent)){noneinS = false;break;}
                   parent = parents.get(parent);
                }
                if(noneinS)
                {
                    d1.addVertex(u);
                    d1.addEdge(source, u);
                    w1.put(new DirectedEdge(source,u),d);
                }
            }
        }

    }
    public static void khopbfs2(Graph g, int source, int k, HashSet<Integer> S, Graph d1, HashMap<DirectedEdge,Integer> w1) {
        Queue<Integer> Q = new LinkedList<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer,Integer> parents = new HashMap<>();
        Q.add(source);
        parents.put(source, source);
        dist.put(source, 0);
        while (!Q.isEmpty()) {

            int u = Q.poll();
            int d = dist.get(u);
            if (d == k) {
                break;
            }
            List<Integer> N = g.in(u);
            for (Integer v : N) {
                if (!(dist.containsKey(v))) {
                    dist.put(v, d + 1);
                    Q.add(v);
                    parents.put(v,u);
                }
            }
            if(S.contains(u))
            {
                d1.addEdge(u,source);
                w1.put(new DirectedEdge(u,source), d);
            }else
            {
                int parent = parents.get(u);
                boolean noneinS = true;
                while(parent!=source)
                {
                   if(S.contains(parent)){noneinS = false;break;}
                   parent = parents.get(parent);
                }
                if(noneinS)
                {
                    d1.addVertex(u);
                    d1.addEdge(u,source);
                    w1.put(new DirectedEdge(u,source),d);
                }
            }
        }

    }
}
