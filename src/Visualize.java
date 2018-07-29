import com.graph.components.Activity;
import com.graph.components.Edge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * @author Cay Horstmann
 * @version 1.32 2007-04-14
 */
public class Visualize {
    public static ArrayList<Double> x;
    public static ArrayList<Double> y;
    public static ArrayList<Edge> edges;
    public static ArrayList<Activity> activities;
    public static String title;

    public void drawPoints(ArrayList<Double> x1, ArrayList<Double> y1, ArrayList<Edge> edges1, ArrayList<Activity> activities1, String title1) {
        EventQueue.invokeLater(() -> {
            DrawFrameNaive frame = new DrawFrameNaive();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            x = x1;
            y = y1;
            edges = edges1;
            activities = activities1;
            title = title1;
        });
    }
}

/**
 * A frame that contains a panel with drawings
 */
class DrawFrameNaive extends JFrame {
    public DrawFrameNaive() {
        setTitle(Visualize.title);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setSize((int) width, (int) height);
        add(new JPanel(), BorderLayout.NORTH);
        add(new JScrollPane(), BorderLayout.CENTER);
        DrawComponentNaive component = new DrawComponentNaive();
        add(component);
    }
}

/**
 * A component that displays rectangles and ellipses.
 */
class DrawComponentNaive extends JComponent {
    double min_x = 1000000;
    double min_y = 1000000;
    double max_x = -1000000;
    double max_y = -1000000;
    double leftX = 0;
    double topY = 0;
    //    double width = 1300;
//    double height = 680;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();

    Color background = Color.WHITE;
    Color grids = Color.WHITE;
    Color points_main = Color.BLACK;
    Color points_scattered = Color.RED;
    Color naive_circles = Color.GREEN;
    Color moving_circles = Color.BLUE;

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        g2.setColor(background);
        drawBackground(g2);
        g2.setColor(grids);
        drawPoints(g2, x, y);
        g2.setColor(naive_circles);
        g2.setStroke(new BasicStroke(2));
//        drawCircles(g2);
    }

    private void drawBackground(Graphics2D g2) {
        Rectangle2D rect = new Rectangle2D.Double(leftX, topY, width, height);
        g2.fill(rect);
    }

//    private void drawCircles(Graphics2D g2) {
//        double average_x = 0;
//        double average_y = 0;
//
//
//        int total = VisualizeNaive.circles.size();
//        total -= 4;
////        System.out.println("Total circles drawn: " + total);
//
////        System.out.println(VisualizeNaive.circles.size());
//
//        //uncomment if problem
////        for (Circle circle : VisualizeNaive.circles) {
////            if (circle.getRadius() == -7) {
////                continue;
////            }
////            double x = circle.getX_coord();
////            double y = circle.getY_coord();
////            double r = circle.getRadius();
////            average_x += x;
////            average_y += y;
////            if (x - r < min_x)
////                min_x = x - r;
////            if (x + r > max_x)
////                max_x = x + r;
////            if (y - r < min_y)
////                min_y = y - r;
////            if (y + r > max_y)
////                max_y = y + r;
////        }
//
//        average_x /= total;
//        average_y /= total;
////        System.out.println("Average: " + average_x + ", " + average_y);
////        System.out.println("Range: " + min_x + " to " + max_x + " and " + min_y + " to " + max_y);
//        int i = 0;
//        for (Circle circle : VisualizeNaive.circles) {
//            if (circle.getRadius() == -7) {
//                g2.setColor(moving_circles);
//                continue;
//            }
//            i++;
//            double x = circle.getX_coord();
//            double y = circle.getY_coord();
//            double r = circle.getRadius();
//
//            Circle scaled_circle = new Circle();
//            //there will be two radius after scaling, since uneven scaling
//            double radius_horizontal = width * (r) / (max_x - min_x);
//            double radius_vertical = height * (r) / (max_y - min_y);
//            scaled_circle.setX_coord(width * (x - min_x) / (max_x - min_x));
//            scaled_circle.setY_coord(height * (y - min_y) / (max_y - min_y));
//
//            Ellipse2D.Double scaled_ellipse = new Ellipse2D.Double(scaled_circle.getX_coord() - radius_horizontal, scaled_circle.getY_coord() - radius_vertical, 2 * radius_horizontal, 2 * radius_vertical);
//            g2.draw(scaled_ellipse);
//        }
//    }

    private void drawPoints(Graphics2D g2, ArrayList<Double> new_x, ArrayList<Double> new_y) {
        Ellipse2D.Double ellipse;
        int point_size;
        for (int i = 0; i < new_x.size(); i++) {
            g2.setColor(points_main);
            point_size = 3;
            ellipse = new Ellipse2D.Double(new_x.get(i), new_y.get(i), point_size, point_size);
            g2.fill(ellipse);
            ellipse = new Ellipse2D.Double(0, 0, point_size, point_size);
            g2.fill(ellipse);
        }
    }

}