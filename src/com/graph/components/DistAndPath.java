package com.graph.components;

import com.Executor.Main;

public class DistAndPath {
    public double[] dist;
    public Path[] path;

    public DistAndPath(double[] dist, Path[] path) {
        this.dist = dist;
        this.path = path;
    }

    //Returns shortest path and distance between any 2 activities
    public static DistAndPathSingle get_distance_and_path_using_filter(Activity a1, Activity a2) {
        int n1 = a1.e.n1;
        int n2 = a1.e.n2;
        int n3 = a2.e.n1;
        int n4 = a2.e.n2;
        double n1n3 = Main.distances[Main.node_map.get(n1)][Main.node_map.get(n3)];
        double n1n4 = Main.distances[Main.node_map.get(n1)][Main.node_map.get(n4)];
        double n2n3 = Main.distances[Main.node_map.get(n2)][Main.node_map.get(n3)];
        double n2n4 = Main.distances[Main.node_map.get(n2)][Main.node_map.get(n4)];
        double a1n1 = Node.get_distance2(a1, n1);
        double a1n2 = Node.get_distance2(a1, n2);
        double a2n3 = Node.get_distance2(a2, n3);
        double a2n4 = Node.get_distance2(a2, n4);
        if ((n1 == n3 && n2 == n4) || (n1 == n4 && n2 == n3))
            return new DistAndPathSingle(Activity.get_distance3(a1, a2), new Path());
        double P = a1n1 + n1n3 + a2n3;
        double Q = a1n2 + n2n3 + a2n3;
        double R = a1n1 + n1n4 + a2n4;
        double S = a1n2 + n2n4 + a2n4;

        if (P <= Q && P <= R && P <= S)
            return new DistAndPathSingle(P, Main.paths[Main.node_map.get(n1)][Main.node_map.get(n3)]);
        if (Q <= P && Q <= R && Q <= S)
            return new DistAndPathSingle(Q, Main.paths[Main.node_map.get(n2)][Main.node_map.get(n3)]);
        if (R <= Q && R <= P && R <= S)
            return new DistAndPathSingle(R, Main.paths[Main.node_map.get(n1)][Main.node_map.get(n4)]);
        else    //if (S <= Q && S <= R && S <= P)
            return new DistAndPathSingle(S, Main.paths[Main.node_map.get(n2)][Main.node_map.get(n4)]);
//        return null;
    }

    @Override
    public String toString() {
        StringBuilder a = new StringBuilder();
        for (Path p : path)
            a.append(p.toString());
        return a.toString();
    }
}
