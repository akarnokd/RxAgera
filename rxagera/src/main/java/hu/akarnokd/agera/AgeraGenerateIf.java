package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Condition;
import com.google.android.agera.Updatable;

/**
 * For each incoming updatable, calls a generator callback
 */
final class AgeraGenerateIf extends AgeraTracking<GeneratorTarget> {

    final BiReceiver<Updatable, Condition> generator;

    AgeraGenerateIf(BiReceiver<Updatable, Condition> generator) {
        this.generator = generator;
    }

    @NonNull
    @Override
    protected GeneratorTarget createWrapper(@NonNull Updatable updatable) {
        return new GeneratorTarget(updatable);
    }

    @Override
    protected void onAdd(@NonNull Updatable updatable, @NonNull GeneratorTarget wrapper) {
        generator.accept(wrapper, wrapper);
    }

    @Override
    protected void onRemove(@NonNull Updatable updatable, @NonNull GeneratorTarget wrapper) {
        wrapper.cancelled = true;
    }

}

final class GeneratorTarget implements Updatable, Condition {
    final Updatable actual;

    volatile boolean cancelled;

    GeneratorTarget(Updatable actual) {
        this.actual = actual;
    }

    @Override
    public boolean applies() {
        return !cancelled;
    }

    @Override
    public void update() {
        actual.update();
    }
}