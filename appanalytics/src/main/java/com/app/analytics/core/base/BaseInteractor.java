package com.app.analytics.core.base;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by Sanjay.k on 1/19/2017.
 */

public interface BaseInteractor<T> {

    void execute(DisposableObserver<T> disposableObserver);

    void execute(DisposableObserver<T> disposableObserver, Object objectKey);

    Observable<T> executeWithObserver(DisposableObserver<T> disposableObserver, Object objectKey);

    Observable<T> execute();

    Observable<T> execute(Object objectKey);

    Observable<T> validateAndTransformObservable();

    Observable<T> validateAndTransformObservable(Object objectKey);

}
