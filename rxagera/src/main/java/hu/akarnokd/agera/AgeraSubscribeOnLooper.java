package hu.akarnokd.agera;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

/**
 * Subscribes on a specified looper.
 */
final class AgeraSubscribeOnLooper extends Agera {

    final Observable source;

    final Handler handler;

    AgeraSubscribeOnLooper(Observable source, Looper looper) {
        this.source = source;
        this.handler = new Handler(looper);
    }

    @Override
    public void addUpdatable(@NonNull final Updatable updatable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                source.addUpdatable(updatable);
            }
        });
    }

    @Override
    public void removeUpdatable(@NonNull final Updatable updatable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                source.removeUpdatable(updatable);
            }
        });
    }
}
