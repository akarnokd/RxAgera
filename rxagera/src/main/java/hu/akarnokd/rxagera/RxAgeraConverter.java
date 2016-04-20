package hu.akarnokd.rxagera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.UpdateDispatcher;

import rx.subjects.Subject;

/**
 * Converts between RxJava and Google/Agera classes.
 */
public final class RxAgeraConverter {

    private RxAgeraConverter() {
        throw new IllegalStateException("No instances!");
    }

    /**
     * Wraps an Agera Observable and exposes it as an Rx Observable.
     * <p>
     *     Note that Agera and the returned wrapper doesn't do backpressure on
     *     their own, you may need to apply {@code onBackpressureXXX} on the
     *     sequence.
     * </p>
     * @param ageraObservable the source Agera Observable
     * @return the wrapper Rx Observable
     */
    @NonNull
    public static rx.Observable<Object> toRxObservable(@NonNull Observable ageraObservable) {
        return rx.Observable.create(new AgeraAsRxObservable(ageraObservable));
    }

    /**
     * Wraps an Rx Observable and exposes it as an Agera Observable.
     * <p>
     *     All signal types are emitted as update() calls to the Updatables.
     * </p>
     * <p>
     *     The Rx Observable is published and auto-connected on the first incoming Updatable.
     *     If the source terminates, all further call to addUpdatable is rejected via
     *     IllegalStateException.
     * </p>
     * @param rxObservable the source Rx Observable
     * @return the wrapper Agera Observable
     */
    @NonNull
    public static Observable toAgeraObservable(@NonNull rx.Observable<?> rxObservable) {
        return new RxObservableAsAgera(rxObservable);
    }

    /**
     * Wraps an Rx Subject and exposes it as an Agera UpdateDispatcher.
     *
     * @param rxSubject the source Rx Subject
     * @return the wrapper Agera UpdateDispatcher
     */
    @NonNull
    public static UpdateDispatcher toAgeraUpdateDispatcher(@NonNull Subject<Object, ?> rxSubject) {
        return new RxSubjectAsUpdateDispatcher(rxSubject);
    }

    /**
     * Wraps an Agera UpdateDispatcher and exposes it as an Rx Subject.
     *
     * @param ageraUpdateDispatcher the source Agera UpdateDispatcher
     * @return the wrapper Rx Subject
     */
    @NonNull
    public static Subject<Object, Object> toRxSubject(@NonNull UpdateDispatcher ageraUpdateDispatcher) {
        return new UpdateDispatcherAsSubject(ageraUpdateDispatcher);
    }
}