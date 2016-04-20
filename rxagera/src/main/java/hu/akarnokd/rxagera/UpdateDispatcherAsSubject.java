package hu.akarnokd.rxagera;

import com.google.android.agera.UpdateDispatcher;

import rx.subjects.Subject;

/**
 * Wraps an Agera UpdateDispatcher and exposes it as an Rx Subject.
 */
final class UpdateDispatcherAsSubject extends Subject<Object, Object> {

    final UpdateDispatcher updateDispatcher;

    UpdateDispatcherAsSubject(UpdateDispatcher updateDispatcher) {
        super(new AgeraAsRxObservable(updateDispatcher));
        this.updateDispatcher = updateDispatcher;
    }

    @Override
    public void onNext(Object o) {
        updateDispatcher.update();
    }

    @Override
    public void onError(Throwable e) {
        updateDispatcher.update();
    }

    @Override
    public void onCompleted() {
        updateDispatcher.update();
    }

    @Override
    public boolean hasObservers() {
        return false; // hard to tell
    }
}