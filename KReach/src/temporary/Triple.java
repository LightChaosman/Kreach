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
public class Triple<K1,K2,K3> extends Tuple<K1,K2> {
    
    public K3 k3;
    public Triple( K1 k1, K2 k2,K3 k3) {
        super(k1, k2);
        this.k3 = k3;
    }
    
    
}
