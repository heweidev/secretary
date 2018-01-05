package com.hewei.secretary;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fengyinpeng on 2018/1/4.
 */

public class OKHttpLoader<T> extends Loader<T> {
    private Call<T> mCall;

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public OKHttpLoader(Context context, Call<T> call) {
        super(context);
        mCall = call;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        mCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                deliverResult(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                deliverError(t);
            }
        });
    }

    @Override
    protected boolean onCancelLoad() {
        mCall.cancel();
        return false;
    }

    @Override
    protected void onForceLoad() {
        onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        onCancelLoad();
    }

    @Override
    protected void onReset() {
        mCall = null;
    }

    public void deliverError(Throwable t) {
        Log.d("OKHttpLoader", t.getMessage());
    }
}
