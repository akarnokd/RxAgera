package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

/**
 * An Agera that has a source Observable and the only add/remove behavior is to
 * subscribe the custom Updatable wrapper created.
 */
abstract class AgeraSource<U extends Updatable> extends AgeraTracking<U> {
    protected final Observable source;

    AgeraSource(@NonNull Observable source) {
        this.source = source;
    }


    @Override
    protected final void onAdd(@NonNull Updatable updatable, @NonNull U wrapper) {
        source.addUpdatable(wrapper);
    }

    @Override
    protected final void onRemove(@NonNull Updatable updatable, @NonNull U wrapper) {
        source.removeUpdatable(wrapper);
    }
}
