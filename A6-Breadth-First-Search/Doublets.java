import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Provides an implementation of the WordLadderGame interface. 
 *
 * @author Will Humphlett (wah0028@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 11-11-2019
 */
public class Doublets implements WordLadderGame {
   private TreeSet<String> lexicon;
   private static final List<String> EMPTY_LADDER = new ArrayList<String>();

   /**
    * Instantiates a new instance of Doublets with the lexicon populated with
    * the strings in the provided InputStream. The InputStream can be formatted
    * in different ways as long as the first string on each line is a word to be
    * stored in the lexicon.
    *
    * @param in File input stream
    */
   public Doublets(InputStream in) {
      try {
         lexicon = new TreeSet<String>();
         Scanner s =
            new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            lexicon.add(str.toLowerCase());
            s.nextLine();
         }
         in.close();
         s.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }

   /**
    * Returns the Hamming distance between two strings, str1 and str2. The
    * Hamming distance between two strings of equal length is defined as the
    * number of positions at which the corresponding symbols are different. The
    * Hamming distance is undefined if the strings have different length, and
    * this method returns -1 in that case. See the following link for
    * reference: https://en.wikipedia.org/wiki/Hamming_distance
    *
    * @param str1 The first string
    * @param str2 The second string
    * @return the Hamming distance between str1 and str2 if they are the
    *                  same length, -1 otherwise
    */
   public int getHammingDistance(String str1, String str2) {
      if (str1.length() != str2.length()) {
         return -1;
      }
      int hamDist = 0;
      for (int i = 0; i < str1.length(); i++) {
         if (str1.charAt(i) != str2.charAt(i)) {
            hamDist++;
         }
      }
      return hamDist;
   }

   /**
    * Returns a minimum-length word ladder from start to end. If multiple
    *  minimum-length word ladders exist, no guarantee is made regarding which
    * one is returned. If no word ladder exists, this method returns an empty
    * list.
    *
    * Breadth-first search must be used in all implementing classes.
    *
    * @param start The starting word
    * @param end The ending word
    * @return a minimum length word ladder from start to end
    */
   public List<String> getMinLadder(String start, String end) {
      if (start == null || end == null) {
         return EMPTY_LADDER;
      }
      if (getHammingDistance(start, end) == -1) {
         return EMPTY_LADDER;
      }
      if (!isWord(start) || !isWord(end)) {
         return EMPTY_LADDER;
      }
   
      String ladStart = start.toLowerCase();
      String ladEnd = end.toLowerCase();
      List<String> ladder = new ArrayList<String>();
   
      if (ladStart.equals(ladEnd)) {
         ladder.add(ladStart);
         return ladder;
      }
      ArrayDeque<Node> queue = new ArrayDeque<Node>();
      HashSet<String> visited = new HashSet<String>();
      
      Node startNode = new Node(ladStart); // Origin vertex
      visited.add(startNode.word);
      queue.add(startNode);
         
      while (!queue.isEmpty()) {
         Node current = queue.removeFirst();
         List<String> neighbors = current.neighbors;
         for (String neighbor : neighbors) {   // Creates ladder web
            if (!visited.contains(neighbor)) { //   with incriminting distance
               Node neighborNode = new Node(neighbor, current); // Creates edge
               visited.add(neighbor);
               queue.addLast(neighborNode);
               if (neighbor.equals(ladEnd)) { // Once end is reached return path
                  return toList(new Node(neighbor, current));
               }
            }
         }
      }
      return EMPTY_LADDER;
   }

   /**
    * Returns all the words that have a Hamming distance of one relative to the
    * given word.
    *
    * @param word The given word
    * @return the neighbors of the given word
    */
   public List<String> getNeighbors(String word) {
      List<String> neighbors = new ArrayList<String>();
      if (!isWord(word)) {
         return neighbors;
      }
      Iterator<String> itr = lexicon.iterator();
      String testWord;
      while (itr.hasNext()) {
         testWord = itr.next();
         if (getHammingDistance(word, testWord) == 1) {
            neighbors.add(testWord);
         }
      }
      return neighbors;
   }

   /**
    * Returns the total number of words in the current lexicon.
    *
    * @return number of words in the lexicon
    */
   public int getWordCount() {
      return lexicon.size();
   }

   /**
    * Checks to see if the given string is a word.
    *
    * @param str The string to check
    * @return true if str is a word, false otherwise
    */
   public boolean isWord(String str) {
      str = str.toLowerCase();
      return lexicon.contains(str);
   }

   /**
    * Checks to see if the given sequence of strings is a valid word ladder.
    *
    * @param sequence The given sequence of strings
    * @return true if the given sequence is a valid word ladder,
    *         false otherwise
    */
   public boolean isWordLadder(List<String> sequence) {
      if (sequence == null || sequence.isEmpty()) {
         return false;
      }
      if (!isWord(sequence.get(0).toLowerCase())) {
         return false;
      }
      for (int i = 1; i < sequence.size(); i++) {
         String s1 = sequence.get(i).toLowerCase();
         String s2 = sequence.get(i - 1).toLowerCase();
         if (!isWord(s1)) {
            return false;
         }
         if (getHammingDistance(s1, s2) != 1) {
            return false;
         }
      }
      return true;
   }

   /**
    * Defines a private node class to create a graph.
    */
   private class Node {
      private String word;
      private Node next;
      private List<String> neighbors;
   
      /**
       * Node constructor.
       * 
       * @param w Word in
       */
      Node(String w) {
         this.word = w;
         this.neighbors = getNeighbors(this.word);
      }
   
      /**
       * Node constructor.
       * 
       * @param w Word in
       * @param n Next node
       */
      Node(String w, Node n) {
         this.word = w;
         this.next = n;
         this.neighbors = getNeighbors(this.word);
      }
   }

   /**
    * Takes a ladder path and produces a list.
    *
    * @param n Ending node of the ladder
    * @return ladder as list
    */
   private List<String> toList(Node n) {
      List<String> list = new ArrayList<String>();
      String word = n.word;
      Node prev = n.next;
      list.add(word);
      while (prev != null) {
         word = prev.word;
         list.add(0, word);
         prev = prev.next;
      }
      return list;
   }
}