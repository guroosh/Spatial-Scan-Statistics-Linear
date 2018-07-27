import java.io.*;
import java.util.*;

public class Main {
    private static int get_number_between_two_activities(Activity a1, Activity a2) {
        int n1 = a1.e.n1;
        int n2 = a1.e.n2;
        int n3 = a2.e.n1;
        int n4 = a2.e.n2;
        double n1n3 = distances.get(node_map.get(n1))[node_map.get(n3)];
        double n1n4 = distances.get(node_map.get(n1))[node_map.get(n4)];
        double n2n3 = distances.get(node_map.get(n2))[node_map.get(n3)];
        double n2n4 = distances.get(node_map.get(n2))[node_map.get(n4)];
        double a1n1 = get_distance2(a1, n1);
        double a1n2 = get_distance2(a1, n2);
        double a2n3 = get_distance2(a2, n3);
        double a2n4 = get_distance2(a2, n4);
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
        return get_points2(a1, _2nodes.a1_node) + get_points2(a2, _2nodes.a2_node) + number_of_activities_between_nodes.get(node_map.get(_2nodes.a1_node)).get(node_map.get(_2nodes.a2_node));
    }

    private static DistAndPathSingle get_distance_and_path_using_filter(Activity a1, Activity a2) {
        int n1 = a1.e.n1;
        int n2 = a1.e.n2;
        int n3 = a2.e.n1;
        int n4 = a2.e.n2;
        double n1n3 = distances.get(node_map.get(n1))[node_map.get(n3)];
        double n1n4 = distances.get(node_map.get(n1))[node_map.get(n4)];
        double n2n3 = distances.get(node_map.get(n2))[node_map.get(n3)];
        double n2n4 = distances.get(node_map.get(n2))[node_map.get(n4)];
        double a1n1 = get_distance2(a1, n1);
        double a1n2 = get_distance2(a1, n2);
        double a2n3 = get_distance2(a2, n3);
        double a2n4 = get_distance2(a2, n4);
        if ((n1 == n3 && n2 == n4) || (n1 == n4 && n2 == n3))
            return new DistAndPathSingle(get_distance3(a1, a2), new Path());
        double P = a1n1 + n1n3 + a2n3;
        double Q = a1n2 + n2n3 + a2n3;
        double R = a1n1 + n1n4 + a2n4;
        double S = a1n2 + n2n4 + a2n4;

        if (P <= Q && P <= R && P <= S)
            return new DistAndPathSingle(P, paths.get(node_map.get(n1))[node_map.get(n3)]);
        if (Q <= P && Q <= R && Q <= S)
            return new DistAndPathSingle(Q, paths.get(node_map.get(n2))[node_map.get(n3)]);
        if (R <= Q && R <= P && R <= S)
            return new DistAndPathSingle(R, paths.get(node_map.get(n1))[node_map.get(n4)]);
        else    //if (S <= Q && S <= R && S <= P)
            return new DistAndPathSingle(S, paths.get(node_map.get(n2))[node_map.get(n4)]);
//        return null;
    }

    private static int get_points3(Activity a1, Activity a2) {
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


    private static int get_points2(Activity a, int n) {
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

    private static double get_distance5(double x1, double y1, int n) {
        double x2 = x.get(node_map.get(n));
        double y2 = y.get(node_map.get(n));
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static double get_distance4(double x1, double y1, Activity a) {
        double x2 = a.x;
        double y2 = a.y;
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static double get_distance3(Activity a1, Activity a2) {
        double x1 = a1.x;
        double y1 = a1.y;
        double x2 = a2.x;
        double y2 = a2.y;
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static double get_distance2(Activity a, int n) {
        double x1 = a.x;
        double y1 = a.y;
        double x2 = x.get(node_map.get(n));
        double y2 = y.get(node_map.get(n));
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static double get_distance(Edge e) {
        double x1 = x.get(node_map.get(e.n1));
        double x2 = x.get(node_map.get(e.n2));
        double y1 = y.get(node_map.get(e.n1));
        double y2 = y.get(node_map.get(e.n2));
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    private static ArrayList<double[]> distances;
    private static ArrayList<Path[]> paths;
    private static ArrayList<Double> x = new ArrayList<>();
    private static ArrayList<Double> y = new ArrayList<>();
    private static HashSet<Integer> node_set = new HashSet<>();
    private static HashMap<Integer, Integer> rev_node_map = new HashMap<>();
    private static HashMap<Integer, Integer> activity_map = new HashMap<>();
    private static HashMap<Integer, Integer> node_map = new HashMap<>();
    private static ArrayList<ArrayList<Integer>> number_of_activities_between_nodes = new ArrayList<>();

    public static void main(String args[]) throws IOException {

        //nodes ending on 21075
        //activities starting on 21100
        int new_nodes_starting_index = 21100;
        // max_coor_x = -math.inf
        // max_coor_y = -math.inf
        // min_coor_x = math.inf
        // min_coor_y = math.inf

        int max_coor_x = -116;
        int max_coor_y = 36;
        int min_coor_x = -117;
        int min_coor_y = 35;
//
//        int max_coor_x = 3;
//        int max_coor_y = 3;
//        int min_coor_x = -1;
//        int min_coor_y = -1;


        BufferedReader br;
        FileReader fr;

        //reading files into nodes
        try {
            fr = new FileReader("nodes.txt");
            br = new BufferedReader(fr);
            String sCurrentLine;
            int index = 0;
            while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
                String[] s = sCurrentLine.split(" ");
                if (min_coor_x < Double.parseDouble(s[1])) {
                    if (Double.parseDouble(s[1]) < max_coor_x) {
                        if (min_coor_y < Double.parseDouble(s[2])) {
                            if (Double.parseDouble(s[2]) < max_coor_y) {
                                x.add(Double.parseDouble(s[1]));
                                y.add(Double.parseDouble(s[2]));
                                node_set.add(Integer.parseInt(s[0]));
                                node_map.put(Integer.parseInt(s[0]), index);
                                rev_node_map.put(index, Integer.parseInt(s[0]));
                                index++;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(x.size());
        int num_n = x.size();

        //reading edge file into edges, only if both nodes are loaded
        ArrayList<Edge> edges = new ArrayList<>();
        HashMap<Integer, ArrayList<Edge>> node_to_edges_map = new HashMap<>();
        try {
            fr = new FileReader("edges.txt");
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
                String[] s = sCurrentLine.split(" ");
                if (node_set.contains(Integer.parseInt(s[1])) && node_set.contains(Integer.parseInt(s[2]))) {
                    Edge new_edge = new Edge(Integer.parseInt(s[1]), Integer.parseInt(s[2]), Double.parseDouble(s[3]));
                    edges.add(new_edge);
                    if (node_to_edges_map.containsKey(Integer.parseInt(s[1]))) {
                        node_to_edges_map.get(Integer.parseInt(s[1])).add(new_edge);
                    } else {
                        node_to_edges_map.put(Integer.parseInt(s[1]), new ArrayList<>());
                        node_to_edges_map.get(Integer.parseInt(s[1])).add(new_edge);
                    }
                    if (node_to_edges_map.containsKey(Integer.parseInt(s[2]))) {
                        node_to_edges_map.get(Integer.parseInt(s[2])).add(new_edge);
                    } else {
                        node_to_edges_map.put(Integer.parseInt(s[2]), new ArrayList<>());
                        node_to_edges_map.get(Integer.parseInt(s[2])).add(new_edge);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(edges.size());

        // matrix_ is used as input for dijkstra's
        double[][] matrix_ = new double[num_n][num_n];
        for (int i = 0; i < num_n; i++) {
            matrix_[i][i] = 0;
        }
        for (Edge e : edges) {
            int p1 = node_map.get(e.n1);
            int p2 = node_map.get(e.n2);
            double dist = get_distance(e);
            matrix_[p1][p2] = dist;
            matrix_[p2][p1] = dist;
        }
        Dijkstra g = new Dijkstra();
        ArrayList<double[]> distances = new ArrayList<>();
        ArrayList<Path[]> paths = new ArrayList<>();
        for (int i = 0; i < num_n; i++) {
            DistAndPath distAndPath = g.dijkstra(matrix_, i, num_n);
            distances.add(distAndPath.dist);
            paths.add(distAndPath.path);
        }
        Main.distances = distances;
        Main.paths = paths;


        //generating activities for each edge
        ArrayList<Activity> activities = new ArrayList<>();
        int index1 = 0;
        for (Edge e : edges) {
//            System.out.println(e.toString());
            double x1 = x.get(node_map.get(e.n1));
            double x2 = x.get(node_map.get(e.n2));
            double y1 = y.get(node_map.get(e.n1));
            double y2 = y.get(node_map.get(e.n2));
//            System.out.println(x1+" "+x2+" "+y1+" "+y2);
            Random r = new Random();
            int number_of_activities = r.nextInt(3);
//            System.out.println(number_of_activities);
            ArrayList<Tuple> temp_activity_list = new ArrayList<>();
            for (int i = 0; i < number_of_activities; i++) {
                double x_random, y_random;
                if (x1 != x2) {
                    double slope = (y1 - y2) / (x1 - x2);

//                    System.out.println(slope);
                    if (slope == 0) {
                        x_random = x1 + (x2 - x1) * r.nextDouble();
                        y_random = y1;

                    } else {
                        y_random = y1 + (y2 - y1) * r.nextDouble();
                        x_random = x1 - ((y1 - y_random) / slope);
                    }
                } else {
                    y_random = y1 + (y2 - y1) * r.nextDouble();
                    x_random = x1;
                }
                temp_activity_list.add(new Tuple(x_random, y_random));
                // plot the random point
                activities.add(new Activity(new_nodes_starting_index, x_random, y_random, e));
                activity_map.put(new_nodes_starting_index, index1);
                new_nodes_starting_index++;
                index1++;
            }
//            Edge e_temp1 = null, e_temp2 = null;
//            ArrayList<Edge> edge_list1 = node_to_edges_map.get(e.n1);
//            ArrayList<Edge> edge_list2 = node_to_edges_map.get(e.n2);
//            for (Edge other1 : edge_list1) {
//                for (Edge other2 : edge_list2) {
//                    if (other1 == other2) {
//                        e_temp1 = other1;
//                        e_temp2 = other2;
//                    }
//                }
//            }
            e.number_of_activities += number_of_activities;
//            e_temp1.number_of_activities += number_of_activities;
//            e_temp2.number_of_activities += number_of_activities;
            e.activity_tuples = temp_activity_list;
//            e_temp1.activity_tuples = temp_activity_list;
//            e_temp2.activity_tuples = temp_activity_list;

        }

        int num_a = activities.size();
        // matrix2 stores minimum distance between all activity pairs
        double[][] matrix2 = new double[num_a][num_a];
        for (int i = 0; i < num_a; i++) {
            matrix2[i][i] = 0;
        }
        // activity_path_matrix stores shorted paths(nodes) between all activity pairs
        Path[][] activity_path_matrix = new Path[num_a][num_a];
        for (int i = 0; i < num_a; i++) {
            activity_path_matrix[i][i] = new Path();
        }
        // updating the above 2 matrices
        for (Activity a1 : activities) {
            for (Activity a2 : activities) {
                if (a1 != a2) {
                    DistAndPathSingle distAndPathSingle = get_distance_and_path_using_filter(a1, a2);
                    matrix2[activity_map.get(a1.new_node_starting_index)][activity_map.get(a2.new_node_starting_index)] = distAndPathSingle.dist;
                    matrix2[activity_map.get(a2.new_node_starting_index)][activity_map.get(a1.new_node_starting_index)] = distAndPathSingle.dist;
                    activity_path_matrix[activity_map.get(a1.new_node_starting_index)][activity_map.get(a2.new_node_starting_index)] = distAndPathSingle.path;
                    Path temp_path = new Path();
                    for (int i = distAndPathSingle.path.path.size() - 1; i >= 0 ; i--) {
                        temp_path.path.add(distAndPathSingle.path.path.get(i));
                    }
                    activity_path_matrix[activity_map.get(a2.new_node_starting_index)][activity_map.get(a1.new_node_starting_index)] = temp_path;
                }
            }
        }

        for (Path[] particular_paths : paths) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (Path p : particular_paths) {
                int count = 0;
                for (int index = 0; index < p.path.size() - 1; index++) {
                    int n1 = p.path.get(index);
                    int n2 = p.path.get(index + 1);
                    int id1 = rev_node_map.get(n1);
                    int id2 = rev_node_map.get(n2);
                    ArrayList<Edge> edge_list1 = node_to_edges_map.get(id1);
                    ArrayList<Edge> edge_list2 = node_to_edges_map.get(id2);
                    for (Edge other1 : edge_list1) {
                        for (Edge other2 : edge_list2) {
                            if (other1 == other2) {
                                count += other1.number_of_activities;
                            }
                        }
                    }
                }
                temp.add(count);
            }
            number_of_activities_between_nodes.add(temp);
        }

        int[][] number_of_activities_between_activities = new int[num_a][num_a];
        for (int i = 0; i < num_a; i++) {
            number_of_activities_between_activities[i][i] = -1;
        }
        for (int i = 0; i < num_a; i++) {
            for (int j = 0; j < num_a; j++) {
                Activity a1 = activities.get(i);
                Activity a2 = activities.get(j);
                if (a1 != a2) {
                    number_of_activities_between_activities[i][j] = get_number_between_two_activities(a1, a2);
                    number_of_activities_between_activities[j][i] = get_number_between_two_activities(a1, a2);
                }
            }
        }


        System.out.println("X");
        for (double h : x) {
            System.out.println(h);
        }
        System.out.println(x.size());
        System.out.println();

        System.out.println("Y");
        for (double h : y) {
            System.out.println(h);
        }
        System.out.println(y.size());
        System.out.println();

        System.out.println("Node set");
        for (double h : node_set) {
            System.out.println(h);
        }
        System.out.println(node_set.size());
        System.out.println();

        System.out.println("Node map");
        for (Map.Entry<Integer, Integer> h : node_map.entrySet()) {
            System.out.println(h.getKey() + ":" + h.getValue());
        }
        System.out.println(node_map.size());
        System.out.println();

        System.out.println("Reverse Node map");
        for (Map.Entry<Integer, Integer> h : rev_node_map.entrySet()) {
            System.out.println(h.getKey() + ":" + h.getValue());
        }
        System.out.println(rev_node_map.size());
        System.out.println();

        System.out.println("Activity map");
        for (Map.Entry<Integer, Integer> h : activity_map.entrySet()) {
            System.out.println(h.getKey() + ":" + h.getValue());
        }
        System.out.println(activity_map.size());
        System.out.println();

        System.out.println("distance between adjacent nodes");
        for (double[] h : matrix_) {
            for (double h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(matrix_.length);
        System.out.println();

        System.out.println("Edges");
        for (Edge h : edges) {
            System.out.println(h);
        }
        System.out.println(edges.size());
        System.out.println();

        System.out.println("Node-> list(edges) dict");
        for (Map.Entry<Integer, ArrayList<Edge>> h : node_to_edges_map.entrySet()) {
            System.out.println(h.getKey() + ":" + h.getValue());
        }
        System.out.println(rev_node_map.size());
        System.out.println();

        System.out.println("distance between all nodes");
        for (double[] h : distances) {
            for (double h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(distances.size());
        System.out.println();

        System.out.println("Shortest path between all nodes");
        for (Path[] h : paths) {
            for (Path h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(paths.size());
        System.out.println();

        System.out.println("Activities");
        for (Activity h : activities) {
            System.out.println(h);
        }
        System.out.println(activities.size());
        System.out.println();

        System.out.println("Number of activities between all node pairs");
        for (ArrayList<Integer> h : number_of_activities_between_nodes) {
            for (int h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(number_of_activities_between_nodes.size());
        System.out.println();

        System.out.println("distance between all activities");
        for (double[] h : matrix2) {
            for (double h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(matrix2.length);
        System.out.println();

        System.out.println("Shortest path between all activities");
        for (Path[] h : activity_path_matrix) {
            for (Path h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(activity_path_matrix.length);
        System.out.println();

        System.out.println("Number of activities between all activities");
        for (int[] h : number_of_activities_between_activities) {
            for (int h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(number_of_activities_between_activities.length);
        System.out.println();

        double total_weight = 0;
        for (Edge e : edges) {
            total_weight += get_distance(e);
        }
        int total_activities = activities.size();
        System.out.println("Total weight: " + total_weight);
        System.out.println("Total activities: " + total_activities);
        System.out.println();

        double[][] final_answer = new double[num_a][num_a];
        for (int i = 0; i < num_a; i++) {
            final_answer[i][i] = 0;
        }

        for (int i = 0; i < num_a; i++) {
            for (int j = 0; j < num_a; j++) {
                if (i != j) {
                    int this_activities = number_of_activities_between_activities[i][j] + 2;
                    double this_weight = matrix2[i][j];
                    final_answer[i][j] = (this_activities / this_weight) / ((total_activities - this_activities) / (total_weight - this_weight));
                    final_answer[j][i] = (this_activities / this_weight) / ((total_activities - this_activities) / (total_weight - this_weight));
                }
            }
        }

        double min_distance_threshold = 0.1; //-1;
        double max_distance_threshold = 0.3; //100;
        double max_ratio = Double.MIN_VALUE;

        int max_a1 = -1;
        int max_a2 = -1;

        for (int i = 0; i < num_a; i++) {
            for (int j = 0; j < num_a; j++) {
                if (final_answer[i][j] > max_ratio) {
                    if (matrix2[i][j] > min_distance_threshold && matrix2[i][j] < max_distance_threshold) {
                        max_ratio = final_answer[i][j];
                        max_a1 = i;
                        max_a2 = j;
                    }
                }
            }
        }
        System.out.println(matrix2[max_a1][max_a2]);
        System.out.println("Max density ratio between A and B: " + max_ratio);
        System.out.println("Activity A: " + node_map.get(activities.get(max_a1).e.n1) + " " + node_map.get(activities.get(max_a1).e.n2) + " " + activities.get(max_a1).toString());
        System.out.println("Activity B: " + node_map.get(activities.get(max_a2).e.n1) + " " + node_map.get(activities.get(max_a2).e.n2) + " " + activities.get(max_a2).toString());
        Path path = activity_path_matrix[max_a1][max_a2];
        System.out.println(path.toString());

        ArrayList<Tuple> best_path = new ArrayList<>();

        if (path.path.size() == 0) {
            best_path.add(new Tuple(activities.get(max_a1).x, activities.get(max_a1).y));
            best_path.add(new Tuple(activities.get(max_a2).x, activities.get(max_a2).y));
        } else if (path.path.size() == 1) {
            best_path.add(new Tuple(activities.get(max_a1).x, activities.get(max_a1).y));
            best_path.add(new Tuple(x.get(path.path.get(0)), y.get(path.path.get(0))));
            best_path.add(new Tuple(activities.get(max_a2).x, activities.get(max_a2).y));
        } else {
            if (node_map.get(activities.get(max_a1).e.n1).equals(path.path.get(0))) {
                best_path.add(new Tuple(activities.get(max_a1).x, activities.get(max_a1).y));
                for (int p : path.path) {
                    best_path.add(new Tuple(x.get(p), y.get(p)));
                }
                best_path.add(new Tuple(activities.get(max_a2).x, activities.get(max_a2).y));
            } else if (node_map.get(activities.get(max_a1).e.n2).equals(path.path.get(0))) {
                best_path.add(new Tuple(activities.get(max_a1).x, activities.get(max_a1).y));
                for (int p : path.path) {
                    best_path.add(new Tuple(x.get(p), y.get(p)));
                }
                best_path.add(new Tuple(activities.get(max_a2).x, activities.get(max_a2).y));
            } else {
                best_path.add(new Tuple(activities.get(max_a2).x, activities.get(max_a2).y));
                for (int p : path.path) {
                    best_path.add(new Tuple(x.get(p), y.get(p)));
                }
                best_path.add(new Tuple(activities.get(max_a1).x, activities.get(max_a1).y));
            }
        }


        write_into_file(best_path);
        write_into_file(x, y);
        write_into_file2(edges);
        write_into_file3(activities);

    }

    private static void write_into_file(ArrayList<Tuple> best_path) throws IOException {
        File fout = new File("output_best_path.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (Tuple t : best_path) {
            bw.write(t.x + " " + t.y);
            bw.newLine();
        }
        bw.close();
    }

    private static void write_into_file3(ArrayList<Activity> activities) throws IOException {
        File fout = new File("output_activities.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (Activity a : activities) {
            bw.write(a.x + " " + a.y);
            bw.newLine();
        }
        bw.close();
    }

    private static void write_into_file2(ArrayList<Edge> edges) throws IOException {
        File fout = new File("output_edges.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (Edge edge : edges) {
            bw.write(x.get(node_map.get(edge.n1)) + " " + y.get(node_map.get(edge.n1)) + " " +
                    x.get(node_map.get(edge.n2)) + " " + y.get(node_map.get(edge.n2)));
            bw.newLine();
        }
        bw.close();
    }

    private static void write_into_file(ArrayList<Double> x, ArrayList<Double> y) throws IOException {

        File fout = new File("output_nodes.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (int i = 0; i < x.size(); i++) {
            bw.write(x.get(i) + " " + y.get(i));
            bw.newLine();
        }
        bw.close();
    }
}

class Activity {
    int new_node_starting_index;
    double x;
    double y;
    Edge e;

    Activity(int new_nodes_starting_index, double x_random, double y_random, Edge e) {
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


class Edge {
    int n1;
    int n2;
    private double distance;
    int number_of_activities;
    ArrayList<Tuple> activity_tuples;

    Edge(int n1, int n2, double distance) {
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

class Path {
    ArrayList<Integer> path;

    Path() {
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

class DistAndPath {
    double[] dist;
    Path[] path;

    DistAndPath(double[] dist, Path[] path) {
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

class DistAndPathSingle {
    double dist;
    Path path;

    DistAndPathSingle(double dist, Path path) {
        this.dist = dist;
        this.path = path;
    }
}

class Tuple {
    double x;
    double y;

    Tuple(double x_random, double y_random) {
        x = x_random;
        y = y_random;
    }
}

class ReturnObject {
    int a1_node;
    int a2_node;

    ReturnObject(int a1_node, int a2_node) {
        this.a1_node = a1_node;
        this.a2_node = a2_node;
    }

    ReturnObject() {

    }
}