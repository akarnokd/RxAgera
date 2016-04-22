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
package hu.akarnokd.agera;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Executes a Runnable action for each update() call and offers cancellation
 * support
 */
final class RunnableUpdatable
extends AtomicBoolean
implements Updatable, Closeable {
    Observable parent;

    Runnable run;

    RunnableUpdatable(Observable parent, Runnable run) {
        this.parent = parent;
        this.run = run;
        set(false); // release fields
    }

    @Override
    public void update() {
        Runnable r = run;
        if (r != null) {
            r.run();
        }
    }

    @Override
    public void close() {
        if (compareAndSet(false, true)) {
            parent.removeUpdatable(this);
            parent = null;
            run = null;
        }
    }
}
