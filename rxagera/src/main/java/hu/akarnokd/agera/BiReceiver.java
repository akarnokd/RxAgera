package hu.akarnokd.agera;

/**
 * Receiver of two values.
 * @param <T> the first value type
 * @param <U> the second value type
 */
public interface BiReceiver<T, U> {
    void accept(T t, U u);
}
