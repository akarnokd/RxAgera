package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

/**
 * Subscribes on the given ExecutorService to the wrapper source.
 */
final class AgeraSubscribeOnExecutor extends AgeraTracking<Trampoline> {

    final Observable source;

    final Executor executor;

    AgeraSubscribeOnExecutor(Observable source, Executor executor) {
        this.source = source;
        this.executor = executor;
    }


    @NonNull
    @Override
    protected Trampoline createWrapper(@NonNull Updatable updatable) {
        return new Trampoline(new ConcurrentLinkedQueue<Runnable>(), executor);
    }

    @Override
    protected void onAdd(@NonNull final Updatable updatable, @NonNull Trampoline wrapper) {
        wrapper.offer(new Runnable() {
            @Override
            public void run() {
                source.addUpdatable(updatable);
            }
        });
    }

    @Override
    protected void onRemove(@NonNull final Updatable updatable, @NonNull Trampoline wrapper) {
        wrapper.offer(new Runnable() {
            @Override
            public void run() {
                source.removeUpdatable(updatable);
            }
        });
    }
}

