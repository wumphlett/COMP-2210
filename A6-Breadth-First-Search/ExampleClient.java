import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * ExampleClient.java
 * Provides example calls to WordLadderGame methods in an instance of
 * the Doublets class.
 *
 * @author Dean Hendrix (dh@auburn.edu)
 * @author Will Humphlett (wah0028@auburn.edu)
 * @version 2019-03-29
 */
public class ExampleClient {

   /** Drives execution. */
   public static void main(String[] args) throws FileNotFoundException {
      WordLadderGame doublets = new Doublets(new FileInputStream(new File("WordLists/sowpods.txt")));
   
      System.out.println(doublets.getHammingDistance("tiger", "tiger"));
      System.out.println(doublets.getHammingDistance("tiger", "eagle"));
      System.out.println(doublets.getHammingDistance("war", "eagle"));
      System.out.println(doublets.getHammingDistance("barner", "bammer"));
   
      System.out.println(doublets.isWord("tiger"));
      System.out.println(doublets.isWord("eagle"));
      System.out.println(doublets.isWord("aubie"));
   
      System.out.println(doublets.getWordCount());
   
      System.out.println(doublets.isWordLadder(Arrays.asList("cat", "cot", "zot", "dot")));
      System.out.println(doublets.isWordLadder(Arrays.asList("cat", "cot", "pot", "dot")));
   
      System.out.println(doublets.getNeighbors("tiger"));
   
      System.out.println(doublets.getMinLadder("cat", "hat"));
      System.out.println(doublets.getMinLadder("last", "mass"));
      System.out.println(doublets.getMinLadder("tiger", "polis"));
   }
}

/**

RUNTIME OUTPUT

0
4
-1
2
true
true
false
267751
false
true
[liger, niger, tiler, timer, titer, tiges]
[cat, hat]
[last, lass, mass]
[tiger, tiges, tiles, piles, pilis, polis]

*/

