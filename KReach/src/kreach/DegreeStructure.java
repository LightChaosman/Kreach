/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kreach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Helmond
 */
class DegreeStructure {

    private final ArrayList<HashSet<Integer>> verticesOfDegree;
    private final HashMap<Integer, Integer> degree;
    private final Graph g;
    int curmax;

    public DegreeStructure(Graph g) {
        this.g = g;
        curmax = 0;
        verticesOfDegree = new ArrayList<>();
        degree = new HashMap<>();
        for (int v : g.vertices()) {
            int d = g.out(v).size() + g.in(v).size();
            degree.put(v, d);
            for (int i = curmax; i <= d; i++) {
                verticesOfDegree.add(new HashSet<Integer>());
            }
            verticesOfDegree.get(d).add(v);
            curmax = Math.max(curmax, d);
        }
    }

    public void removeV(int v) {
        if (!degree.containsKey(v)) {
            return;
        }
        int d = degree.get(v);
        degree.remove(v);
        verticesOfDegree.get(d).remove(v);
        for (int u : g.out(v)) {
            DecreaseNeighbor(u);
        }
        for (int u : g.in(v)) {
            DecreaseNeighbor(u);
        }
        while (verticesOfDegree.get(curmax).isEmpty() && curmax > 0) {
            curmax--;
        }
    }

    private void DecreaseNeighbor(int u) {
        if (!degree.containsKey(u)) {
            return;
        }
        int deg = degree.get(u);
        if (deg == 0) {
            return;
        }
        degree.put(u, deg - 1);
        boolean wasPresent = verticesOfDegree.get(deg).remove(u);
        if (wasPresent) {
            verticesOfDegree.get(deg - 1).add(u);
        }
    }

    public int popMax() {
        if (curmax < 1) {
            return -1;
        }
        HashSet<Integer> maxset = verticesOfDegree.get(curmax);
        int v = maxset.iterator().next();
        maxset.remove(v);
        for (int u : g.out(v)) {
            DecreaseNeighbor(u);
        }
        for (int u : g.in(v)) {
            DecreaseNeighbor(u);
        }
        while (verticesOfDegree.get(curmax).isEmpty() && curmax > 0) {
            curmax--;
        }
        return v;
    }

}
