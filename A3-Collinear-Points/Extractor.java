import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Extractor.java. Implements feature extraction for collinear points in
 * two dimensional data.
 *
 * @author  Will Humphlett (wah0028@auburn.edu)
 * @author  Dean Hendrix (dh@auburn.edu)
 * @version 10/06/2019
 *
 */
public class Extractor {
   
   /** raw data: all (x,y) points from source data. */
   private Point[] points;
   
   /** lines identified from raw data. */
   private SortedSet<Line> lines;
  
   /**
    * Builds an extractor based on the points in the file named by filename. 
    */
   public Extractor(String filename) {
      try {
         Scanner scan = new Scanner(new File(filename));
         int size = scan.nextInt();
         points = new Point[size];
         for (int i = 0; i < size; i++) {
            int x = scan.nextInt();
            int y = scan.nextInt();
            points[i] = new Point(x, y);
         }
      }
      catch (java.io.IOException e) {
         System.err.println("File not found");
      }
   }
  
   /**
    * Builds an extractor based on the points in the Collection named by pcoll. 
    *
    * THIS METHOD IS PROVIDED FOR YOU AND MUST NOT BE CHANGED.
    */
   public Extractor(Collection<Point> pcoll) {
      points = pcoll.toArray(new Point[]{});
   }
  
   /**
    * Returns a sorted set of all line segments of exactly four collinear
    * points. Uses a brute-force combinatorial strategy. Returns an empty set
    * if there are no qualifying line segments.
    */
   public SortedSet<Line> getLinesBrute() {
      Line aux = new Line();
      lines = new TreeSet<Line>();
      for (int i = 0; i < points.length; i++) { // P1
         for (int j = i + 1; j < points.length; j++) { // P2
            for (int k = j + 1; k < points.length; k++) { // P3
               for (int l = k + 1; l < points.length; l++) { // P4
                  aux.add(points[i]);
                  aux.add(points[j]);
                  aux.add(points[k]);
                  aux.add(points[l]);
                  if (aux.length() == 4) { // If line is successful, add to lines
                     lines.add(aux);
                  }
                  aux = new Line();
               }
            }
         }
      }
      return lines;
   }
  
   /**
    * Returns a sorted set of all line segments of at least four collinear
    * points. The line segments are maximal; that is, no sub-segments are
    * identified separately. A sort-and-scan strategy is used. Returns an empty
    * set if there are no qualifying line segments.
    */
   public SortedSet<Line> getLinesFast() {
      Point[] plist = Arrays.copyOf(points, points.length);
      lines = new TreeSet<Line>();
      Line aux = new Line();
      boolean contAdd = true;
      for (int i = 0; i < points.length; i++) { // Loop through array
         Arrays.sort(plist, points[i].slopeOrder); // Sorts each loop with respec to current point
         for (int j = 0; j < points.length; j++) {
            aux.add(plist[0]); // Start with first point
            contAdd = aux.add(plist[j]); // Attempt to add next point
            if (!contAdd) { // if Cannot continue to add
               if (aux.length() >= 4) { // Adds line if longer that 4
                  lines.add(aux);
               }
               // Start new line beginning at the current point
               aux = new Line();
               aux.add(plist[j]);
            }
         }
      }
      return lines;
   }
}
