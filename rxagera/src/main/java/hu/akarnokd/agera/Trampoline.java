package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Makes sure tasks offered are executed in a FIFO order on an Executor.
 */
public final class Trampoline
        extends AtomicInteger
        implements Runnable {

    final Queue<Runnable> queue;

    final Executor executor;

    public Trampoline(@NonNull Queue<Runnable> queue, @NonNull Executor executor) {
        this.queue = queue;
        this.executor = executor;
    }

    @Override
    public void run() {
        do {

            queue.poll().run();

        } while (decrementAndGet() != 0);
    }

    public void offer(@NonNull Runnable task) {
        queue.offer(task);

        if (getAndIncrement() == 0) {
            executor.execute(this);
        }
    }
}
