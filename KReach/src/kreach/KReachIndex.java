package kreach;

import java.util.Arrays;

/**
 *
 * @author Helmond
 */
public abstract class KReachIndex {
    
    final int[] caseCounter;
    private long constructionTime;
    private long queryTime = 0;
    private long queries = 0;
    private long trues = 0;
    protected final Graph g;
    protected final int k;
    
    protected KReachIndex(Graph g,int k,int cases)
    {
        caseCounter = new int[cases];
        this.g =g;
        this.k = k;
    }
    
    protected void Case(int Case)
    {
        caseCounter[Case-1]++;
    }
    
    protected final void construct(Graph g)
    {
        this.constructionTime=System.currentTimeMillis();
        construct2(g);
        this.constructionTime=System.currentTimeMillis()-this.constructionTime;
    }
    
    protected abstract void construct2(Graph g);
    
    public final boolean query(int s,int t)
    {
        queries++;
        queryTime-=System.nanoTime();
        boolean b = query2(s,t);
        queryTime+=System.nanoTime();
        trues += (b?1:0);
        return b;
    }
    
    protected abstract boolean query2(int s, int t);
    
    protected abstract Object getIndex();
    
    protected abstract String getName();
    
    public void printResults() {
        
        System.out.println(getName() +" index on graph G; " +  g + "\n"
                + "Index size became; " + getIndex() + "\n"
                + "Index construction time; " + constructionTime + "\n"
                + "Query amount; " + queries + ", time spent on queries; " + queryTime  + "ns ("+(queryTime/1000000)+"ms), ammount of trues returned; "  + trues + "\n"
                + "Case partitioning; " + Arrays.toString(caseCounter) );
    }
    
}
