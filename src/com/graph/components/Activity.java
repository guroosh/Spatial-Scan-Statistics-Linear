package com.graph.components;

public class Activity {
    public int new_node_starting_index;
    public double x;
    public double y;
    public Edge e;

    public Activity(int new_nodes_starting_index, double x_random, double y_random, Edge e) {
        this.new_node_starting_index = new_nodes_starting_index;
        this.x = x_random;
        this.y = y_random;
        this.e = e;
    }

    @Override
    public String toString() {
        return "(" + new_node_starting_index + ", " + x + ", " + y + ", " + e.toString() + ")";
    }
}
