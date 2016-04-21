package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

/**
 * Wraps an arbitrary Agera Observable and exposes fluent services over it.
 */
final class AgeraWrapper extends Agera {
    final Observable source;


    AgeraWrapper(@NonNull Observable source) {
        this.source = source;
    }

    @Override
    public void addUpdatable(@NonNull Updatable updatable) {
        source.addUpdatable(updatable);
    }

    @Override
    public void removeUpdatable(@NonNull Updatable updatable) {
        source.removeUpdatable(updatable);
    }
}
