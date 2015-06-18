package kreach;

import java.util.Set;

/**
 *
 * @author Helmond
 */
public class KReachIndexFinal extends KReachIndex{
    
    private KReachIndex index;
    private final int b;

    public KReachIndexFinal(Graph g, int k, int b) {
        super(g, k, 5);
        this.b =b;
        construct(g);
    }

    @Override
    protected void construct2(Graph g) {
       Set<Integer> S = VertexCoverAlgorithms.computeBasic2AproxVertexCover(g);
       if(S.size()<10000)
       {
           index = new KReachIndexBasic(g, k);
       }else
       {
           index = new KReachIndexTwoLevel(g, k, b);
       }
    }

    @Override
    protected boolean query2(int s, int t) {
        return index.query(s, t);
    }

    @Override
    protected Object getIndex() {
        return index.getIndex();
    }
    
    @Override
    protected String getName() {
        return "Final k-reach (k="+k+")";
    }

    @Override
    protected String extraInfo() {
        return index.extraInfo();
    }

    @Override
    public void printResults() {
        index.printResults(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
}
