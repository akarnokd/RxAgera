# RxAgera

Convert between RxJava and Agera reactive base types

# Usage

```java

import static hu.akarnokd.rxagera.RxAgeraConverter.*;

import com.google.android.agera.*;

// .........

Observable o = toAgeraObservable(rx.Observable.just("Hello World!"));

o.addUpdatable(() -> System.out.println("Something happened!"));

// ;;;;;;

UpdateDispatcher ud = Observables.updateDispatcher();

rx.Observable rx = toRxObservable(ud);

rs.subscribe(() -> System.out.println("Something happened!"));

ud.update();
```

