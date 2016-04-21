package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Observe update() signals on the specified ExecutorService.
 */
final class AgeraObserveOnExecutor extends AgeraTracking<ObserveOnExecutor> {
    final Observable source;

    final Executor executor;

    AgeraObserveOnExecutor(Observable source, Executor executor) {
        this.source = source;
        this.executor = executor;
    }

    @NonNull
    @Override
    protected ObserveOnExecutor createWrapper(@NonNull Updatable updatable) {
        return new ObserveOnExecutor(updatable, executor);
    }

    @Override
    protected void onAdd(@NonNull Updatable updatable, @NonNull ObserveOnExecutor wrapper) {
        source.addUpdatable(wrapper);
    }

    @Override
    protected void onRemove(@NonNull Updatable updatable, @NonNull ObserveOnExecutor wrapper) {
        wrapper.cancelled = true;
        source.removeUpdatable(wrapper);
    }

}

final class ObserveOnExecutor
        extends AtomicLong
        implements Updatable, Runnable {

    final Updatable actual;

    final Executor executor;

    volatile boolean cancelled;

    ObserveOnExecutor(Updatable actual, Executor executor) {
        this.actual = actual;
        this.executor = executor;
    }

    @Override
    public void run() {
        long c = get();

        Updatable u = actual;

        for (;;) {
            for (long i = 0; i < c; i++) {
                if (cancelled) {
                    return;
                }

                u.update();
            }

            c = addAndGet(-c);
            if (c == 0L) {
                break;
            }
        }
    }

    @Override
    public void update() {
        if (getAndIncrement() == 0) {
            executor.execute(this);
        }
    }
}