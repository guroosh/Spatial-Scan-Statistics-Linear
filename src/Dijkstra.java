import java.lang.*;

class Dijkstra {

    private int minDistance(double dist[], Boolean sptSet[], int V) {
        double min = Double.MAX_VALUE;
        int min_index = -1;
        for (int v = 0; v < V; v++)
            if (!sptSet[v] && dist[v] < min) {
                min = dist[v];
                min_index = v;
            }
        return min_index;
    }

    private void printSolution(double dist[], int V) {
        System.out.println("Vertex   Distance from Source");
        for (int i = 0; i < V; i++)
            System.out.println(i + " tt " + dist[i]);
    }

    public DistAndPath dijkstra(double[][] graph, int src, int V) {
        double dist[] = new double[V];
        Path parent[] = new Path[V];
        Boolean sptSet[] = new Boolean[V];
        for (int i = 0; i < V; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
            parent[i] = new Path();
        }
        parent[src].path.add(src);
        dist[src] = 0;
        for (int count = 0; count < V; count++) {
            int u = minDistance(dist, sptSet, V);
            sptSet[u] = true;
            for (int v = 0; v < V; v++) {
                if (!sptSet[v] && graph[u][v] > 0 &&
                        dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                    parent[v].path.addAll(parent[u].path);
                    parent[v].path.add(v);
                }
            }
        }
        return new DistAndPath(dist, parent);
//        printSolution(dist, V);
//        System.out.println();
//        for (Path p : parent) {
//            System.out.println(p.toString());
//        }
    }

//    public static void main(String[] args) {
//        /* Let us create the example graph discussed above */
//        double graph[][] = new double[][]{{0, 4, 0, 0, 0, 0, 0, 8, 0},
//                {4, 0, 8, 0, 0, 0, 0, 11, 0},
//                {0, 8, 0, 7, 0, 4, 0, 0, 2},
//                {0, 0, 7, 0, 9, 14, 0, 0, 0},
//                {0, 0, 0, 9, 0, 10, 0, 0, 0},
//                {0, 0, 4, 14, 10, 0, 2, 0, 0},
//                {0, 0, 0, 0, 0, 2, 0, 1, 6},
//                {8, 11, 0, 0, 0, 0, 1, 0, 7},
//                {0, 0, 2, 0, 0, 0, 6, 7, 0}};
//        Dijkstra t = new Dijkstra();
//        t.dijkstra(graph, 0, 9);
//    }
}