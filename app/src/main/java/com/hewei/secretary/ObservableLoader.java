package com.hewei.secretary;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;

/**
 * Created by fengyinpeng on 2018/2/1.
 */

public class ObservableLoader {
    private static int sId;

    public static <T> Observable<T> create(final Context context, final LoaderManager mgr,
                                           final Call<T> rawCall) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(final ObservableEmitter<T> e) throws Exception {
                mgr.restartLoader(sId++, null, new LoaderManager.LoaderCallbacks<T>() {
                    @Override
                    public Loader<T> onCreateLoader(int id, Bundle args) {
                        return new OKHttpLoader<>(context, rawCall, new OKHttpLoader.OnLoadErrorListener() {
                            @Override
                            public void onLoadError(Throwable t) {
                                e.onError(t);
                            }
                        });
                    }

                    @Override
                    public void onLoadFinished(Loader<T> loader, T data) {
                        e.onNext(data);
                        e.onComplete();
                    }

                    @Override
                    public void onLoaderReset(Loader<T> loader) {

                    }
                });
            }
        });
    }
}
