package hu.axolotl.tasklib;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import hu.axolotl.tasklib.exception.EngineRuntimeException;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public abstract class TaskSchedulers {

    public static final int SINGLE = 0;
    public static final int COMPUTATION = -1;
    public static final int IO = -2;
    public static final int TRAMPOLINE = -3;
    public static final int NEW_THREAD = -4;

    private static Map<Integer, Scheduler> schedulerMap = new HashMap<>();

    static {
        addScheduler(SINGLE, Schedulers.single(), true);
        addScheduler(COMPUTATION, Schedulers.computation(), true);
        addScheduler(IO, Schedulers.io(), true);
        addScheduler(TRAMPOLINE, Schedulers.trampoline(), true);
        addScheduler(NEW_THREAD, Schedulers.newThread(), true);
    }

    private static void addScheduler(int id, Scheduler scheduler, boolean builtIn) {
        if (!builtIn && id <= 0) {
            throw new EngineRuntimeException("Scheduler id must be a positive number (error: " + id + ")");
        }
        if (schedulerMap.containsKey(id)) {
            throw new EngineRuntimeException("Scheduler already exists with id: " + id);
        }
        schedulerMap.put(id, scheduler);
    }

    public static void registerScheduler(int id, Scheduler scheduler) {
        addScheduler(id, scheduler, false);
    }

    public static void registerScheduler(int id, Executor executor) {
        registerScheduler(id, Schedulers.from(executor));
    }

    public static Scheduler getScheduler(int id) {
        if (!schedulerMap.containsKey(id)) {
            throw new EngineRuntimeException("Scheduler not exists with id: " + id);
        }
        return schedulerMap.get(id);
    }

}
