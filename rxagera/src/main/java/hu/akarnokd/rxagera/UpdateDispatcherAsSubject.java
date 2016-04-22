/*
 * Copyright 2016 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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