package com.nanda.socketexample.utils;


import com.nanda.socketexample.data.exception.ApiHttpException;
import com.nanda.socketexample.data.exception.AppException;

import java.io.IOException;

import retrofit2.Response;
import rx.Completable;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorNotImplementedException;
import rx.schedulers.Schedulers;

public class RxJavaUtils {
    public static <T> Observable.Transformer<T, T> applyObserverSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Completable.Transformer applyCompletableSchedulers() {
        return completable -> completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> Observable.Transformer<T, T> applyErrorTransformer() {
        return observable -> observable.onErrorResumeNext(throwable -> {
            if (throwable instanceof ApiHttpException) {
                Response response = ((ApiHttpException) throwable).response();
                if (AppException.isSanghaException(response)) {
                    try {
                        return Observable.error(AppException.create(response));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return Observable.error(throwable);
        });
    }

    public static <T> Observable.Transformer<T, T> applyOnErrorCrasher() {
        return observable -> observable.doOnError(throwable -> {
            final Throwable checkpoint = new Throwable();
            StackTraceElement[] stackTrace = checkpoint.getStackTrace();
            StackTraceElement element = stackTrace[1]; // First element after `crashOnError()`
            String msg = String.format("onError() crash from subscribe() in %s.%s(%s:%s)",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber());

            throw new OnErrorNotImplementedException(msg, throwable);
        });
    }
}

