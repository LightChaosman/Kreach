package kreach;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import temporary.Quintuple;
import temporary.Triple;
import temporary.Tuple;

/**
 *
 * @author Helmond
 */
public class KReachAlgorithms {

    public static WeightedGraph computeOriginalKReachGraph(Graph g, int k) {
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
        return new WeightedGraph(I, wI);
    }

    public static boolean queryKReach1(Graph g, int s, int t, WeightedGraph kreach, int k) {
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

    public static Quintuple<WeightedGraph, WeightedGraph, Graph, HashSet<Integer>, HashSet<Integer>> algorithm3(Graph g, int k, int b) {
        HashSet<Integer> S = new HashSet<>();
        Graph D1 = new Graph();
        HashMap<DirectedEdge, Integer> w1 = new HashMap<>();
        int i = 0;
        DegreeStructure ds = new DegreeStructure(g);
        int mv = ds.popMax();
        while (i < b && mv != -1) {
            System.out.println("mv:"+mv);
            khopbfs(g, mv, k, S, D1, w1);
            khopbfs2(g, mv, k, S, D1, w1);
            S.add(mv);
            mv = ds.popMax();
            i++;
        }
        System.out.println("D1 done, building Gprime");
        Graph Gprime = new Graph();
        for (int v : g.vertices()) {
            if (!S.contains(v)) {
                Gprime.addVertex(v);
            }
        }
        for (DirectedEdge e : g.edges()) {
            if (S.contains(e.u) || S.contains(e.v)) {
                continue;
            }
            Gprime.addEdge(e.u, e.v);
        }
        Graph D2 = new Graph();
        HashSet<Integer> SPrime = new HashSet<>();
        HashMap<DirectedEdge, Integer> w2 = new HashMap<>();
        DegreeStructure dsPrime = new DegreeStructure(Gprime);
        int mvPrime = dsPrime.popMax();
        Runtime runtime = Runtime.getRuntime();
        boolean enoughmem = (runtime.maxMemory()-runtime.totalMemory())/(1024*1024)>1750;
        
        while (enoughmem && mvPrime !=-1)//TODO
        {
            khopbfs(Gprime, mvPrime, k, SPrime, D2, w2);
            khopbfs2(Gprime, mvPrime, k, SPrime, D2, w2);
            SPrime.add(mvPrime);
            mvPrime = ds.popMax();
            if(Math.random()>0.01)System.out.println((runtime.maxMemory()-runtime.totalMemory())/(1024*1024));
            enoughmem = (runtime.maxMemory()-runtime.totalMemory())/(1024*1024)>1750;
        }
        System.out.println("D2 step 1 done, fine tuning now");
        HashSet<Integer> VD1capSprime = new HashSet<>(SPrime);
        VD1capSprime.retainAll(D1.vertices());
        for (int u : VD1capSprime) {
            for (int v : VD1capSprime) {
                if (u == v) {
                    continue;
                }
                DirectedEdge uv = new DirectedEdge(u, v);
                if (D2.edges().contains(uv)) {
                    int weight2 = w2.get(uv);
                    int minw = weight2;
                    for (int x : S) {
                        for (int y : S) {

                            DirectedEdge ux = new DirectedEdge(u, x);
                            if (!w1.containsKey(ux)) {
                                continue;
                            }
                            int xw1 = w1.get(ux);
                            DirectedEdge xy = new DirectedEdge(x, y);
                            if (!w1.containsKey(xy) && x != y) {
                                continue;
                            }
                            int xw2 = (x == y ? 0 : w1.get(xy));
                            DirectedEdge yv = new DirectedEdge(y, v);
                            if (!w1.containsKey(yv)) {
                                continue;
                            }
                            int xw3 = w1.get(yv);
                            int thisweight = xw1 + xw2 + xw3;
                            minw = Math.min(minw, thisweight);
                        }
                    }
                    w2.put(uv, minw);
                } else {
                    int d = 999999999;
                    for (int x : S) {
                        for (int y : S) {
                            DirectedEdge ux = new DirectedEdge(u, x);
                            if (!w1.containsKey(ux)) {
                                continue;
                            }
                            int xw1 = w1.get(ux);
                            DirectedEdge xy = new DirectedEdge(x, y);
                            if (!w1.containsKey(xy) && x != y) {
                                continue;
                            }
                            int xw2 = (x == y ? 0 : w1.get(xy));
                            DirectedEdge yv = new DirectedEdge(y, v);
                            if (!w1.containsKey(yv)) {
                                continue;
                            }
                            int xw3 = w1.get(yv);
                            int thisweight = xw1 + xw2 + xw3;
                            d = Math.min(d, thisweight);
                        }
                    }
                    if (d <= k) {
                        Gprime.addEdge(u, v);
                        w2.put(uv, d);
                    }
                }
            }
        }
        System.out.println("Tuning done, staring constrution of D3");
        Graph D3 = new Graph();
        for (int v : Gprime.vertices()) {
            if (!SPrime.contains(v)) {
                D3.addVertex(v);
            }
        }
        for (DirectedEdge e : Gprime.edges()) {
            if (SPrime.contains(e.u) || SPrime.contains(e.v)) {
                continue;
            }
            D3.addEdge(e.u, e.v);
        }
        return new Quintuple<>(new WeightedGraph(D1, w1), new WeightedGraph(D2, w2), D3, S, SPrime);
    }

    public static boolean algorithm4(Quintuple<WeightedGraph, WeightedGraph, Graph, HashSet<Integer>, HashSet<Integer>> scaleIndex, int k, int s, int t) {
        HashSet<Integer> S = scaleIndex.k4;
        HashSet<Integer> Sprime = scaleIndex.k5;
        WeightedGraph D1 = scaleIndex.k1;
        WeightedGraph D2 = scaleIndex.k2;
        Graph D3 = scaleIndex.k3;
        boolean Ss = S.contains(s);
        boolean St = S.contains(t);
        if (Ss && St) {
            return D1.getWeight(s, t)!=null;

        } else if (Ss || St) {
            boolean t1 = D1.getWeight(s, t)!=null;
            if (t1) {
                return true;
            }
            for (int v : S) {
                Integer w1 = D1.getWeight(s, v);
                Integer w2 = D1.getWeight(v, t);
                if (w1 != null && w2 != null && w1 + w2 <= k) {
                    return true;
                }

            }
            return false;
        }
        boolean Sps = Sprime.contains(s);
        boolean Spt = Sprime.contains(t);
        
        if (Sps && Spt) {
            if (D1.getWeight(s, t)!=null) {
                return true;
            }
            for (int u : S) {
                for (int v : S) {
                    Integer w1 = D1.getWeight(s, u);
                    Integer w2 = D1.getWeight(u, v);
                    Integer w3 = D1.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
            return false;
            
        } else if (Sps || Spt) {
            if (D1.getWeight(s, t)!=null) {
                return true;
            }
            for (int v : Sprime) {

                Integer w1 = D2.getWeight(s, v);
                Integer w2 = D2.getWeight(v, t);
                if (w1 != null && w2 != null && w1 + w2 <= k) {
                    return true;
                }
            }
            for (int u : S) {
                for (int v : S) {
                    Integer w1 = D1.getWeight(s, u);
                    Integer w2 = D1.getWeight(u, v);
                    Integer w3 = D1.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (int u : S) {
                for (int v : S) {
                    Integer w1 = D1.getWeight(s, u);
                    Integer w2 = D1.getWeight(u, v);
                    Integer w3 = D1.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
            for (int u : Sprime) {
                for (int v : Sprime) {
                    Integer w1 = D2.getWeight(s, u);
                    Integer w2 = D2.getWeight(u, v);
                    Integer w3 = D2.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
                return paralelBFS(D3,k,s,t);
        }
    }
    
        public static boolean algorithm5(Quintuple<WeightedGraph, WeightedGraph, Graph, HashSet<Integer>, HashSet<Integer>> scaleIndex, int k, int s, int t) {
        HashSet<Integer> S = scaleIndex.k4;
        HashSet<Integer> Sprime = scaleIndex.k5;
        WeightedGraph D1 = scaleIndex.k1;
        WeightedGraph D2 = scaleIndex.k2;
        Graph D3 = scaleIndex.k3;
        boolean Ss = S.contains(s);
        boolean St = S.contains(t);
        if (Ss && St) {
            return D1.getWeight(s, t)!=null && D1.getWeight(s, t)<=k;

        } else if (Ss || St) {
            boolean t1 = D1.getWeight(s, t)!=null && D1.getWeight(s, t)<=k;
            if (t1) {
                return true;
            }
            for (int v : S) {
                Integer w1 = D1.getWeight(s, v);
                Integer w2 = D1.getWeight(v, t);
                if (w1 != null && w2 != null && w1 + w2 <= k) {
                    return true;
                }

            }
            return false;
        }
        boolean Sps = Sprime.contains(s);
        boolean Spt = Sprime.contains(t);
        
        if (Sps && Spt) {
            if (D1.getWeight(s, t)!=null&& D2.getWeight(s, t)<=k) {
                return true;
            }
            for (int u : S) {
                for (int v : S) {
                    Integer w1 = D1.getWeight(s, u);
                    Integer w2 = D1.getWeight(u, v);
                    Integer w3 = D1.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
            return false;
            
        } else if (Sps || Spt) {
            if (D1.getWeight(s, t)!=null&& D2.getWeight(s, t)<=k) {
                return true;
            }
            for (int v : Sprime) {

                Integer w1 = D2.getWeight(s, v);
                Integer w2 = D2.getWeight(v, t);
                if (w1 != null && w2 != null && w1 + w2 <= k) {
                    return true;
                }
            }
            for (int u : S) {
                for (int v : S) {
                    Integer w1 = D1.getWeight(s, u);
                    Integer w2 = D1.getWeight(u, v);
                    Integer w3 = D1.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (int u : S) {
                for (int v : S) {
                    Integer w1 = D1.getWeight(s, u);
                    Integer w2 = D1.getWeight(u, v);
                    Integer w3 = D1.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
            for (int u : Sprime) {
                for (int v : Sprime) {
                    Integer w1 = D2.getWeight(s, u);
                    Integer w2 = D2.getWeight(u, v);
                    Integer w3 = D2.getWeight(v, t);
                    if (w1 != null && w2 != null && w3 != null && w1 + w2 + w3 <= k) {
                        return true;
                    }
                }
            }
                return paralelBFS(D3,k,s,t);
        }
    }

    public static void khopbfs(Graph g, int source, int k, HashSet<Integer> S, Graph d1, HashMap<DirectedEdge, Integer> w1) {
        Queue<Integer> Q = new LinkedList<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer, Integer> parents = new HashMap<>();
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
                    parents.put(v, u);
                }
            }
            if (S.contains(u)) {
                d1.addEdge(source, u);
                w1.put(new DirectedEdge(source, u), d);
            } else {
                Integer parent = parents.get(u);
                boolean noneinS = true;
                while (parent != source) {
                    if (S.contains(parent)) {
                        noneinS = false;
                        break;
                    }
                    parent = parents.get(parent);
                }
                if (noneinS) {
                    d1.addVertex(u);
                    d1.addEdge(source, u);
                    w1.put(new DirectedEdge(source, u), d);
                }
            }
        }

    }

    public static void khopbfs2(Graph g, int source, int k, HashSet<Integer> S, Graph d1, HashMap<DirectedEdge, Integer> w1) {
        Queue<Integer> Q = new LinkedList<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        HashMap<Integer, Integer> parents = new HashMap<>();
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
                    parents.put(v, u);
                }
            }
            if (S.contains(u)) {
                d1.addEdge(u, source);
                w1.put(new DirectedEdge(u, source), d);
            } else {
                int parent = parents.get(u);
                boolean noneinS = true;
                while (parent != source) {
                    if (S.contains(parent)) {
                        noneinS = false;
                        break;
                    }
                    parent = parents.get(parent);
                }
                if (noneinS) {
                    d1.addVertex(u);
                    d1.addEdge(u, source);
                    w1.put(new DirectedEdge(u, source), d);
                }
            }
        }

    }

    private static boolean paralelBFS(Graph D3, int k, int s, int t) {
        Graph g = D3;
        Graph g2 = Graph.inverseLayer(g);
        Queue<Integer> Q1 = new LinkedList<>();
        Queue<Integer> Q2 = new LinkedList<>();
        Q1.add(s);
        Q2.add(t);
        HashMap<Integer,Integer> dist1 = new HashMap<>(), dist2 = new HashMap<>();
        dist1.put(s, 0);
        dist2.put(t, 0);
        int halfk = (int)Math.ceil(k/2d);
        while(!Q1.isEmpty())
        {
            int u = Q1.poll();
            int d = dist1.get(u);
            if(d>=halfk)break;
            for(int v:g.out(u))
            {
                if(!dist1.containsKey(v)){
                dist1.put(v, d+1);
                Q1.add(v);}
            }
        }
        while(!Q2.isEmpty())
        {
            int u = Q2.poll();
            int d = dist2.get(u);
            if(d>=halfk)break;
            for(int v:g2.out(u))
            {if(dist2.containsKey(v))continue;
                dist2.put(v, d+1);
                Q2.add(v);
                if(dist1.keySet().contains(v))
                {
                    int d1 = dist1.get(v);
                    if(d1+d+1<=k)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
