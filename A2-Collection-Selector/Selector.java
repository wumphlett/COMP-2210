import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Defines a library of selection methods on Collections.
 *
 * @author  Will Humphlett (wah0028@auburn.edu)
 * @author  Dean Hendrix (dh@auburn.edu)
 * @version 9/19/2019
 *
 */
public final class Selector {

/**
 * Can't instantiate this class.
 *
 * D O   N O T   C H A N G E   T H I S   C O N S T R U C T O R
 *
 */
   private Selector() { }


   /**
    * Returns the minimum value in the Collection coll as defined by the
    * Comparator comp. If either coll or comp is null, this method throws an
    * IllegalArgumentException. If coll is empty, this method throws a
    * NoSuchElementException. This method will not change coll in any way.
    *
    * @param coll    the Collection from which the minimum is selected
    * @param comp    the Comparator that defines the total order on T
    * @return        the minimum value in coll
    * @throws        IllegalArgumentException as per above
    * @throws        NoSuchElementException as per above
    */
   public static <T> T min(Collection<T> coll, Comparator<T> comp) {
      if (coll == null || comp == null) {
         throw new IllegalArgumentException("Arguments must not be null");
      }
      if (coll.isEmpty()) {
         throw new NoSuchElementException("Collection must not be empty");
      }
      Iterator<T> it = coll.iterator();
      T min = it.next();
      while (it.hasNext()) {
         T aux = it.next();
         if (comp.compare(aux, min) < 0) {
            min = aux;
         }
      }
      return min;
   }


   /**
    * Selects the maximum value in the Collection coll as defined by the
    * Comparator comp. If either coll or comp is null, this method throws an
    * IllegalArgumentException. If coll is empty, this method throws a
    * NoSuchElementException. This method will not change coll in any way.
    *
    * @param coll    the Collection from which the maximum is selected
    * @param comp    the Comparator that defines the total order on T
    * @return        the maximum value in coll
    * @throws        IllegalArgumentException as per above
    * @throws        NoSuchElementException as per above
    */
   public static <T> T max(Collection<T> coll, Comparator<T> comp) {
      if (coll == null || comp == null) {
         throw new IllegalArgumentException("Arguments must not be null");
      }
      if (coll.isEmpty()) {
         throw new NoSuchElementException("Collection must not be empty");
      }
      Iterator<T> it = coll.iterator();
      T max = it.next();
      while (it.hasNext()) {
         T aux = it.next();
         if (comp.compare(aux, max) > 0) {
            max = aux;
         }
      }
      return max;
   }


   /**
    * Selects the kth minimum value from the Collection coll as defined by the
    * Comparator comp. If either coll or comp is null, this method throws an
    * IllegalArgumentException. If coll is empty or if there is no kth minimum
    * value, this method throws a NoSuchElementException. This method will not
    * change coll in any way.
    *
    * @param coll    the Collection from which the kth minimum is selected
    * @param k       the k-selection value
    * @param comp    the Comparator that defines the total order on T
    * @return        the kth minimum value in coll
    * @throws        IllegalArgumentException as per above
    * @throws        NoSuchElementException as per above
    */
   public static <T> T kmin(Collection<T> coll, int k, Comparator<T> comp) {
      if (coll == null || comp == null) {
         throw new IllegalArgumentException("Arguments must not be null");
      }
      if (coll.isEmpty()) {
         throw new NoSuchElementException("Collection must not be empty");
      }
      if (k < 1 || k > coll.size()) {
         throw new NoSuchElementException("Illegal k value");
      }
      // Copy collection for sorting
      ArrayList<T> list = new ArrayList<T>();
      Iterator<T> it = coll.iterator();
      while (it.hasNext()) {
         list.add(it.next());
      }
      java.util.Collections.sort(list, comp);
      // Returns first element if k = 1
      if (k == 1) {
         return list.get(0);
      }
      // Counts minimums bar duplicate elements
      T aux = list.get(0);
      T kmin = null;
      int tracker = 1;
      for (int i = 1; i < list.size(); i++) {
         if (!list.get(i).equals(aux)) {
            tracker++;
            if (k == tracker) {
               kmin = list.get(i);
            }
         }
         aux = list.get(i);
      }
      // Too many duplicates test
      if (tracker < k) {
         throw new NoSuchElementException(k + "th min does not exist.");
      }
      return kmin;
   }


   /**
    * Selects the kth maximum value from the Collection coll as defined by the
    * Comparator comp. If either coll or comp is null, this method throws an
    * IllegalArgumentException. If coll is empty or if there is no kth maximum
    * value, this method throws a NoSuchElementException. This method will not
    * change coll in any way.
    *
    * @param coll    the Collection from which the kth maximum is selected
    * @param k       the k-selection value
    * @param comp    the Comparator that defines the total order on T
    * @return        the kth maximum value in coll
    * @throws        IllegalArgumentException as per above
    * @throws        NoSuchElementException as per above
    */
   public static <T> T kmax(Collection<T> coll, int k, Comparator<T> comp) {
      if (coll == null || comp == null) {
         throw new IllegalArgumentException("Arguments must not be null");
      }
      if (coll.isEmpty()) {
         throw new NoSuchElementException("Collection must not be empty");
      }
      if (k < 1 || k > coll.size()) {
         throw new NoSuchElementException("Illegal k value");
      }
      // Copy collection for sorting
      ArrayList<T> list = new ArrayList<T>();
      Iterator<T> it = coll.iterator();
      while (it.hasNext()) {
         list.add(it.next());
      }
      java.util.Collections.sort(list, comp);
      // Returns last element if k = 1
      if (k == 1) {
         return list.get(list.size() - 1);
      }
      // Counts maximums bar duplicate elements
      T aux = list.get(list.size() - 1);
      T kmax = null;
      int tracker = 1;
      for (int i = list.size() - 2; i >= 0; i--) {
         if (!list.get(i).equals(aux)) {
            tracker++;
            if (k == tracker) {
               kmax = list.get(i);
            }
         }
         aux = list.get(i);
      }
      // Too many duplicates test
      if (tracker < k) {
         throw new NoSuchElementException(k + "th min does not exist.");
      }
      return kmax;
   }


   /**
    * Returns a new Collection containing all the values in the Collection coll
    * that are greater than or equal to low and less than or equal to high, as
    * defined by the Comparator comp. The returned collection must contain only
    * these values and no others. The values low and high themselves do not have
    * to be in coll. Any duplicate values that are in coll must also be in the
    * returned Collection. If no values in coll fall into the specified range or
    * if coll is empty, this method throws a NoSuchElementException. If either
    * coll or comp is null, this method throws an IllegalArgumentException. This
    * method will not change coll in any way.
    *
    * @param coll    the Collection from which the range values are selected
    * @param low     the lower bound of the range
    * @param high    the upper bound of the range
    * @param comp    the Comparator that defines the total order on T
    * @return        a Collection of values between low and high
    * @throws        IllegalArgumentException as per above
    * @throws        NoSuchElementException as per above
    */
   public static <T> Collection<T> range(Collection<T> coll, T low, T high,
                                         Comparator<T> comp) {
      if (coll == null || comp == null) {
         throw new IllegalArgumentException("Arguments must not be null");
      }
      if (coll.isEmpty()) {
         throw new NoSuchElementException("Collection must not be empty");
      }
      ArrayList<T> range = new ArrayList<T>();
      Iterator<T> it = coll.iterator();
      while (it.hasNext()) {
         T aux = it.next();
         if (comp.compare(aux, low) >= 0 && comp.compare(aux,high) <= 0) {
            range.add(aux);
         }
      }
      if (range.isEmpty()) {
         throw new NoSuchElementException("No such values fit arguments");
      }
      return range;
   }


   /**
    * Returns the smallest value in the Collection coll that is greater than
    * or equal to key, as defined by the Comparator comp. The value of key
    * does not have to be in coll. If coll or comp is null, this method throws
    * an IllegalArgumentException. If coll is empty or if there is no
    * qualifying value, this method throws a NoSuchElementException. This
    * method will not change coll in any way.
    *
    * @param coll    the Collection from which the ceiling value is selected
    * @param key     the reference value
    * @param comp    the Comparator that defines the total order on T
    * @return        the ceiling value of key in coll
    * @throws        IllegalArgumentException as per above
    * @throws        NoSuchElementException as per above
    */
   public static <T> T ceiling(Collection<T> coll, T key, Comparator<T> comp) {
      if (coll == null || comp == null) {
         throw new IllegalArgumentException("Arguments must not be null");
      }
      if (coll.isEmpty()) {
         throw new NoSuchElementException("Collection must not be empty");
      }
      T ceiling = null;
      boolean stat = false; // Tracks if a ceiling exists
      Iterator<T> it = coll.iterator();
      while (it.hasNext()) {
         T aux = it.next();
         // If a ceiling has not been found, compare to key
         if(!stat && comp.compare(aux, key) >= 0) {
            ceiling = aux;
            stat = true;
         }
         // Else compare to key
         else if (comp.compare(aux, key) >= 0 && comp.compare(aux, ceiling) <= 0) {
            ceiling = aux;
         }
      }
      if (!stat) {
         throw new NoSuchElementException("Ceiling does not exist");
      }
      return ceiling;
   }


   /**
    * Returns the largest value in the Collection coll that is less than
    * or equal to key, as defined by the Comparator comp. The value of key
    * does not have to be in coll. If coll or comp is null, this method throws
    * an IllegalArgumentException. If coll is empty or if there is no
    * qualifying value, this method throws a NoSuchElementException. This
    * method will not change coll in any way.
    *
    * @param coll    the Collection from which the floor value is selected
    * @param key     the reference value
    * @param comp    the Comparator that defines the total order on T
    * @return        the floor value of key in coll
    * @throws        IllegalArgumentException as per above
    * @throws        NoSuchElementException as per above
    */
   public static <T> T floor(Collection<T> coll, T key, Comparator<T> comp) {
      if (coll == null || comp == null) {
         throw new IllegalArgumentException("Arguments must not be null");
      }
      if (coll.isEmpty()) {
         throw new NoSuchElementException("Collection must not be empty");
      }
      T floor = null;
      boolean stat = false; // Tracks if a floor exists
      Iterator<T> it = coll.iterator();
      while (it.hasNext()) {
         T aux = it.next();
         // If a floor has not been found, compare to key
         if(!stat && comp.compare(aux, key) <= 0) {
            floor = aux;
            stat = true;
         }
         // Else compare to key
         else if (comp.compare(aux, key) <= 0 && comp.compare(aux, floor) >= 0) {
            floor = aux;
         }
      }
      if (!stat) {
         throw new NoSuchElementException("Floor does not exist");
      }
      return floor;
   }
}
