package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Updatable;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Adds tracking structures and behavior to an Agera.
 */
abstract class AgeraTracking<U> extends Agera {

    /**
     * Tracks the original Updatable instances and their associated wrappers.
     */
    final ConcurrentHashMap<Updatable, U> map = new ConcurrentHashMap<>();

    @Override
    public final void addUpdatable(@NonNull Updatable updatable) {
        U wrapper = createWrapper(updatable);
        if (map.putIfAbsent(updatable, wrapper) != null) {
            throw new IllegalStateException("The updatable instance has been already added");
        }
        onAdd(updatable, wrapper);
    }

    @Override
    public final void removeUpdatable(@NonNull Updatable updatable) {
        U wrapper = map.remove(updatable);
        if (wrapper == null) {
            throw new IllegalStateException("The updatable is (no longer) registered with this Observable");
        }
        onRemove(updatable, wrapper);
    }

    /**
     * Returns true if the updatable is still tracked by this object.
     * @param updatable the updatable to check
     * @return true if the updatable is still tracked
     */
    protected final boolean isListening(@NonNull Updatable updatable) {
        return map.containsKey(updatable);
    }

    /**
     * Called for an incoming Updatable instance and allows creating a
     * wrapper object for it.
     * @param updatable the original updatable
     * @return the wrapper object
     */
    @NonNull
    protected abstract U createWrapper(@NonNull Updatable updatable);

    /**
     * Called when the updatable was successfully added to the tracking structure,
     * allowing further initialization to happen.
     * @param updatable the original updatable instance
     * @param wrapper the wrapper from {@link #createWrapper(Updatable)}
     */
    protected abstract void onAdd(@NonNull Updatable updatable, @NonNull U wrapper);

    /**
     * Called when the updatable was successfully removed from the tracking structure,
     * allowing cleanup to happen.
     * @param updatable the original updatable instance
     * @param wrapper the wrapper from {@link #createWrapper(Updatable)}
     */
    protected abstract void onRemove(@NonNull Updatable updatable, @NonNull U wrapper);

    /**
     * Returns true if there are registered Updatable instances.
     * @return true if there are registered Updatable instances
     */
    public final boolean hasUpdatables() {
        return !map.isEmpty();
    }
}
