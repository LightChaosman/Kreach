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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
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
        //Graph g2 = loadPatents(100000);
        Graph g2 = loadARXiv();
        System.out.println(g2);
       //Set<Integer> vc2 = VertexCoverAlgorithms.computeBudgetedVertexCover(g2,VertexCoverAlgorithms.DEFAULT_BUDGET);
        Set<Integer> vc2 = VertexCoverAlgorithms.computeBasic2AproxVertexCover(g2);
        System.out.println(vc2.size());
        int k = 3;
        Tuple<Graph,HashMap<DirectedEdge,Integer>> kreach = KReachAlgorithms.computeOriginalKReachGraph(g2, k);
        for(int i = 0; i < 100; i ++)
        {
            int s=-1,t=-1;
            for(int v:g2.vertices())
            {
                if(Math.random()<0.0005)
                {
                    s=v;
                }else if(Math.random()<0.001)
                {
                    t=v;
                }
            }
            System.out.println("querying ("+s+","+t+")");
            boolean res = KReachAlgorithms.queryKReach1(g2, s, t, kreach, k);
            System.out.println(i+"=querying ("+s+","+t+"):"+res);
        }
        
    }
    
     private static Graph loadARXiv() throws FileNotFoundException, IOException {
        File f = new File("Datasets/Arxiv");
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

}
