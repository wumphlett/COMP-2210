import java.util.List;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Scanner;
import java.util.Iterator;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Defines and provides methods for a square word search game.
 * 
 * @author Will Humphlett (wah0028@auburn.edu)
 * @version 11-1-2019
 */
public class HumphlettWordSearchTrie implements WordSearchGame {
   private Trie lexicon;
   private String[][] board;
   private int size;

   private SortedSet<String> allWords;
   
   /**
    * GameDefine constructor.
    * Sets the default game variables.
    */
   public HumphlettWordSearchTrie() {
      lexicon = null;
      size = 4;
      board = new String[size][size];
      board[0][0] = "E"; board[0][1] = "E"; board[0][2] = "C"; board[0][3] = "A"; 
      board[1][0] = "A"; board[1][1] = "L"; board[1][2] = "E"; board[1][3] = "P"; 
      board[2][0] = "H"; board[2][1] = "N"; board[2][2] = "B"; board[2][3] = "O"; 
      board[3][0] = "Q"; board[3][1] = "T"; board[3][2] = "T"; board[3][3] = "Y";
   }
   
   /**
    * Loads the lexicon into a data structure for later use. 
    * 
    * @param fileName A string containing the name of the file to be opened.
    * @throws IllegalArgumentException if fileName is null
    * @throws IllegalArgumentException if fileName cannot be opened.
    */
   public void loadLexicon(String fileName) {
      if (fileName == null) {
         throw new IllegalArgumentException();
      }
      try {
         lexicon = new Trie();
         Scanner file = new Scanner(new File(fileName));
         while (file.hasNextLine()) {
            lexicon.insert(file.next().toUpperCase());
            if (!file.hasNextLine()) {
               break;
            }
            file.nextLine();
         }
         file.close();
      }
      catch (FileNotFoundException e) {
         throw new IllegalArgumentException();
      }
   }
   
   /**
    * Stores the incoming array of Strings in a data structure that will make
    * it convenient to find words.
    * 
    * @param letterArray This array of length N^2 stores the contents of the
    *     game board in row-major order. Thus, index 0 stores the contents of
    *     board position (0,0) and index length-1 stores the contents of board
    *     position (N-1,N-1). Note that the board must be square and that the
    *     strings inside may be longer than one character
    * @throws IllegalArgumentException if letterArray is null, or is  not
    *     square
    */
   public void setBoard(String[] letterArray) {
      if (letterArray == null) {
         throw new IllegalArgumentException();
      }
      double n = java.lang.Math.sqrt((double) letterArray.length);
      if (n % 1 > .00001) {
         throw new IllegalArgumentException("array must be square");
      }
      board = new String[(int) n][(int) n];
      int i = 0;
      for (int j = 0; j < n; j++) {
         for (int k = 0; k < n; k++) {
            board[j][k] = letterArray[i++];
         }
      }
      size = (int) n;
   }
    
   /**
    * Creates a String representation of the board, suitable for printing to
    * standard out. Note that this method can always be called since
    * implementing classes should have a default board.
    *
    * @return board as string
    */
   public String getBoard() {
      String strBoard = "";
      for (int i = 0; i < size; i++) {
         strBoard += "\n| ";
         for (int j = 0; j < size; j++) {
            strBoard += board[i][j] + " ";
         }
         strBoard += "|";
      }
      return strBoard;
   }
    
   /**
    * Retrieves all valid words on the game board, according to the stated game
    * rules.
    * 
    * @param minimumWordLength The minimum allowed length (i.e., number of
    *     characters) for any word found on the board.
    * @return java.util.SortedSet which contains all the words of minimum length
    *     found on the game board and in the lexicon.
    * @throws IllegalArgumentException if minimumWordLength < 1
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   public SortedSet<String> getAllValidWords(int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      allWords = new TreeSet<String>();
      LinkedList<Integer> wordProg = new LinkedList<Integer>();
      for (int i = 0; i < (size * size); i++) {
         wordProg.add(i);
         if (isValidWord(toWord(wordProg))
            && toWord(wordProg).length() >= minimumWordLength) {
            allWords.add(toWord(wordProg));
         }
         if (isValidPrefix(toWord(wordProg))) {
            allValidWordSearch(wordProg, minimumWordLength);
         }
         wordProg.clear();
      }
      return allWords;
   }

   /**
    * Recursive dfs search method with backtracking.
    *
    * @param wordProg The word being built and checked against the lexicon
    * @param min Minimum word length
    * @return The updated word being built
    */
   private LinkedList<Integer> allValidWordSearch(LinkedList<Integer> wordProg,
      int min) {
      Position[] adjArray = new Position(wordProg.getLast()).adjacent(wordProg);
      for (Position p : adjArray) {
         if (p == null) {
            break;
         }
         wordProg.add(p.getIndex());
         if (isValidPrefix(toWord(wordProg))) {
            if (isValidWord(toWord(wordProg))
               && toWord(wordProg).length() >= min) {
               allWords.add(toWord(wordProg));
            }
            allValidWordSearch(wordProg, min);
         }
         else {
            wordProg.removeLast();
         }
      }
      wordProg.removeLast();
      return wordProg;
   }
    
   /**
    * Computes the cumulative score for the scorable words in the given set.
    * To be scorable, a word must (1) have at least the minimum number of
    * characters, (2) be in the lexicon, and (3) be on the board. Each
    * scorable word is awarded one point for the minimum number of characters, 
    * and one point for each character beyond the minimum number.
    *
    * @param words The set of words that are to be scored
    * @param minimumWordLength The minimum number of characters required per
    *    word
    * @return the cumulative score of all scorable words in the set
    * @throws IllegalArgumentException if minimumWordLength < 1
    * @throws IllegalStateException if loadLexicon has not been called
    */  
   public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      int score = 0;
      Iterator<String> itr = words.iterator();
      while (itr.hasNext()) {
         String word = itr.next();
         if (word.length() >= minimumWordLength && isValidWord(word)
            && !isOnBoard(word).isEmpty()) {
            score += (word.length() - minimumWordLength) + 1;
         }
      }
      return score;
   }
    
   /**
    * Determines if the given word is in the lexicon.
    * 
    * @param wordToCheck The word to validate
    * @return true if wordToCheck appears in lexicon, false otherwise.
    * @throws IllegalArgumentException if wordToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   public boolean isValidWord(String wordToCheck) {
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      return lexicon.search(wordToCheck);
   }
    
   /**
    * Determines if there is at least one word in the lexicon with the 
    * given prefix.
    * 
    * @param prefixToCheck The prefix to validate
    * @return true if prefixToCheck appears in lexicon, false otherwise.
    * @throws IllegalArgumentException if prefixToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   public boolean isValidPrefix(String prefixToCheck) {
      if (prefixToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      return lexicon.startsWith(prefixToCheck);
   }
       
   /**
    * Determines if the given word is in on the game board. If so, it returns
    * the path that makes up the word.
    * @param wordToCheck The word to validate
    * @return java.util.List containing java.lang.Integer objects with  the path
    *     that makes up the word on the game board. If word is not on the game
    *     board, return an empty list. Positions on the board are numbered from
    *     zero top to bottom, left to right (i.e., in row-major order). Thus, on
    *     an NxN board, the upper left position is numbered 0 and the lower
    *     right position is numbered N^2 - 1.
    *
    * @throws IllegalArgumentException if wordToCheck is null.
    * @throws IllegalStateException if loadLexicon has not been called.
    */
   public List<Integer> isOnBoard(String wordToCheck) {
      if (wordToCheck == null) {
         throw new IllegalArgumentException();
      }
      if (lexicon == null) {
         throw new IllegalStateException();
      }
      LinkedList<Integer> auxList = new LinkedList<Integer>();
      List<Integer> wordPath = boardSearch(wordToCheck, auxList, 0);
      return wordPath;
   }

   /**
    * Recursive dfs search method with backtracking.
    *
    * @param wordToCheck Word to check if found in the board
    * @param listProg Integer list representing the positions of the word in
    * @param posIndex Index of the last letter in the word
    * @return the updated word being built
    */
   private LinkedList<Integer> boardSearch(String wordToCheck,
      LinkedList<Integer> listProg, int posIndex) {
      if (listProg.size() > 0 && !wordToCheck.equals(toWord(listProg))) {
         Position[] adjArray = 
            new Position(posIndex).adjacent(listProg);
         for (Position p : adjArray) {
            if (p == null) {
               break;
            }
            listProg.add(p.getIndex());
            if (wordToCheck.equals(toWord(listProg))) {
               break;
            }
            if (wordToCheck.startsWith(toWord(listProg))) {
               boardSearch(wordToCheck, listProg, p.getIndex());
            }
            else {
               listProg.removeLast();
            }
         }
      }
      if (listProg.size() == 0) {
         while (posIndex < (size * size)) {
            if (wordToCheck.startsWith(new Position(posIndex).getLetter())) {
               listProg.add(posIndex);
               boardSearch(wordToCheck, listProg, posIndex);
            }
            posIndex++;
         }
         return listProg;
      }
      if (toWord(listProg).equals(wordToCheck)) {
         return listProg;
      }
      else {
         listProg.removeLast();
         return listProg;
      }
   }

   /**
    * Takes an integer list and creates its corresponding word.
    *
    * @param listIn Integer list representing word
    * @return word in string form
    */
   public String toWord(LinkedList<Integer> listIn) {
      String word = "";
      for (int i : listIn) {
         word += new Position(i).getLetter();
      }
      return word;
   }
   
   /**
    * Node class acting as a position on the gameboard.
    */
   private class Position {
      private int x;
      private int y;
      private int index;
      private String letter;
      private static final int MAX_ADJ = 8;
   
      /**
       * Position constructor.
       * 
       * @param index The index of the position
       */
      Position(int indexIn) {
         this.index = indexIn;
         if (index == 0) {
            this.x = 0;
            this.y = 0;
         }
         else {
            this.x = index % size;
            this.y = index / size;
         }
         this.letter = board[y][x];
      }
      
      /**
       * Position constructor.
       * 
       * @param x The x coordinate of the position
       * @param y The y coordinate of the position
       */
      Position(int xIn, int yIn) {
         this.x = xIn;
         this.y = yIn;
         this.index = (y * size) + x;
         this.letter = board[y][x];
      }
   
      /**
       * Finds all valid adjacent positions on the gameboard.
       * 
       * @param visited Integer list of the current path
       * @return possible adjacent positions
       */
      public Position[] adjacent(LinkedList<Integer> visited) {
         Position[] adj = new Position[MAX_ADJ];
         int k = 0;
         for (int i = this.x - 1; i <= this.x + 1; i++) {
            for (int j = this.y - 1; j <= this.y + 1; j++) {
               if (!(i == this.x && j == this.y)) {
                  if (isValid(i, j) && !visited.contains((j * size) + i)) {
                     Position aux = new Position(i, j);
                     adj[k++] = aux;
                  }
               }
            }
         }
         return adj;
      }
   
      /**
       * Determines if position is within the gameboard.
       * 
       * @param x X coord of position
       * @param y Y coord of position
       * @return if the position is valid
       */
      public boolean isValid(int xTest, int yTest) {
         return xTest >= 0 && xTest < size && yTest >= 0 && yTest < size;
      }
   
      /**
       * Get position letter.
       * 
       * @return letter
       */
      public String getLetter() {
         return letter;
      }
   
      /**
       * Get position index.
       * 
       * @return index
       */
      public int getIndex() {
         return index;
      }
   }
   
   /**
    * Defines TrieTree to store lexicon.
    */
   private class Trie {
      private TrieNode root;
   
      /**
       * Trie constructor.
       */
      Trie() {
         root = new TrieNode();
      }
   
      /**
       * Inserts word into Trie.
       * 
       * @param word String to be passed in
       */
      public void insert(String word) {
         TrieNode p = root;
         for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'A';
            if (index == -20) {
               index = 26;
            }
            if (p.children[index] == null) {
               TrieNode aux = new TrieNode();
               p.children[index] = aux;
               p = aux;
            }
            else {
               p = p.children[index];
            }
         }
         p.isWord = true;
      }
   
      /**
       * Searches Trie for word.
       * 
       * @param word String to be searched for
       */
      public boolean search(String word) {
         TrieNode node = searchNode(word);
         if (node == null) {
            return false;
         }
         else {
            if (node.isWord) {
               return true;
            }
         }
         return false;
      }
   
      /**
       * Determines if Trie contains a given prefix.
       * 
       * @param prefix String to be searched for
       */
      public boolean startsWith(String prefix) {
         TrieNode node = searchNode(prefix);
         return node != null;
      }
   
      /**
       * Searches node for consecutive letters in the string.
       * 
       * @param s String representing word
       * @return ending node of word
       */
      public TrieNode searchNode(String word) {
         TrieNode node = root;
         for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'A';
            // Non alphabetical character clauses
            if (index == -20) { // -
               index = 26;
            }
            if (index == 55) { // unknown character
               continue;
            }
            if (node.children[index] != null) {
               node = node.children[index];
            }
            else {
               return null;
            }
         }
         if (node == root) {
            return null;
         }
         return node;
      }
      
      /**
       * Defines Trie Node.
       */
      public class TrieNode {
         private TrieNode[] children;
         private boolean isWord;
         private static final int ALPHABET = 27;
      
         /**
          * TrieNode constructor.
          */
         TrieNode() {
            this.children = new TrieNode[ALPHABET];
         }
      }
   }
}