/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

/**
 *
 * @author Helmond
 */
public class UndirectedEdge extends AbstractEdge{

    public UndirectedEdge(int u, int v) {
        super(u, v);
    }

    @Override
    public int hashCode() {
        int p = u+v;
        int c = u*v;
        return p*p+c*2;//uu+2uv+vv+2uv=uu+vv+4uv
    }
    
    
    
}
