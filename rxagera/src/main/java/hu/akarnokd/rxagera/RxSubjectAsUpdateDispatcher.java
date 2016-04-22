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