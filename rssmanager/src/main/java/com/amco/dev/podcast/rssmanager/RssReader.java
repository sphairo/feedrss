package com.amco.dev.podcast.rssmanager;

import android.support.annotation.NonNull;
import android.util.Log;

import org.simpleframework.xml.core.Persister;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RssReader {
    private static final String TAG = "RssReader";

    @NonNull
    private RssCallback rssCallback;

    private Disposable disposable;

    public RssReader(@NonNull RssCallback rssCallback) {
        this.rssCallback = rssCallback;
    }

    public void loadFeeds(final String url) {
        final long startTimeMillis = System.currentTimeMillis();

        final List<Observable<RSS>> observables = new ArrayList<>();
        observables.add(Observable.create(new ObservableOnSubscribe<RSS>() {
            @Override
            public void subscribe(ObservableEmitter<RSS> emitter) throws Exception {
                try {
                    RSS rss = new Persister().read(RSS.class, RssParser.parse(url).body().string());
                    Log.d(TAG, "subscribe: url: " + url + " ; thread: " + Thread.currentThread().getName());
                    emitter.onNext(rss);
                    emitter.onComplete();
                } catch (InterruptedIOException e) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }
            }
        }));

        Observable<List<RSS>> rssListObservable = Observable.zip(observables, new Function<Object[], List<RSS>>() {
            @Override
            public List<RSS> apply(Object[] objects) throws Exception {
                final List<RSS> rssList = new ArrayList<>();

                for (Object object : objects) {
                    rssList.add((RSS) object);
                }

                return rssList;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        disposable = rssListObservable.subscribeWith(new DisposableObserver<List<RSS>>() {
            @Override
            public void onNext(List<RSS> rssList) {
                rssCallback.rssFeedsLoaded(rssList);
                Log.d(TAG, "onNext: rssListSize: " + rssList.size());
            }

            @Override
            public void onError(Throwable e) {
                rssCallback.unableToReadRssFeeds(e.getMessage());
                Log.e(TAG, "onError: " + e.getMessage(), e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, String.format(Locale.ENGLISH, "onComplete: done with time spent(ms): %d", (System.currentTimeMillis() - startTimeMillis)));
            }
        });
    }

    // Crashes
    public void destroy() {
        disposable.dispose();
    }

    public interface RssCallback {
        void rssFeedsLoaded(List<RSS> rssList);

        void unableToReadRssFeeds(String errorMessage);
    }
}
