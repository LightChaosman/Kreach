/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

import java.util.function.Consumer;

/**
 *
 * @author Helmond
 */
public abstract class StreamGraphParser implements Consumer<String>{
    
    protected final Graph g;

    public StreamGraphParser(boolean directed) {
        g = new Graph();
    }

    public Graph getG() {
        return g;
    }
    
    

    @Override
    public abstract void accept(String t);
    
}
