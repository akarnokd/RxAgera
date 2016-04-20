package hu.akarnokd.rxagera;

import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;
import com.google.android.agera.UpdateDispatcher;

import rx.subjects.Subject;

/**
 * Wraps an Rx Subject and exposes it as an Agera UpdateDispatcher.
 */
final class RxSubjectAsUpdateDispatcher implements UpdateDispatcher {

    final Subject<Object, ?> subject;

    final Observable ageraObservable;

    static final Object EVENT = new Object();

    RxSubjectAsUpdateDispatcher(Subject<Object, ?> subject) {
        this.subject = subject;
        this.ageraObservable = new RxObservableAsAgera(subject);
    }


    @Override
    public void addUpdatable(@NonNull Updatable updatable) {
        ageraObservable.addUpdatable(updatable);
    }

    @Override
    public void removeUpdatable(@NonNull Updatable updatable) {
        ageraObservable.removeUpdatable(updatable);
    }

    @Override
    public void update() {
        subject.onNext(EVENT);
    }
}