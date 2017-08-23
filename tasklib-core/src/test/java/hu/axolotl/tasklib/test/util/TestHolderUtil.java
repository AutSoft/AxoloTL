/*
 * Copyright (C) 2017 AutSoft Kft.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.axolotl.tasklib.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import hu.axolotl.tasklib.TaskEngine;
import hu.axolotl.tasklib.test.base.BaseTarget;

public class TestHolderUtil {

    public static final String TAG = TestHolderUtil.class.getSimpleName();

    static TestHolderUtil holderUtil;

    public static TestHolderUtil getInstance() {
        if (holderUtil == null) {
            holderUtil = new TestHolderUtil();
        }
        return holderUtil;
    }

    final Lock lock = new ReentrantLock();
    final Condition empty = lock.newCondition();
    final List<BaseTarget> targets = new ArrayList<>();

    private TestHolderUtil() {
    }

    public void registerTarget(BaseTarget baseTarget) {
        lock.lock();
        try {
            targets.add(baseTarget);
        } finally {
            lock.unlock();
        }
    }

    public void onTaskPendingFinished() {
        lock.lock();
        try {
            UtilTestLogger.v(TAG, "onTaskPendingFinished: " + TaskEngine.getInstance().getPendingTaskCount());
            if (TaskEngine.getInstance().getPendingTaskCount() == 0) {
                empty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitUntilFinished() {
        lock.lock();
        try {
            try {
                UtilTestLogger.v(TAG, "waitUntilFinished: " + TaskEngine.getInstance().getPendingTaskCount());
                if (TaskEngine.getInstance().getPendingTaskCount() > 0) {
                    boolean success = empty.await(1, TimeUnit.SECONDS);
                    if (!success) {
                        throw new InterruptedException();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("waitUntilFinished interrupted", e);
            }
            try {
                Thread.sleep(20);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    public void stopHolders() {
        for (BaseTarget target : targets) {
            target.stopHolder();
        }
        targets.clear();
    }

}
