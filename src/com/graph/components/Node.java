package com.graph.components;

import com.Executor.Main;

public class Node {
    //Returns number of activities between a node and an activity on same edge
    public static int get_points2(Activity a, int n) {
        double dist = get_distance2(a, n);
        int ret_val = 0;
        for (Tuple other_a : a.e.activity_tuples) {
            if (!(other_a.x == a.x && other_a.y == a.y)) {
                double dist2 = get_distance5(other_a.x, other_a.y, n);
                if (dist2 < dist) {
                    ret_val++;
                }
            }
        }
        return ret_val;
    }

    //Returns direct distance between (x,y) and a node
    private static double get_distance5(double x1, double y1, int n) {
        double x2 = Main.x.get(Main.node_map.get(n));
        double y2 = Main.y.get(Main.node_map.get(n));
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    //Returns direct distance between a node and an activity
    public static double get_distance2(Activity a, int n) {
        double x1 = a.x;
        double y1 = a.y;
        double x2 = Main.x.get(Main.node_map.get(n));
        double y2 = Main.y.get(Main.node_map.get(n));
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}
