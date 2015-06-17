/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temporary;

/**
 *
 * @author Helmond
 */
public class Tuple<K1,K2> {
    public K1 k1;
    public K2 k2;

    public Tuple(K1 k1, K2 k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    @Override
    public String toString() {
        return "<" + k1 + ", " + k2+">"; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
