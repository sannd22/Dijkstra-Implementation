import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.*;

//	This class generates subsets of a set represented as a string in Java

public class DijkstraTest
{
   public static void main(String[] args)   {
         String file = "grph_dijkstra_test1.txt";
         Dijkstra dike = new Dijkstra();
         int [][] spt = dike.findShortPaths(file);
         //System.out.println("\n-------Test 1--------");
         //System.out.println(Arrays.deepToString(spt));
      }
}