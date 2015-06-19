package kreach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Helmond
 */
public class KReach {

    private static final File ARXIV = new File("Datasets\\Arxiv"),
            FAA = new File("Datasets\\Air traffic control (FAA)\\out.maayan-faa"),
            DBLP = new File("Datasets\\DBLP\\out.dblp-cite"),
            EPINOMS = new File("Datasets\\Epinions\\out.soc-Epinions1"),
            GNUTELLA2 = new File("Datasets\\Gnutella\\out.p2p-Gnutella31"),
            GPLUS = new File("Datasets\\Google plus\\out.ego-gplus"),
            FIGEYS = new File("Datasets\\Human protein (Figeys)\\out.maayan-figeys"),
            STANFORD = new File("Datasets\\Stanford\\out.web-Stanford"),
            TARO = new File("Datasets\\Taro exchange\\out.moreno_taro_taro");
    private static File[] ALL = new File[]{ARXIV, FAA, DBLP, EPINOMS, GNUTELLA2, GPLUS, FIGEYS, STANFORD, TARO};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        //for(int j = 0; j < 5; j++){
        Graph g2;
        g2 = load(ARXIV);
        int b = 250;
        int k = 10;
        //KReachIndex index = new KReachIndexBasic(g2, k);
        KReachIndex index = new KReachIndexTwoLevel(g2, k, b);
        //KReachIndex index = new KReachIndexFinal(g2,k,b);
        List<Integer> vertices = new ArrayList<>();
        //index.printResults();
        boolean a = false;
        if(a)return;
        vertices.addAll(g2.vertices());
        Collections.sort(vertices);
        List<Integer> ss = new ArrayList<>(), ts = new ArrayList<>();
        int imax = 10000;
        long seed = 28469247374783468l;
        Random r = new Random(seed);
        for (int i = 0; i < imax; i++) {
            ss.add(vertices.get(r.nextInt(vertices.size())));
            ts.add(vertices.get(r.nextInt(vertices.size())));
        }
        for (int i = 0; i < imax; i++) {
            int s = ss.get(i);
            int t = ts.get(i);
            index.query(s, t);
            if((i+1)%1000==0)index.printResults();
        }
        System.out.println("\n\n");
        index.printResults();
    }

    private static Graph load(File f) throws FileNotFoundException, IOException {
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
}
