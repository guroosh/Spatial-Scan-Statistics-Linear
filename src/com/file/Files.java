package com.file;

import com.Executor.Main;
import com.graph.components.Activity;
import com.graph.components.Edge;
import com.graph.components.Tuple;

import java.io.*;
import java.util.ArrayList;

public class Files {

    public static void readingNodeFile(int min_coor_x, int min_coor_y, int max_coor_x, int max_coor_y)
    {

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
                                Main.x.add(Double.parseDouble(s[1]));
                                Main.y.add(Double.parseDouble(s[2]));
                                Main.node_set.add(Integer.parseInt(s[0]));
                                Main.node_map.put(Integer.parseInt(s[0]), index);
                                Main.rev_node_map.put(index, Integer.parseInt(s[0]));
                                index++;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readingEdgeFile() {
        BufferedReader br;
        FileReader fr;

        try {
            fr = new FileReader("edges.txt");
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
//                System.out.println(sCurrentLine);
                String[] s = sCurrentLine.split(" ");
                if (Main.node_set.contains(Integer.parseInt(s[1])) && Main.node_set.contains(Integer.parseInt(s[2]))) {
                    Edge new_edge = new Edge(Integer.parseInt(s[1]), Integer.parseInt(s[2]), Double.parseDouble(s[3]));
                    Main.edges.add(new_edge);
                    if (Main.node_to_edges_map.containsKey(Integer.parseInt(s[1]))) {
                        Main.node_to_edges_map.get(Integer.parseInt(s[1])).add(new_edge);
                    } else {
                        Main.node_to_edges_map.put(Integer.parseInt(s[1]), new ArrayList<>());
                        Main.node_to_edges_map.get(Integer.parseInt(s[1])).add(new_edge);
                    }
                    if (Main.node_to_edges_map.containsKey(Integer.parseInt(s[2]))) {
                        Main.node_to_edges_map.get(Integer.parseInt(s[2])).add(new_edge);
                    } else {
                        Main.node_to_edges_map.put(Integer.parseInt(s[2]), new ArrayList<>());
                        Main.node_to_edges_map.get(Integer.parseInt(s[2])).add(new_edge);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //writes best path in a file
    public static void write_into_file(ArrayList<ArrayList<Tuple>> best_path) throws IOException {
        File fout = new File("output_best_path.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (ArrayList<Tuple> at : best_path) {
            for (Tuple t : at) {
                bw.write(t.x + " " + t.y);
                bw.newLine();
            }
            bw.write("#");
            bw.newLine();
        }
        bw.close();
    }

    //writes all activities in a file
    public static void write_into_file3(ArrayList<Activity> activities) throws IOException {
        File fout = new File("output_activities.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (Activity a : activities) {
            bw.write(a.x + " " + a.y);
            bw.newLine();
        }
        bw.close();
    }

    //writes all edges in a file
    public static void write_into_file2(ArrayList<Edge> edges) throws IOException {
        File fout = new File("output_edges.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (Edge edge : edges) {
            bw.write(Main.x.get(Main.node_map.get(edge.n1)) + " " + Main.y.get(Main.node_map.get(edge.n1)) + " " +
                    Main.x.get(Main.node_map.get(edge.n2)) + " " + Main.y.get(Main.node_map.get(edge.n2)));
            bw.newLine();
        }
        bw.close();
    }

    //writes all nodes in a file
    public static void write_into_file(ArrayList<Double> x, ArrayList<Double> y) throws IOException {

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
