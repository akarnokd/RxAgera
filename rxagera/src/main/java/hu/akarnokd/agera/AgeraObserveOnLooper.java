package hu.akarnokd.agera;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Signals to update() are delivered on the specified looper.
 */
final class AgeraObserveOnLooper extends AgeraTracking<ObserveOnLooper> {

    final Observable source;

    final Handler handler;

    AgeraObserveOnLooper(Observable source, Looper looper) {
        this.source = source;
        this.handler = new Handler(looper);
    }

    @NonNull
    @Override
    protected ObserveOnLooper createWrapper(@NonNull Updatable updatable) {
        return new ObserveOnLooper(updatable, handler);
    }

    @Override
    protected void onAdd(@NonNull Updatable updatable, @NonNull ObserveOnLooper wrapper) {
        source.addUpdatable(wrapper);
    }

    @Override
    protected void onRemove(@NonNull Updatable updatable, @NonNull ObserveOnLooper wrapper) {
        wrapper.cancelled = true;
        source.removeUpdatable(wrapper);
    }
}

final class ObserveOnLooper
    extends AtomicLong
        implements Updatable, Runnable {

    final Updatable actual;

    final Handler handler;

    volatile boolean cancelled;

    ObserveOnLooper(Updatable actual, Handler handler) {
        this.actual = actual;
        this.handler = handler;
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
            handler.post(this);
        }
    }
}