package com.app.analytics.core.base;

import android.util.Log;

import com.app.analytics.core.domain.abstraction.AppDataRepository;
import com.app.analytics.utils.AppConstants;
import com.app.analytics.utils.CustomHttpException;
import com.app.analytics.utils.NetworkUtil;

import java.io.IOException;
import java.net.HttpURLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * Created by Sanjay.k on 3/28/2017.
 */

public abstract class BaseUseCase<T> implements BaseInteractor<T> {
    public static final String TAG = BaseUseCase.class.getSimpleName();

    private final AppDataRepository appDataRepository;
    private DisposableObserver disposableObserver;

    public BaseUseCase(AppDataRepository appDataRepository) {

        Log.d(TAG, "AppDataRepository: " + appDataRepository);
        this.appDataRepository = appDataRepository;
        /*Constructor argument can take any custom ThreadExecutor(for subscribeOn) And PostExecutionThread(for observeOn)*/
    }


    protected abstract Observable<T> createObservable();

    protected abstract Observable<T> createObservable(Object objectKey);

    @Override
    public void execute(DisposableObserver<T> disposableObserver) {

        this.disposableObserver = disposableObserver;
        execute().subscribe(this.disposableObserver);
    }

    @Override
    public void execute(DisposableObserver<T> disposableObserver, Object objectKey) {

        this.disposableObserver = disposableObserver;
        execute(objectKey).subscribe(this.disposableObserver);
    }

    @Override
    public Observable<T> executeWithObserver(DisposableObserver<T> disposableObserver, Object objectKey) {

        Observable<T> tObservable = execute(objectKey);
        tObservable.subscribe(disposableObserver);
        return tObservable;
    }

    @Override
    public Observable<T> execute() {
        return validateAndTransformObservable()
                .retryWhen(throwableObservable -> throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> onError(throwable)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> execute(Object objectKey) {
        return validateAndTransformObservable(objectKey)
                .retryWhen(throwableObservable -> throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> onError(throwable)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<T> validateAndTransformObservable() {
        return appDataRepository.checkAndUpdateSessionToken()
                .flatMap(new Function<Boolean, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            return createObservable();
                        return Observable.just((T) aBoolean);
                    }
                });
    }

    @Override
    public Observable<T> validateAndTransformObservable(final Object objectKey) {
        return appDataRepository.checkAndUpdateSessionToken()
                .flatMap(new Function<Boolean, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            return createObservable(objectKey);
                        return Observable.just((T) aBoolean);
                    }
                });
    }

    private Observable<T> onError(Throwable throwable) {

        Log.d(TAG, "onError: " + throwable);
        if (throwable instanceof HttpException || throwable instanceof CustomHttpException) {
            /** A non-2XX HTTP status code was received from the server. */

            int errorCode = (throwable instanceof HttpException) ? ((HttpException) throwable).code() : ((CustomHttpException) throwable).getCode();
            String url = (throwable instanceof HttpException) ? ((HttpException) throwable).response().raw().request().url().toString() : ((CustomHttpException) throwable).getUrl();
            String uuid = (throwable instanceof HttpException) ? ((HttpException) throwable).response().raw().header(AppConstants.UUID, AppConstants.UUID_DEFAULT_VALUE) : ((CustomHttpException) throwable).getUuid();

            /*Initiate Flambe Analytics Error Event. Required: errorCode, requestUrl, uuid*/

            if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED)
                return updateTokensOn401Response(throwable);
        } else if (throwable instanceof IOException) {
            /** An {@link IOException} occurred while communicating to the server. */
        } else {
            /** An internal error occurred while attempting to execute a request. It is best practice to re-throw this exception so your application crashes. */
        }

        // some other kind of error: just pass it along and don't retry
        return Observable.error(throwable);
    }

    /**
     * With 401 error check response body, if it contains "USER" then renew BearerToken & If response body is "FABRIC" renew sessionToken.
     */
    private Observable<T> updateTokensOn401Response(Throwable throwable) {

        Log.d(TAG, "updateTokensOn401Response: " + throwable);
        try {
            String responseBody = (throwable instanceof HttpException) ? NetworkUtil.convertInputStreamToString(((HttpException) throwable).response().errorBody().byteStream()) : ((CustomHttpException) throwable).getType();

            if (responseBody.equals(AppConstants.FABRIC)) {

                // this is the error we care about - to trigger a retry we need to emit anything other than onError or onCompleted
                return Observable.just((T) new Object());
            } else if (responseBody.equals(AppConstants.USER)) {

                // this is the error we care about - to trigger a retry we need to emit anything other than onError or onCompleted
                return Observable.just((T) appDataRepository.checkAndUpdateBearerToken());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // some other kind of error: just pass it along and don't retry
        return Observable.error(throwable);
    }

    public void dispose() {
        if (disposableObserver != null && !disposableObserver.isDisposed()) {
            Log.d(TAG, "Disposed.");
            disposableObserver.dispose();
        }
    }
}
