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

    //Returns number of activities between any 2 activities
    public static int get_number_between_two_activities(Activity a1, Activity a2) {
        int n1 = a1.e.n1;
        int n2 = a1.e.n2;
        int n3 = a2.e.n1;
        int n4 = a2.e.n2;
        double n1n3 = com.Executor.Main.distances[com.Executor.Main.node_map.get(n1)][com.Executor.Main.node_map.get(n3)];
        double n1n4 = com.Executor.Main.distances[com.Executor.Main.node_map.get(n1)][com.Executor.Main.node_map.get(n4)];
        double n2n3 = com.Executor.Main.distances[com.Executor.Main.node_map.get(n2)][com.Executor.Main.node_map.get(n3)];
        double n2n4 = com.Executor.Main.distances[com.Executor.Main.node_map.get(n2)][com.Executor.Main.node_map.get(n4)];
        double a1n1 = Node.get_distance2(a1, n1);
        double a1n2 = Node.get_distance2(a1, n2);
        double a2n3 = Node.get_distance2(a2, n3);
        double a2n4 = Node.get_distance2(a2, n4);
        if ((n1 == n3 && n2 == n4) || (n1 == n4 && n2 == n3))
            return get_points3(a1, a2);
        double P = a1n1 + n1n3 + a2n3;
        double Q = a1n2 + n2n3 + a2n3;
        double R = a1n1 + n1n4 + a2n4;
        double S = a1n2 + n2n4 + a2n4;
        ReturnObject _2nodes = new ReturnObject();
        if (P <= Q && P <= R && P <= S)
            _2nodes = new ReturnObject(n1, n3);
        if (Q <= P && Q <= R && Q <= S)
            _2nodes = new ReturnObject(n2, n3);
        if (R <= Q && R <= P && R <= S)
            _2nodes = new ReturnObject(n1, n4);
        if (S <= Q && S <= R && S <= P)
            _2nodes = new ReturnObject(n2, n4);
        return Node.get_points2(a1, _2nodes.a1_node) + Node.get_points2(a2, _2nodes.a2_node) + com.Executor.Main.number_of_activities_between_nodes.get(com.Executor.Main.node_map.get(_2nodes.a1_node)).get(com.Executor.Main.node_map.get(_2nodes.a2_node));
    }

    //Returns number of activities between 2 activities on same edge
    public static int get_points3(Activity a1, Activity a2) {
        double dist = get_distance3(a1, a2);
        int ret_val = 0;
        for (Tuple other_a : a1.e.activity_tuples) {
            if (!(other_a.x == a1.x && other_a.y == a1.y)) {
                if (!(other_a.x == a2.x && other_a.y == a2.y)) {
                    double dist2 = get_distance4(other_a.x, other_a.y, a1);
                    double dist3 = get_distance4(other_a.x, other_a.y, a2);
                    if (dist2 + dist3 <= dist) {
                        ret_val++;
                    }
                }
            }
        }
        return ret_val;
    }

    //Returns direct distance between 2 activities
    public static double get_distance3(Activity a1, Activity a2) {
        double x1 = a1.x;
        double y1 = a1.y;
        double x2 = a2.x;
        double y2 = a2.y;
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    //Returns direct distance between (x,y) and an activity
    private static double get_distance4(double x1, double y1, Activity a) {
        double x2 = a.x;
        double y2 = a.y;
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    @Override
    public String toString() {
        return "(" + new_node_starting_index + ", " + x + ", " + y + ", " + e.toString() + ")";
    }
}
