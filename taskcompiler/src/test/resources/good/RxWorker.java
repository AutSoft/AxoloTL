package hu.axolotl.test;

import java.util.List;

import hu.axolotl.tasklib.annotation.CreateTask;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class RxWorker {

    @CreateTask
    public Observable<Integer> integerObservable() {
        return null;
    }

    @CreateTask
    public Flowable<Integer> integerFlowable() {
        return null;
    }

    @CreateTask
    public Flowable<List<String>> stringListFlowable() {
        return null;
    }

    @CreateTask
    public Single<Double> doubleSingle() {
        return null;
    }

    @CreateTask
    public Completable completable() {
        return null;
    }
}