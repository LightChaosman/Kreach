package kreach;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    private final static File[] ALL = new File[]{ARXIV, FAA, DBLP, EPINOMS, GNUTELLA2, GPLUS, FIGEYS, STANFORD, TARO};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Graph g;
        //Choose dataset
        g = load(ARXIV);
        //Set parameters
        int b = 250;
        int k = 10;
        //Choose index type
        //KReachIndex index = new KReachIndexBasic(g, k);
        //KReachIndex index = new KReachIndexTwoLevel(g, k, b);
        KReachIndex index = new KReachIndexFinal(g, k, b);

        //Generate queries
        List<Integer> vertices = new ArrayList<>();
        vertices.addAll(g.vertices());
        Collections.sort(vertices);
        List<Integer> ss = new ArrayList<>(), ts = new ArrayList<>();
        int imax = 10000;
        long seed = 28469247374783468l;
        Random r = new Random(seed);
        for (int i = 0; i < imax; i++) {
            ss.add(vertices.get(r.nextInt(vertices.size())));
            ts.add(vertices.get(r.nextInt(vertices.size())));
        }
        //perform queries
        for (int i = 0; i < imax; i++) {
            int s = ss.get(i);
            int t = ts.get(i);
            index.query(s, t);
            if ((i + 1) % 1000 == 0) {
                index.printResults();
            }
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

    private static void K24681012CheckBigSets() throws IOException {
        File[] fs = new File[]{EPINOMS,GNUTELLA2,ARXIV};
        int[] ks = new int[]{12,10,8,6,4, 2};
        int b = 250;
        int reps = 3;
        int imax = 10000;
        for (int k : ks) {
            for (File f : fs) {
                System.out.println("loading " + f);
                Graph g = load(f);

                long avg = 0;
                for (int j = 0; j < reps; j++) {
                    System.gc();
                    System.out.println("Now starting " + f + " for k=" + k);
                    KReachIndex index = new KReachIndexTwoLevel(g, k, b);
                    long seed = 28469247374783468l;
                    Random r = new Random(seed);
                    ArrayList<Integer> vertices = new ArrayList<>();
                    vertices.addAll(g.vertices());
                    Collections.sort(vertices);
                    List<Integer> ss = new ArrayList<>(), ts = new ArrayList<>();
                    for (int i = 0; i < imax; i++) {
                        ss.add(vertices.get(r.nextInt(vertices.size())));
                        ts.add(vertices.get(r.nextInt(vertices.size())));
                    }
                    for (int i = 0; i < imax; i++) {
                        int s = ss.get(i);
                        int t = ts.get(i);
                        index.query(s, t);
                        if ((i + 1) % 1000 == 0) {
                            index.printResults();
                        }
                    }
                    avg += index.getQueryTime();
                    System.out.println("\n\n\n!!\n");
                    index.printResults();
                }
                avg = avg / reps;
                System.out.println("File; " + f + "; k=" + k + ", avarage query time for " + imax + " queries; " + avg);
                System.gc();

            }

        }
    }
}
