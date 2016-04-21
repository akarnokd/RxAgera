package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Updatable;

/**
 * Doesn't signal anything.
 */
final class AgeraEmpty extends AgeraTracking<Updatable> {
    @NonNull
    @Override
    protected Updatable createWrapper(@NonNull Updatable updatable) {
        return updatable;
    }

    @Override
    protected void onAdd(@NonNull Updatable updatable, @NonNull Updatable wrapper) {
        // nothing to do
    }

    @Override
    protected void onRemove(@NonNull Updatable updatable, @NonNull Updatable wrapper) {
        // nothing to do
    }
}
