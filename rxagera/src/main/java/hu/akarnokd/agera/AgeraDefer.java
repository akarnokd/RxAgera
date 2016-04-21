package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

/**
 * Defers the creation of the actual Observable till an Updatable registers.
 */
final class AgeraDefer extends AgeraTracking<DeferUpdatable> {

    final Supplier<? extends Observable> supplier;

    AgeraDefer(Supplier<? extends Observable> supplier) {
        this.supplier = supplier;
    }


    @NonNull
    @Override
    protected DeferUpdatable createWrapper(@NonNull Updatable updatable) {
        Observable source = supplier.get();
        return new DeferUpdatable(updatable, source);
    }

    @Override
    protected void onAdd(@NonNull Updatable updatable, @NonNull DeferUpdatable wrapper) {
        wrapper.add();
    }

    @Override
    protected void onRemove(@NonNull Updatable updatable, @NonNull DeferUpdatable wrapper) {
        wrapper.remove();
    }
}

final class DeferUpdatable
implements Updatable {
    final Updatable actual;

    final Observable source;

    DeferUpdatable(Updatable actual, Observable source) {
        this.actual = actual;
        this.source = source;
    }

    @Override
    public void update() {
        actual.update();
    }

    void add() {
        source.addUpdatable(this);
    }

    void remove() {
        source.removeUpdatable(this);
    }
}
