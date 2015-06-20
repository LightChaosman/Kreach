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
public class DirectedEdge{

public final int u,v;

    public DirectedEdge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DirectedEdge other = (DirectedEdge) obj;
        
        return this.u == other.u && this.v == other.v;
    }

    @Override
    public String toString() {
        return u + "->" + v; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
