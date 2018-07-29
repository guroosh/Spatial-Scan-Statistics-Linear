package com.graph.components;

import java.util.ArrayList;

public class Path {
    public ArrayList<Integer> path;

    public Path() {
        this.path = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (int i : this.path) {
            s.append(i).append(", ");
        }
        s.append("]");
        return s.toString();
    }
}
