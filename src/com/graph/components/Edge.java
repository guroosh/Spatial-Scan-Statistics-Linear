package com.graph.components;

import com.Executor.Main;

import java.util.ArrayList;

public class Edge {
    public int n1;
    public int n2;
    private double distance;
    public int number_of_activities;
    public ArrayList<Tuple> activity_tuples;

    public Edge(int n1, int n2, double distance) {
        this.n1 = n1;
        this.n2 = n2;
        this.distance = distance;
        number_of_activities = 0;
    }

    //Returns length of an edge(between 2 nodes)
    public static double get_distance(Edge e) {
        double x1 = Main.x.get(Main.node_map.get(e.n1));
        double x2 = Main.x.get(Main.node_map.get(e.n2));
        double y1 = Main.y.get(Main.node_map.get(e.n1));
        double y2 = Main.y.get(Main.node_map.get(e.n2));
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[" + n1 + ", " + n2 + ", " + distance + ", " + number_of_activities + ", [");
        for (Tuple t : activity_tuples) {
            s.append("(").append(t.x).append(", ").append(t.y).append("), ");
        }
        s.append("]");
        return s.toString();
    }
}
