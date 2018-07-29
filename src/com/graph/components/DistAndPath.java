package com.graph.components;

public class DistAndPath {
    public double[] dist;
    public Path[] path;

    public DistAndPath(double[] dist, Path[] path) {
        this.dist = dist;
        this.path = path;
    }

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        for (Path p : path)
            a.append(p.toString());
        return a.toString();
    }
}
