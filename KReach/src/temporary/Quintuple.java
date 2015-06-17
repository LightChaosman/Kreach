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
public class Quintuple<K1,K2,K3,K4,K5> extends Quadruple<K1,K2,K3,K4> {
    
    public K5 k5;
    public Quintuple( K1 k1, K2 k2,K3 k3,K4 k4, K5 k5) {
        super(k1, k2,k3,k4);
        this.k5 = k5;
    }
    
    @Override
    public String toString() {
        return "<" +k1 + ", " + k2 + ", " + k3 +", " + k4 + ", " + k5 + ">"; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
