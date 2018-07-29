package com.Executor;

import com.file.Files;
import com.graph.components.*;
import com.graph.functions.Dijkstra;

import java.io.*;
import java.util.*;

public class Habenero_Main extends Main{

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

        Files.readingNodeFile(min_coor_x, min_coor_y, max_coor_x, max_coor_y);

        System.out.println(x.size());
        num_n = x.size();
        distances = new double[num_n][num_n];
        paths = new Path[num_n][num_n];

        //reading edge file into edges, only if both nodes are loaded
        Files.readingEdgeFile();

        System.out.println(edges.size());

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
        calculatingTheBestAnswerAndPlottingIt();
    }

}

