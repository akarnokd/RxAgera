package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Updatable;

/**
 * Calls the update method on the Updatables a specified number of times.
 */
final class AgeraRange extends AgeraTracking<Updatable> {

    final int count;

    AgeraRange(int count) {
        this.count = count;
    }


    @NonNull
    @Override
    protected Updatable createWrapper(@NonNull Updatable updatable) {
        return updatable;
    }

    @Override
    protected void onAdd(@NonNull Updatable updatable, @NonNull Updatable wrapper) {
        for (int i = 0; i < count; i++) {
            if (isListening(updatable)) {
                updatable.update();
            }
        }
    }

    @Override
    protected void onRemove(@NonNull Updatable updatable, @NonNull Updatable wrapper) {
        // nothing to do
    }
}
