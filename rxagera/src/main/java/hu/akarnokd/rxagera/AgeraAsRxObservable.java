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

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Subscriber;
import rx.Subscription;

/**
 * Wraps an Agera Observable and exposes it as a rx OnSubscribe.
 */
final class AgeraAsRxObservable implements rx.Observable.OnSubscribe<Object> {

    final Observable source;

    public AgeraAsRxObservable(Observable source) {
        this.source = source;
    }

    @Override
    public void call(Subscriber<? super Object> subscriber) {
        AgeraUpdatable u = new AgeraUpdatable(source, subscriber);
        source.addUpdatable(u);
        subscriber.add(u);
    }

    static final class AgeraUpdatable
            extends AtomicBoolean
            implements Updatable, Subscription {
        final Observable parent;

        final Subscriber<? super Object> actual;

        static final Object EVENT = new Object();

        AgeraUpdatable(Observable parent, Subscriber<? super Object> actual) {
            this.parent = parent;
            this.actual = actual;
        }

        @Override
        public void update() {
            actual.onNext(EVENT);
        }

        @Override
        public boolean isUnsubscribed() {
            return get();
        }

        @Override
        public void unsubscribe() {
            if (compareAndSet(false, true)) {
                parent.removeUpdatable(this);
            }
        }
    }
}