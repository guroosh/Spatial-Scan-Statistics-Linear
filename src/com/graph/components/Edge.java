package com.graph.components;

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
