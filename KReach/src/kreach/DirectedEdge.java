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
public class DirectedEdge extends AbstractEdge{

    public DirectedEdge(int u, int v) {
        super(u, v);
    }

    @Override
    public int hashCode() {
        int p = 213*u+v;
        int c = u*v;
        return p*p+c*2;
    }
    
    public DirectedEdge getFlippedEdge()
    {
        return new DirectedEdge(v,u);
    }
    
}
