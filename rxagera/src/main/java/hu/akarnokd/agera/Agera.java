package hu.akarnokd.agera;

import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.agera.Condition;
import com.google.android.agera.Function;
import com.google.android.agera.Observable;
import com.google.android.agera.Predicate;
import com.google.android.agera.Receiver;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Fluent API entry point which is based on Google/Agera.
 */
public abstract class Agera implements Observable {

    // ************************************************************************
    // Factory methods
    // ************************************************************************

    /**
     * Returns an Agera which signals exactly one update().
     * @return the Agera instance
     */
    public static Agera just() {
        return new AgeraJust();
    }

    /**
     * Returns an Agera which calls the update() the given number of times.
     * @param count the number of times to signal
     * @return the Agera instance
     */
    public static Agera range(int count) {
        return new AgeraRange(count);
    }

    /**
     * Returns an Agera which never calls update().
     * @return the Agera instance
     */
    public static Agera empty() {
        return new AgeraEmpty();
    }

    /**
     * Wraps a generic Observable into an Agera or returns it if it is already an Agera.
     * @param observable the Observable to wrap
     * @return the Agera instance
     */
    public static Agera wrap(@NonNull Observable observable) {
        if (observable instanceof Agera) {
            return (Agera)observable;
        }
        return new AgeraWrapper(observable);
    }

    /**
     * For each updatable, the given receiver is called which then can call
     * update() as it sees fit.
     * @param generator the generator that receives each updatable
     * @return the Agera instance
     */
    public static Agera generate(Receiver<Updatable> generator) {
        return new AgeraGenerate(generator);
    }

    /**
     * For each updatable, the given receiver is called with the updatable
     * and a condition telling if the Updatable is still interested in
     * receiving update() calls.
     *
     * @param generator the generator that receives each updatable and condition pairs
     * @return the Agera instance
     */
    public static Agera generate(BiReceiver<Updatable, Condition> generator) {
        return new AgeraGenerateIf(generator);
    }

    /**
     * Calls the supplier to return an Observable for each registering Updatable
     * and registers the Updatable with that specific Observable.
     * @param supplier the supplier of Observables
     * @return the Agera instance
     */
    public static Agera defer(Supplier<? extends Observable> supplier) {
        return new AgeraDefer(supplier);
    }

    // ************************************************************************
    // Instance methods
    // ************************************************************************

    public final Agera subscribeOnMain() {
        return subscribeOn(Looper.getMainLooper());
    }

    public final Agera subscribeOn(Executor executor) {
        return new AgeraSubscribeOnExecutor(this, executor);
    }

    public final Agera subscribeOn(Looper looper) {
        return new AgeraSubscribeOnLooper(this, looper);
    }

    public final Agera observeOnMain() {
        return observeOn(Looper.getMainLooper());
    }

    public final Agera observeOn(Executor executor) {
        return new AgeraObserveOnExecutor(this, executor);
    }

    public final Agera observeOn(Looper looper) {
        return new AgeraObserveOnLooper(this, looper);
    }

    public final Agera skip(long n) {
        return new AgeraSkip(this, n);
    }

    public final Agera take(long limit) {
        return new AgeraTake(this, limit);
    }

    public final Agera filter(Condition condition) {
        return new AgeraFilter(this, condition);
    }

    public final Agera flatMap(Supplier<Observable> mapper) {
        return new AgeraFlatMap(this, mapper);
    }

    public final <U> U as(Function<? super Agera, ? extends U> converter) {
        return converter.apply(this);
    }

    public final Agera compose(Function<? super Agera, ? extends Agera> composer) {
        return as(composer);
    }
}
