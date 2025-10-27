package org.metaxava.model;

public interface ValueType<T> {
    T parse(String s);
    // May need to be refined so as to accommodate a formatting hint. E.g. a format mask ($###,###.##)
    String format(T t);
}
