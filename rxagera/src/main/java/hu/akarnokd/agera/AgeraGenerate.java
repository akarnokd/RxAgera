package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Receiver;
import com.google.android.agera.Updatable;

/**
 * For each incoming updatable, calls a generator callback
 */
final class AgeraGenerate extends AgeraTracking<Updatable> {

    final Receiver<Updatable> generator;

    AgeraGenerate(Receiver<Updatable> generator) {
        this.generator = generator;
    }


    @NonNull
    @Override
    protected Updatable createWrapper(@NonNull Updatable updatable) {
        return updatable;
    }

    @Override
    protected void onAdd(@NonNull Updatable updatable, @NonNull Updatable wrapper) {
        generator.accept(updatable);
    }

    @Override
    protected void onRemove(@NonNull Updatable updatable, @NonNull Updatable wrapper) {
        // nothing to do
    }
}
