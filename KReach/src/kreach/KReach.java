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
import java.util.Set;
import java.util.function.Consumer;

/**
 *
 * @author Helmond
 */
public class KReach {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File f = new File("D:\\studie\\Master\\14-15\\Q4\\2ID35\\paper\\datasets\\cit-Patents.txt\\cit-Patents.txt");
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        StreamGraphParser parser = Graph.parseLineSeparatedEdgeFormat(true);
        br.lines().forEachOrdered(parser);
        System.out.println("Stream done");
        br.close();
        fr.close();
        Graph g = parser.getG();
        
        Set<Integer> vc2 = VertexCoverAlgorithms.computeBasic2AproxVertexCover(g);
        System.out.println(vc2.size());
    }
    
}
