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
public abstract class AbstractEdge implements Edge{

    protected final int u,v;

    public AbstractEdge(int u, int v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public int getU() {
        return u;
    }

    @Override
    public int getV() {
        return v;
    }
    
    @Override
    public abstract int hashCode();
    
}
