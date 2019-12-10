import java.io.File;
import java.util.HashMap;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;

/**
 * MarkovModel.java Creates an order K Markov model of the supplied source
 * text. The value of K determines the size of the "kgrams" used to generate
 * the model. A kgram is a sequence of k consecutive characters in the source
 * text.
 *
 * @author     Will Humphlett (wah0028@auburn.edu)
 * @author     Dean Hendrix (dh@auburn.edu)
 * @version    12-8-2019
 *
 */
public class MarkovModel {
   private final HashMap<String, String> model;
   private String first;
   private ArrayList<String> keyList;
   private final Random rng = new Random();

   /**
    * Reads the contents of the file sourceText into a string, then calls
    * buildModel to construct the order K model.
    *
    * DO NOT CHANGE THIS CONSTRUCTOR.
    *
    * @param k Key length
    * @param sourceText File containing the source text
    */
   public MarkovModel(final int k, final File sourceText) {
      model = new HashMap<>();
      try {
         final String text = new Scanner(sourceText).useDelimiter("\\Z").next();
         buildModel(k, text);
      } catch (final IOException e) {
         System.out.println("Error loading source text: " + e);
      }
   }

   /**
    * Calls buildModel to construct the order k model of the string sourceText.
    *
    * DO NOT CHANGE THIS CONSTRUCTOR.
    *
    * @param k Key length
    * @param sourceText String containing the source text
    */
   public MarkovModel(final int k, final String sourceText) {
      model = new HashMap<>();
      buildModel(k, sourceText);
   }

   /**
    * Builds an order k Markov model of the string sourceText.
    *
    * @param k Key length
    * @param sourceText String containing the source text
    */
   private void buildModel(final int k, final String sourceText) {
      first = sourceText.substring(0, k);
      int start = 0;
      while (start + k <= sourceText.length()) {
         final String key = sourceText.substring(start, start + k);
         if (!model.containsKey(key)) {
            if (start + k == sourceText.length()) {
            model.put(key, Character.toString('\u0000'));
            }
            else {
            model.put(key, Character.toString(sourceText.charAt(start + k)));
            }
         } 
         else {
            String map = model.get(key);
            if (start + k == sourceText.length()) {
               map += Character.toString('\u0000');
            }
            else {
            map += sourceText.charAt(start + k);
            }
            model.replace(key, map);
         }
         start++;
      }
   }

   /**
    * Returns the first kgram found in the source text.
    *
    * @return first kgram
    */
   public String getFirstKgram() {
      return first;
   }

   /**
    * Returns a kgram chosen at random from the source text.
    * 
    * @return random kgram
    */
   public String getRandomKgram() {
      if (keyList == null || keyList.size() != model.size()) {
         keyList = new ArrayList<String>(getAllKgrams());
      }
      return keyList.get(rng.nextInt(keyList.size()));
   }

   /**
    * Returns the set of kgrams in the source text.
    *
    * DO NOT CHANGE THIS METHOD.
    *
    * @return all kgrams as set
    */
   public Set<String> getAllKgrams() {
      return model.keySet();
   }

   /**
    * Returns a single character that follows the given kgram in the source
    * text. This method selects the character according to the probability
    * distr of all characters that follow the given kgram in the source text.
    *
    * @param kgram The key as a string
    * @return char given a key
    */
   public char getNextChar(final String kgram) {
      if (model.containsKey(kgram)) {
         final String value = model.get(kgram);
         return value.charAt(rng.nextInt(value.length()));
      }
      else {
         return '\u0000';
      }
   }

   /**
    * Returns a string representation of the model.
    * This is not part of the provided shell for the assignment.
    *
    * DO NOT CHANGE THIS METHOD.
    *
    * @return String toString
    */
   @Override
    public String toString() {
      return model.toString();
   }
}