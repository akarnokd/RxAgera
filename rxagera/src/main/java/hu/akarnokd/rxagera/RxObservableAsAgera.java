package hu.akarnokd.rxagera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.util.HashMap;
import java.util.HashSet;

import rx.Subscriber;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;
import rx.subjects.Subject;

/**
 * Wraps an rx Observable and exposes it as an Agera Observable.
 * <p>
 *     The underlying rx Observable is subscribed to when the first
 *     Updatable is added and shared among Updatables via publish().
 * </p>
 */
final class RxObservableAsAgera implements Observable, Action0 {

    final rx.Observable<?> source;

    HashMap<Updatable, Subscriber<Object>> updatables;

    public RxObservableAsAgera(rx.Observable<?> source) {
        if (source instanceof Subject) {
            // no need to publish/autoConnect a subject
            this.source = source.doAfterTerminate(this);
        } else {
            this.source = source.doAfterTerminate(this).publish().autoConnect();
        }
        this.updatables = new HashMap<>();
    }

    @Override
    public void addUpdatable(@NonNull Updatable updatable) {
        UpdatableAsSubscriber s;
        synchronized (this) {
            if (updatables == null) {
                throw new IllegalStateException("The source Rx Observable has terminated");
            }

            if (updatables.containsKey(updatable)) {
                throw new IllegalStateException("The Updatable is already added");
            }

            s = new UpdatableAsSubscriber(updatable);
            updatables.put(updatable, s);
        }
        source.unsafeSubscribe(s);
    }

    @Override
    public void removeUpdatable(@NonNull Updatable updatable) {
        Subscriber<Object> s;
        synchronized (this) {
            if (updatables == null) {
                return;
            }

            s = updatables.remove(updatable);
            if (s == null) {
                throw new IllegalStateException("Updatable was already removed");
            }
        }
        s.unsubscribe();
    }

    @Override
    public synchronized void call() {
        updatables = null;
    }

    static final class UpdatableAsSubscriber extends Subscriber<Object> {
        final Updatable actual;

        UpdatableAsSubscriber(Updatable actual) {
            this.actual = actual;
        }

        @Override
        public void onNext(Object o) {
            actual.update();
        }

        @Override
        public void onError(Throwable e) {
            try {
                actual.update();
            } finally {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            }
        }

        @Override
        public void onCompleted() {
            actual.update();
        }
    }
}