package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

/**
 * Skips the first N signals.
 */
final class AgeraSkip extends AgeraSource<SkipUpdatable> {
    final long n;

    AgeraSkip(Observable source, long n) {
        super(source);
        this.n = n;
    }

    @NonNull
    @Override
    protected SkipUpdatable createWrapper(@NonNull Updatable updatable) {
        return new SkipUpdatable(updatable, n);
    }
}

final class SkipUpdatable implements Updatable {
    final Updatable actual;
    long remaining;

    SkipUpdatable(Updatable actual, long remaining) {
        this.actual = actual;
        this.remaining = remaining;
    }

    @Override
    public void update() {
        long r = remaining;
        if (r == 0L) {
            actual.update();
            return;
        }
        remaining = r - 1;
    }
}
