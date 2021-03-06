package com.Executor;

import com.file.Files;
import com.graph.components.*;
import com.graph.functions.Dijkstra;
import edu.rice.hj.api.SuspendableException;

import java.io.*;
import java.util.*;

import static com.file.Files.write_into_file;
import static com.file.Files.write_into_file2;
import static com.file.Files.write_into_file3;
import static edu.rice.hj.Module0.finish;
import static edu.rice.hj.Module0.launchHabaneroApp;
import static edu.rice.hj.Module1.forasync;
import static edu.rice.hj.runtime.config.HjSystemProperty.numWorkers;

public class Main {

    public static ArrayList<Edge> edges = new ArrayList<>();
    public static HashMap<Integer, ArrayList<Edge>> node_to_edges_map = new HashMap<>();
    public static double[][] distances;
    public static Path[][] paths;
    public static ArrayList<Double> x = new ArrayList<>();
    public static ArrayList<Double> y = new ArrayList<>();
    public static HashSet<Integer> node_set = new HashSet<>();
    public static HashMap<Integer, Integer> rev_node_map = new HashMap<>();
    private static HashMap<Integer, Integer> activity_map = new HashMap<>();
    public static HashMap<Integer, Integer> node_map = new HashMap<>();
    public static ArrayList<ArrayList<Integer>> number_of_activities_between_nodes = new ArrayList<>();
    private static int num_n, num_a;
    private static ArrayList<Activity> activities = new ArrayList<>();
    private static Path[][] activity_path_matrix;
    private static double[][] matrix2;
    private static int[][] number_of_activities_between_activities;
    private static double[][] final_answer;
    private static long startTime;

    private static void habenero_execute() {
        //nodes ending on 21075
        //activities starting on 21100
        int new_nodes_starting_index = 21100;
        // max_coor_x = -math.inf
        // max_coor_y = -math.inf
        // min_coor_x = math.inf
        // min_coor_y = math.inf
        System.out.println("\nNUMBER OF THREADS: " + numWorkers.getPropertyValue());
        int max_coor_x = -116;
        int max_coor_y = 36;
        int min_coor_x = -117;
        int min_coor_y = 35;
//
//        int max_coor_x = 3;
//        int max_coor_y = 3;
//        int min_coor_x = -1;
//        int min_coor_y = -1;

        Files.readingNodeFile(min_coor_x, min_coor_y, max_coor_x, max_coor_y);

        System.out.println(x.size());
        num_n = x.size();
        distances = new double[num_n][num_n];
        paths = new Path[num_n][num_n];

        //reading edge file into edges, only if both nodes are loaded
        Files.readingEdgeFile();
//startcounter
        startTime = System.currentTimeMillis();
        System.out.println(edges.size());
        launchHabaneroApp(Main::startingDijkstra_hj);
        generatingActivitiesForEachEdge(new_nodes_starting_index);

        num_a = activities.size();
        activity_path_matrix = new Path[num_a][num_a];
        matrix2 = new double[num_a][num_a];
        number_of_activities_between_activities = new int[num_a][num_a];
        final_answer = new double[num_a][num_a];

        launchHabaneroApp(Main::addingDistanceAndPathsBetweenActivities_hj);

        addingNumberOfActivitiesBetweenNodes();
        launchHabaneroApp(Main::addingNumberOfActivitiesBetweenActivities_hj);
//        printingEveryThing(matrix_, activities, edges, number_of_activities_between_activities, node_to_edges_map, matrix2, activity_path_matrix);
        addingFinalAnswerMatrix();
        try {
            calculatingTheBestAnswerAndPlottingIt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total time: "+ Long.toString(System.currentTimeMillis() - startTime));
        //ending it
    }

    private static void addingDistanceAndPathsBetweenActivities_hj() {
        // matrix2 stores minimum distance between all activity pairs
        for (int i = 0; i < num_a; i++) {
            matrix2[i][i] = 0;
        }

        // activity_path_matrix stores shorted paths(nodes) between all activity pairs

        for (int i = 0; i < num_a; i++) {
            activity_path_matrix[i][i] = new Path();
        }

        // updating the above 2 matrices
        try {
            finish(() -> {
                forasync(0, activities.size(), i -> {
                    //                for (Activity a1 : activities) {
                    forasync(0, activities.size(), j -> {
                        if (activities.get(i) != activities.get(j)) {
                            DistAndPathSingle distAndPathSingle = DistAndPath.get_distance_and_path_using_filter(activities.get(i), activities.get(j));
                            matrix2[activity_map.get(activities.get(i).new_node_starting_index)][activity_map.get(activities.get(j).new_node_starting_index)] = distAndPathSingle.dist;
                            matrix2[activity_map.get(activities.get(j).new_node_starting_index)][activity_map.get(activities.get(i).new_node_starting_index)] = distAndPathSingle.dist;
                            activity_path_matrix[activity_map.get(activities.get(i).new_node_starting_index)][activity_map.get(activities.get(j).new_node_starting_index)] = distAndPathSingle.path;
                            Path temp_path = new Path();
                            for (int k = distAndPathSingle.path.path.size() - 1; k >= 0; k--) {
                                temp_path.path.add(distAndPathSingle.path.path.get(k));
                            }
                            activity_path_matrix[activity_map.get(activities.get(j).new_node_starting_index)][activity_map.get(activities.get(i).new_node_starting_index)] = temp_path;
                        }
                    });
                    //                }
                });
            });
        } catch (SuspendableException e) {
            e.printStackTrace();
        }
    }


    public static void addingNumberOfActivitiesBetweenActivities_hj() {
        //updating number_of_activities_between_activities
        for (int i = 0; i < num_a; i++) {
            number_of_activities_between_activities[i][i] = -1;
        }
        try {
            finish(() -> {
                forasync(0,num_a, i -> {
                    //         for (int i = 0; i < num_a; i++) {
                    forasync(0,num_a, j -> {
//                     for (int j = 0; j < num_a; j++) {
                        Activity a1 = activities.get(i);
                        Activity a2 = activities.get(j);
                        if (a1 != a2) {
                            number_of_activities_between_activities[i][j] = Activity.get_number_between_two_activities(a1, a2);
                            number_of_activities_between_activities[j][i] = Activity.get_number_between_two_activities(a1, a2);
                        }
                    });
                    //         }
                });});
        } catch (SuspendableException e) {
            e.printStackTrace();
        }
    }



    private static void serial_execute() {

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

        Files.readingNodeFile(min_coor_x, min_coor_y, max_coor_x, max_coor_y);

        System.out.println(x.size());
        num_n = x.size();
        distances = new double[num_n][num_n];
        paths = new Path[num_n][num_n];

        //reading edge file into edges, only if both nodes are loaded
        Files.readingEdgeFile();

        System.out.println(edges.size());

        startTime = System.currentTimeMillis();
        startingDijkstra();

        generatingActivitiesForEachEdge(new_nodes_starting_index);

        num_a = activities.size();

        activity_path_matrix = new Path[num_a][num_a];
        matrix2 = new double[num_a][num_a];
        number_of_activities_between_activities = new int[num_a][num_a];
        final_answer = new double[num_a][num_a];

        addingDistanceAndPathsBetweenActivities();
        addingNumberOfActivitiesBetweenNodes();
        addingNumberOfActivitiesBetweenActivities();
//        printingEveryThing(matrix_, activities, edges, number_of_activities_between_activities, node_to_edges_map, matrix2, activity_path_matrix);
        addingFinalAnswerMatrix();
        try {
            calculatingTheBestAnswerAndPlottingIt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Total time: "+ Long.toString(System.currentTimeMillis() - startTime));
    }
    //prints everything
    private static void printingEveryThing(double[][] matrix_, ArrayList<Activity> activities, ArrayList<Edge> edges, int[][] number_of_activities_between_activities, HashMap<Integer, ArrayList<Edge>> node_to_edges_map, double[][] matrix2, Path[][] activity_path_matrix) {
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

        System.out.println("com.graph.components.Activity map");
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
        System.out.println(distances.length);
        System.out.println();

        System.out.println("Shortest path between all nodes");
        for (Path[] h : paths) {
            for (Path h2 : h) {
                System.out.print(h2 + ", ");
            }
            System.out.println();
        }
        System.out.println(paths.length);
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
    }

    public static void calculatingTheBestAnswerAndPlottingIt() throws IOException {
        double min_distance_threshold = 0.1; //-1;
        double max_distance_threshold = 0.3; //100;
        double max_ratio = Double.MIN_VALUE;

        int topN = 5;

        int max_a1[] = new int[topN];
        for (int i = 0; i < topN; i++) {
            max_a1[i] = -1;
        }
        int max_a2[] = new int[topN];
        for (int i = 0; i < topN; i++) {
            max_a2[i] = -1;
        }
//        int max_a1 = -1;
//        int max_a2 = -1;

        System.out.println();
        HashSet<SortingClass> final_answers_1D_ = new HashSet<>();
        for (int i = 0; i < num_a; i++) {
            for (int j = 0; j < num_a; j++) {
//                System.out.println(final_answer[i][j] + ", " + matrix2[i][j]);
//                System.out.print(final_answer[i][j] + ", ");
//                if (final_answer[i][j] > max_ratio) {
                if (matrix2[i][j] > min_distance_threshold && matrix2[i][j] < max_distance_threshold) {
                    final_answers_1D_.add(new SortingClass(final_answer[i][j], i, j));
//                        max_ratio = final_answer[i][j];
//                        max_a1 = i;
//                        max_a2 = j;
                }
//                }
            }

//            System.out.println();
        }
        ArrayList<SortingClass> final_answers_1D = new ArrayList<>(final_answers_1D_);
        System.out.println(final_answers_1D.size());
        System.out.println(final_answer.length);
        Collections.sort(final_answers_1D);
        int c = 0;
        int flag = 0;
        HashSet<Integer> visited = new HashSet<>();
        ArrayList<Path> paths = new ArrayList<>();
        for (SortingClass d : final_answers_1D) {
            if (c < topN) {

                max_a1[c] = d.i;
                max_a2[c] = d.j;
                Path path = activity_path_matrix[max_a1[c]][max_a2[c]];
                for (Integer p : path.path) {
                    if (visited.contains(p)) {
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    visited.addAll(path.path);
                    System.out.println(matrix2[d.i][d.j]);
                    System.out.println("###: " + d.toString());
                    System.out.println("Activity A: " + node_map.get(activities.get(max_a1[c]).e.n1) + " " + node_map.get(activities.get(max_a1[c]).e.n2) + " " + activities.get(max_a1[c]).toString());
                    System.out.println("Activity B: " + node_map.get(activities.get(max_a2[c]).e.n1) + " " + node_map.get(activities.get(max_a2[c]).e.n2) + " " + activities.get(max_a2[c]).toString());
                    System.out.println(path.toString() + " ");
                    paths.add(path);
                    c++;
                }
                flag = 0;
            }
        }


        //STARTING PLOTTING (WRITING IT INTO FILES)

        ArrayList<ArrayList<Tuple>> best_paths = new ArrayList<>();
        for (int i = 0; i < topN; i++) {
            ArrayList<Tuple> best_path = new ArrayList<>();
            if (paths.get(i).path.size() == 0) {
                best_path.add(new Tuple(activities.get(max_a1[i]).x, activities.get(max_a1[i]).y));
                best_path.add(new Tuple(activities.get(max_a2[i]).x, activities.get(max_a2[i]).y));
            } else if (paths.get(i).path.size() == 1) {
                best_path.add(new Tuple(activities.get(max_a1[i]).x, activities.get(max_a1[i]).y));
                best_path.add(new Tuple(x.get(paths.get(i).path.get(0)), y.get(paths.get(i).path.get(0))));
                best_path.add(new Tuple(activities.get(max_a2[i]).x, activities.get(max_a2[i]).y));
            } else {
                if (node_map.get(activities.get(max_a1[i]).e.n1).equals(paths.get(i).path.get(0))) {
                    best_path.add(new Tuple(activities.get(max_a1[i]).x, activities.get(max_a1[i]).y));
                    for (int p : paths.get(i).path) {
                        best_path.add(new Tuple(x.get(p), y.get(p)));
                    }
                    best_path.add(new Tuple(activities.get(max_a2[i]).x, activities.get(max_a2[i]).y));
                } else if (node_map.get(activities.get(max_a1[i]).e.n2).equals(paths.get(i).path.get(0))) {
                    best_path.add(new Tuple(activities.get(max_a1[i]).x, activities.get(max_a1[i]).y));
                    for (int p : paths.get(i).path) {
                        best_path.add(new Tuple(x.get(p), y.get(p)));
                    }
                    best_path.add(new Tuple(activities.get(max_a2[i]).x, activities.get(max_a2[i]).y));
                } else {
                    best_path.add(new Tuple(activities.get(max_a2[i]).x, activities.get(max_a2[i]).y));
                    for (int p : paths.get(i).path) {
                        best_path.add(new Tuple(x.get(p), y.get(p)));
                    }
                    best_path.add(new Tuple(activities.get(max_a1[i]).x, activities.get(max_a1[i]).y));
                }
            }
            best_paths.add(best_path);
        }
        write_into_file(best_paths);
        write_into_file(x, y);
        write_into_file2(edges);
        write_into_file3(activities);
    }

    public static void addingFinalAnswerMatrix() {
        //starting calculations for the final answer
        double total_weight = 0;
        for (Edge e : edges) {
            total_weight += Edge.get_distance(e);
        }

        int total_activities = activities.size();
        System.out.println("Total weight: " + total_weight);
        System.out.println("Total activities: " + total_activities);
        System.out.println();

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
    }

    public static void addingNumberOfActivitiesBetweenActivities() {
        //updating number_of_activities_between_activities
        for (int i = 0; i < num_a; i++) {
            number_of_activities_between_activities[i][i] = -1;
        }
        for (int i = 0; i < num_a; i++) {
            for (int j = 0; j < num_a; j++) {
                Activity a1 = activities.get(i);
                Activity a2 = activities.get(j);
                if (a1 != a2) {
                    number_of_activities_between_activities[i][j] = Activity.get_number_between_two_activities(a1, a2);
                    number_of_activities_between_activities[j][i] = Activity.get_number_between_two_activities(a1, a2);
                }
            }
        }
    }

    public static void addingNumberOfActivitiesBetweenNodes() {
        //updating number_of_activities_between_nodes
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
    }

    public static void addingDistanceAndPathsBetweenActivities() {
        // matrix2 stores minimum distance between all activity pairs
        for (int i = 0; i < num_a; i++) {
            matrix2[i][i] = 0;
        }

        // activity_path_matrix stores shorted paths(nodes) between all activity pairs

        for (int i = 0; i < num_a; i++) {
            activity_path_matrix[i][i] = new Path();
        }

        // updating the above 2 matrices
        for (Activity a1 : activities) {
            for (Activity a2 : activities) {
                if (a1 != a2) {
                    DistAndPathSingle distAndPathSingle = DistAndPath.get_distance_and_path_using_filter(a1, a2);
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
    }

    public static void generatingActivitiesForEachEdge(int new_nodes_starting_index) {
        //generating activities for each edge
        int index1 = 0;

        for (Edge e : edges) {
            double x1 = x.get(node_map.get(e.n1));
            double x2 = x.get(node_map.get(e.n2));
            double y1 = y.get(node_map.get(e.n1));
            double y2 = y.get(node_map.get(e.n2));
            Random r = new Random();
            int number_of_activities = r.nextInt(3);
            ArrayList<Tuple> temp_activity_list = new ArrayList<>();
            for (int i = 0; i < number_of_activities; i++) {
                double x_random, y_random;
                if (x1 != x2) {
                    double slope = (y1 - y2) / (x1 - x2);
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
            e.number_of_activities += number_of_activities;
            e.activity_tuples = temp_activity_list;
        }
    }

    public static void startingDijkstra_hj() {
        // matrix_ is used as input for dijkstra's
        double[][] matrix_ = new double[num_n][num_n];

                                    for (int i = 0; i < num_n; i++) {
                                               matrix_[i][i] = 0;
                                    }


        for (Edge e : edges) {
            int p1 = node_map.get(e.n1);
            int p2 = node_map.get(e.n2);
            double dist = Edge.get_distance(e);
            matrix_[p1][p2] = dist;
            matrix_[p2][p1] = dist;
        }
        com.graph.functions.Dijkstra g = new Dijkstra();

                try {
            finish(() -> {
                                forasync(0,num_n, i ->{
//        for (int i = 0; i < num_n; i++) {
            DistAndPath distAndPath = g.dijkstra(matrix_, i, num_n);
            distances[i] = distAndPath.dist;
            paths[i] = distAndPath.path;
//        }
    });
            });
        } catch (SuspendableException e) {
            e.printStackTrace();
        }
    }

    public static void startingDijkstra() {
        // matrix_ is used as input for dijkstra's
        double[][] matrix_ = new double[num_n][num_n];
//        try {
//            finish(() -> {
//                                forasync(0,num_n, i ->{
        for (int i = 0; i < num_n; i++) {
            matrix_[i][i] = 0;
        }

//             });
//            });
//        } catch (SuspendableException e) {
//            e.printStackTrace();
//        }
        for (Edge e : edges) {
            int p1 = node_map.get(e.n1);
            int p2 = node_map.get(e.n2);
            double dist = Edge.get_distance(e);
            matrix_[p1][p2] = dist;
            matrix_[p2][p1] = dist;
        }
        com.graph.functions.Dijkstra g = new Dijkstra();
        for (int i = 0; i < num_n; i++) {
                    DistAndPath distAndPath = g.dijkstra(matrix_, i, num_n);
                    distances[i] = distAndPath.dist;
                    paths[i] = distAndPath.path;
        }


    }

    public static void main(String args[]) throws IOException {
        serial_execute();
//        habenero_execute();
    }


}

class SortingClass implements Comparable {
    double value;
    int i;
    int j;

    public SortingClass(double v, int i, int j) {
        value = v;
        this.i = i;
        this.j = j;
    }

    @Override
    public int compareTo(Object o) {
        SortingClass thisObject = (SortingClass) o;
        if (thisObject.value > this.value)
            return 1;
        else if (thisObject.value < this.value)
            return -1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return value + ", " + i + ", " + j;
    }
}