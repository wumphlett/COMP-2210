
/**
 * Provides a factory method for creating word search games. 
 *
 * @author Will Humphlett (wah0028@auburn.edu)
 * @author Dean Hendrix (dh@auburn.edu)
 * @version 11-1-2019
 */
public class WordSearchGameFactory {

   /**
    * Returns an instance of a class that implements the WordSearchGame
    * interface.
    */
   public static WordSearchGame createGame() {
      return new HumphlettWordSearchTrie();
   }

}
