package com.oxande.commons.oxutils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * This helper is mainly for use to avoid typing extra code and gives you better control on
 * what you do.
 */
public final class Safe {
    private static final Logger LOG = LoggerFactory.getLogger(Safe.class);

    /**
     * A very basic method to apply a function to an object only if this one is not null. You can
     * achieve the same results with {@link Optional}
     * using <code>Optional.ofNullable(object).map(function).orElse(null)</code>
     * but the code is more verbose and slower.
     *
     * <p>Example:
     * <pre>
     *     String result = null;
     *     if(str != null){
     *         result = str.trim();
     *     }
     * </pre>
     * <p>
     * The code can be replaced by: <code>String result = Safe.safe(str, s -> s.trim())</code>
     * or event better <code>String result = Safe.safe(str, String::trim)</code>
     * </p>
     *
     * @param object the object to use
     * @param function the transformation function
     * @param <T> the input class
     * @param <R> the output class
     * @return null if the object was null or the result of the function (which can be null).
     */
    public static <T, R> R safe(T object, Function<T, R> function) {
        return (object != null ? function.apply(object) : null);
    }

    /**
     * Create a list from a collection. Ensure the returned value is not null. Note the resulting {@link List}
     * is not immutable and you can add or remove items if needed.
     *
     * @param collection a collection (can be null)
     * @param <T> the type of the collection's elements.
     * @return a list made of these elements. This is always a copy of the original collection, not a reference.
     */
    public static <T> List<T> list(Collection<T> collection) {
        return Optional.ofNullable(collection)
                .map(ArrayList::new) // Create an array list of the perfect size
                .orElse(new ArrayList<>()); // or a default one
    }

    /**
     * Create a new set from a collection. Very useful to have a copy of the original list in the
     * setter for example. This is a good protection for null values.
     *
     * <pre>
     *     void setAuthorities( Collection&gt;GrantedAuthority> authorityList ){
     *         this.authorities = Safe.newSet(authorityList);
     *     }
     * </pre>
     *
     * <p>
     * For the getters, use directly the {@link Collections#unmodifiableList(List)} method.
     * </p>
     *
     * @param collection the collection to set (can be null)
     * @param <T> the type in the collection.
     * @return a {@link Set} made of values found in the collection.
     */
    public static <T> Set<T> newSet(Collection<T> collection) {
        return Optional.ofNullable(collection)
                .map(HashSet::new) // Create a set of the perfect size
                .orElse(new HashSet<>()); // or an empty one
    }

    /**
     * Create a new list from a collection. Very useful to have a copy of the original list in the
     * setter for example. This is a good protection for null values.
     *
     * <pre>
     *     void setUsers( Collection&gt;User> users ){
     *         this.users = Safe.newList(users);
     *     }
     * </pre>
     *
     * @param collection the collection to set (can be null)
     * @param <T> the type in the collection.
     * @return a {@link List} made of values found in the collection.
     */
    public static <T> List<T> newList(Collection<T> collection) {
        List<T> list = new ArrayList<>();
        if (collection != null) {
            list.addAll(collection);
        }
        return list;
    }

    /**
     * Create a list like {@link Arrays#asList(Object[])} but the returned list is an {@link ArrayList} implementation
     * and you can add more data in it.
     *
     * @param collection the objects
     * @param <T> the object type
     * @return an {@link ArrayList}.
     */
    public static <T> List<T> asList(T... collection) {
        ArrayList<T> list = new ArrayList<>(collection.length + 5);
        list.addAll(Arrays.asList(collection));
        return list;
    }

    /**
     * Create a new nap from a map. Very useful to have a copy of the original map.
     * This is a good protection for null values.
     *
     * <pre>
     *     void setUserNames( Map&gt;String, User> userMap ){
     *         this.userName = Safe.newMap(userMap);
     *     }
     * </pre>
     *
     * @param map the map to set (can be null)
     * @param <T> the type in the collection.
     * @return a {@link Map} made of values found in the original map.
     */
    public static <T, U> Map<T, U> newMap(Map<T, U> map) {
        Map<T, U> ret = new HashMap<>();
        if (map != null) {
            ret.putAll(map);
        }
        return ret;
    }

    /**
     * No operation. Only a debug is outputted.
     */
    public static void noop() {
        LOG.debug("NOOP Operation.");
    }

    public static boolean waitFor(long delay) {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return true;
            }
        }
        return false;
    }
}
