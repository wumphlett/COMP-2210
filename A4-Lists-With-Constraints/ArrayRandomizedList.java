import java.util.Iterator;
import java.util.Random;
import java.util.NoSuchElementException;
/**
 * Array based data structure designed to provide the following
 * methods with their respective time complexities.
 * add          |  O(1)
 * remove       |  O(1)
 * sample       |  O(1)
 * size         |  O(1)
 * isEmpty      |  O(1)
 * iterator     |  O(N)
 *
 * @author Will Humphlett (wah0028@auburn.edu)
 * @version 10-17-2019
 *
 * @param <T> generic
 */
public class ArrayRandomizedList<T> implements RandomizedList<T> {
   private static final int DEFAULT_CAPACITY = 1;
   private T[] elements;
   private int size;
   
   /**
    * Generic constructor, utilizes default capacity
    * when capacity is not specified.
    */
   public ArrayRandomizedList() {
      this(DEFAULT_CAPACITY);
   }
   
   /**
    * ArrayRandomizedList constructor.
    * @param capacity - list capacity
    */
   @SuppressWarnings("unchecked")
   public ArrayRandomizedList(int capacity) {
      elements = (T[]) new Object[capacity];
      size = 0;
   }
   
   /**
    * Adds element to ARL.
    * @param element - element to be added
    */
   public void add(T element) {
      if (element == null) {
         throw new IllegalArgumentException("Element cannot be null");
      }
      if (isFull()) {
         resize(size * 2);
      }
      elements[size++] = element;
   }
   
   /**
    * Removes random element and maintains left justification.
    * @return remElement - removed element
    */
   public T remove() {
      if (isEmpty()) {
         return null;
      }
      else {
         Random rng = new Random();
         int remIndex = rng.nextInt(size);
         T remElement = elements[remIndex];
         elements[remIndex] = null;
         elements[remIndex] = elements[--size];
         return remElement;
      }
   }
   
   /**
    * Samples random element from the list.
    * @return samElement - sampled element
    */
   public T sample() {
      if (isEmpty()) {
         return null;
      }
      else {
         Random rng = new Random();
         int samIndex = rng.nextInt(size);
         return elements[samIndex];
      }
   }
   
   /**
    * Returns list size.
    * @return size - size
    */
   public int size() {
      return size;
   }
   
   /**
    * States if list is empty.
    * @return isEmpty - if empty
    */
   public boolean isEmpty() {
      return size == 0;
   }
   
   /**
    * States if list is full.
    * @return isFull - if full
    */
   private boolean isFull() {
      return size == elements.length;
   }
   
   /**
    * Resizes ARL and adds memory if full.
    */
   @SuppressWarnings("unchecked")
   private void resize(int newSize) {
      assert newSize > 0;
      T[] auxArray = (T[]) new Object[newSize];
      for (int i = 0; i < size; i++) {
         auxArray[i] = elements[i];
      }
      elements = auxArray;
   }
   
   /**
    * Creates new ARLIterator.
    * @return ARLIterator - new iterator
    */
   public ARLIterator iterator() {
      return new ARLIterator(elements, size);
   }
   
   /**
    * Defines the iterator for ARLs.
    */
   private class ARLIterator implements Iterator<T> {
      private T[] elem;
      private int count;
      private int current;
      
      /**
       * ARLIterator constructor.
       * @param elements - list to be iterated over
       * @param size - size of the list
       */
      @SuppressWarnings("unchecked")
      ARLIterator(T[] elementsIn, int sizeIn) {
         count = sizeIn;
         current = 0;
         elem = (T[]) new Object[size];
         for (int i = 0; i < size; i++) {
            elem[i] = elementsIn[i];
         }
         this.shuffle();
      }
      
      /**
       * States if the iterator has another element to
       * iterate over.
       * @return hasNext - if has next
       */
      public boolean hasNext() {
         return current < count;
      }
      
      /**
       * Returns next element of the list.
       * @return elem - next element
       */
      public T next() {
         if (!hasNext()) {
            throw new NoSuchElementException("element does not exist");
         }
         return elem[current++];
      }
      
      /**
       * Remove, unsupported operation.
       */
      public void remove() {
         throw new UnsupportedOperationException();
      }
      
      /**
       * Shuffles the list to be iterated over.
       */
      public void shuffle() {
         Random rng = new Random();
         for (int i = 0; i < count; i++) {
            int random = rng.nextInt(count);
            T aux = elem[i];
            elem[i] = elem[random];
            elem[random] = aux;
         }
      }
   }
}