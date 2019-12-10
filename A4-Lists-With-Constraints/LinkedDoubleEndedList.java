import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * Linked node based data structure designed to provide the following
 * methods with their respective time complexities.
 * addFirst     |  O(1)
 * addLast      |  O(1)
 * removeFirst  |  O(1)
 * removeLast   |  O(1)
 * size         |  O(1)
 * isEmpty      |  O(1)
 * iterator     |  O(1)
 *
 * @author Will Humphlett (wah0028@auburn.edu)
 * @version 10-17-2019
 *
 * @param <T> generic
 */
public class LinkedDoubleEndedList<T> implements DoubleEndedList<T> {
   private Node first;
   private Node last;
   private int size;
   
   /**
    * LinkedDoubleEndedList constructor.
    */
   public LinkedDoubleEndedList() {
      size = 0;
   }
   
   /**
    * Adds element to the start of the list.
    * @param element - new first element
    */
   public void addFirst(T element) {
      if (element == null) {
         throw new IllegalArgumentException("element must not be null");
      }
      Node aux = new Node(element);
      if (isEmpty()) {
         first = aux;
         last = aux;
      }
      else {
         first.prev = aux;
         aux.next = first;
         first = aux;
      }
      size++;
   }
   
   /**
    * Adds element to the end of the list.
    * @param element - new last element
    */
   public void addLast(T element) {
      if (element == null) {
         throw new IllegalArgumentException("element must not be null");
      }
      Node aux = new Node(element);
      if (isEmpty()) {
         first = aux;
         last = aux;
      }
      else {
         last.next = aux;
         aux.prev = last;
         last = aux;
      }
      size++;
   }
   
   /**
    * Removes first element from the list.
    * @return aux - removed element
    */
   public T removeFirst() {
      if (isEmpty()) {
         return null;
      }
      T aux = first.element;
      if (size == 1) {
         first = null;
         last = null;
      }
      else {
         first = first.next;
         first.prev = null;
      }
      size--;
      return aux;
   }
   
   /**
    * Removes last element from the list.
    * @return aux - removed element
    */
   public T removeLast() {
      if (isEmpty()) {
         return null;
      }
      T aux = last.element;
      if (size == 1) {
         first = null;
         last = null;
      }
      else {
         last = last.prev;
         last.next = null;
      }
      size--;
      return aux;
   }
   
   /**
    * Returns list size.
    * @return size
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
    * Creates new LDELIterator.
    * @return LDELIterator - new iterator
    */
   public Iterator<T> iterator() {
      return new LDELIterator();
   }
   
   /**
    * Defines node of linked list.
    */
   private class Node {
      private T element;
      private Node prev = null;
      private Node next = null;
      
      /**
       * Node constructor.
       */
      Node(T elementIn) {
         element = elementIn;
      }
   }
   
   /**
    * Defines the iterator for LDELs.
    */
   private class LDELIterator implements Iterator<T> {
      private Node current = first;
      
      /**
       * States if the iterator has another element to
       * iterate over.
       * @return hasNext - if has next
       */
      public boolean hasNext() {
         return current != null;
      }
      
      /**
       * Returns next element of the list.
       * @return elem - next element
       */
      public T next() {
         if (!hasNext()) {
            throw new NoSuchElementException("element does not exist");
         }
         T aux = current.element;
         current = current.next;
         return aux;
      }
      
      /**
       * Remove, unsupported operation.
       */
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}