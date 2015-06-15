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
public class Quadruple<K1,K2,K3,K4> extends Triple<K1,K2,K3> {
    
    public K4 k4;
    public Quadruple( K1 k1, K2 k2,K3 k3, K4 k4) {
        super(k1, k2,k3);
        this.k4 = k4;
    }
    
    
}
