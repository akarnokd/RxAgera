# RxAgera

Convert between RxJava and Agera reactive base types

# Dependency

```
compile 'com.github.akarnokd:rxagera:0.9.1'
```

# Build status

<a href='https://travis-ci.org/akarnokd/RxAgera/builds'><img src='https://travis-ci.org/akarnokd/RxAgera.svg?branch=master'></a>

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

rx.subscribe(() -> System.out.println("Something happened!"));

ud.update();
```

