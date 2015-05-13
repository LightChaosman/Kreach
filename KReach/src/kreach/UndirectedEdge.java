package kreach;

/**
 *
 * @author Helmond
 */
public class UndirectedEdge extends AbstractEdge{

    public UndirectedEdge(int u, int v) {
        super(u<v?u:v, v>u?v:u);
    }

    @Override
    public int hashCode() {
        int p = u+v;
        int c = u*v;
        return p*p+c*2;//uu+2uv+vv+2uv=uu+vv+4uv
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UndirectedEdge other = (UndirectedEdge) obj;
        
        return this.u == other.u && this.v == other.v;
    }
    
    
    
}
