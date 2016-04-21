package hu.akarnokd.agera;

import android.support.annotation.NonNull;

import com.google.android.agera.Condition;
import com.google.android.agera.Observable;
import com.google.android.agera.Updatable;

/**
 * Filters update() signals if the condition evaluates to false.
 */
final class AgeraFilter extends AgeraSource<FilterUpdatable> {
    final Condition condition;

    AgeraFilter(@NonNull Observable source, @NonNull Condition condition) {
        super(source);
        this.condition = condition;
    }

    @NonNull
    @Override
    protected FilterUpdatable createWrapper(@NonNull Updatable updatable) {
        return new FilterUpdatable(updatable, condition);
    }
}

final class FilterUpdatable implements Updatable {
    final Updatable actual;

    final Condition condition;

    FilterUpdatable(Updatable actual, Condition condition) {
        this.actual = actual;
        this.condition = condition;
    }

    @Override
    public void update() {
        if (condition.applies()) {
            actual.update();
        }
    }
}
