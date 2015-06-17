/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import temporary.Quintuple;
import temporary.Tuple;

/**
 *
 * @author Helmond
 */
public class KReach {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Graph g2 = null;
        
        /*for(File f:ALL){
            g2 = loadGeneral(f);
        
        System.out.println(g2);
        int maxdeg = 0;
        for(int v:g2.vertices())
        {
            maxdeg = Math.max(maxdeg, g2.in(v).size()+g2.out(v).size());
        }
        System.out.println(maxdeg);}//*/
        
        
        g2 = loadGeneral(ALL[2]);
        int k = 10;
        int b = 100;
        Quintuple<WeightedGraph, WeightedGraph, Graph, HashSet<Integer>, HashSet<Integer>> algorithm3 = KReachAlgorithms.algorithm3(g2, k, b);
        int trues = 0;
        List<Integer> vertices = new ArrayList<>();
        vertices.addAll(g2.vertices());
        long startime = System.currentTimeMillis();
        List<Integer> ss = new ArrayList<>(), ts = new ArrayList<>();
        int imax = 1000000;
        for(int i = 0; i < imax;i++)
        {
            ss.add(vertices.get((int)Math.floor(Math.random()*vertices.size())));
            ts.add(vertices.get((int)Math.floor(Math.random()*vertices.size())));
        }
        for(int i = 0; i < 1000000; i ++)
        {
            int s = ss.get(i);
            int t = ts.get(i);
            //System.out.println("querying ("+s+","+t+")");
            boolean res = KReachAlgorithms.algorithm4(algorithm3,k,s,t);
            trues +=res?1:0;
            //System.out.println(i+"=querying ("+s+","+t+"):"+res);
            if(i%1000==0)System.out.println("Fraction of succesfull queries: " + ((double)trues/i) + ", avarage time per query; " + ((System.currentTimeMillis()-startime)/(i+1)) + " ms");
        }
//*/
        
    }
    
    private static final File 
            ARXIV = new File("Datasets\\Arxiv"),
             FAA = new File("Datasets\\Air traffic control (FAA)\\out.maayan-faa"),
             DBLP = new File("Datasets\\DBLP\\out.dblp-cite"),
            EPINOMS = new File("Datasets\\Epinions\\out.soc-Epinions1"),
            GNUTELLA2 = new File("Datasets\\Gnutella\\out.p2p-Gnutella31"),
            GPLUS = new File("Datasets\\Google plus\\out.ego-gplus"),
            FIGEYS = new File("Datasets\\Human protein (Figeys)\\out.maayan-figeys"),
            STANFORD = new File("Datasets\\Stanford\\out.web-Stanford"),
            TARO = new File("Datasets\\Taro exchange\\out.moreno_taro_taro");
    private static File[] ALL = new File[]{ARXIV,FAA,DBLP,EPINOMS,GNUTELLA2,GPLUS,FIGEYS,STANFORD,TARO};
    
    private static Graph loadGeneral(File f) throws FileNotFoundException, IOException
    {
        System.out.println("Loading" + f);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        StreamGraphParser parser = Graph.parseLineSeparatedEdgeFormat(true);
        br.lines().forEachOrdered(parser);
        System.out.println("Stream done");
        br.close();
        fr.close();
        Graph g = parser.getG();
        return g;
    }

    private static Graph loadPatents(int vertices) throws FileNotFoundException, IOException {
        File f = new File("D:\\studie\\Master\\14-15\\Q4\\2ID35\\paper\\datasets\\cit-Patents.txt\\cit-Patents.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        StreamGraphParser parser = Graph.parseLineSeparatedEdgeFormat(true);
        br.lines().forEachOrdered(parser);
        System.out.println("Stream done");
        br.close();
        fr.close();
        Graph g = parser.getG();
        System.out.println(g);

        HashSet<Integer> induced = new HashSet<>();
        for (Integer v:g.vertices()) {
            if(induced.size()>vertices)
            {
                break;
            }
            if(g.out(v).size()>0)
            {
                induced.add(v);
                induced.addAll(g.out(v));
            }
        }
        System.out.println(g + " " + induced.size());
        
        return Graph.getInducedGraph(induced, g);
    }
    
    private static int getDiameter(Graph g)
    {
        int m = 0;
        int i = 0;
        for(int v:g.vertices())
        {
            i++;
            m = Math.max(m, getMaxDistFrom(g, v));
            if(i%1==0)System.out.println("Processed " + i + " vertices, current diameter; " + m) ;
        }
        return m;
    }
    
    private static int getMaxDistFrom(Graph g, int v)
    {
        Queue<Integer> q = new LinkedList<>();
        q.add(v);
        HashMap<Integer,Integer> d = new HashMap<>();
        d.put(v, 0);
        int di = 0;
        while(!q.isEmpty())
        {
            int u = q.poll();
            di = d.get(u);
            for(int s:g.out(u))
            {
                if(d.containsKey(s))continue;
                d.put(s, di+1);
                q.add(s);
            }
        }
        return di;
    }

}
