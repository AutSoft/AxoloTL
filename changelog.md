# Changelog

## nextver
* Inner refactor: Builtin `TaskScheduler` ids refactored

## 4.2.1
* Fix: ConcurrentModification exception fix for holder array

## 4.2.0
* Feature: Stetho finished

## 4.1
* Package refactor
* Module separation

## 4.0
* Queue added to `TaskEngineHolder`
    * Stores early tasks which are added before `TaskEngineHolder.start()`
    * `TaskEngineHolder.start()` will start these tasks
* Experimental feature: Engine changed to Rx

## 3.1.0
* `BaseTask.testWithSuccess(...)` and `BaseTask.testWithError(...)` added

## 3.0
* EventBus removed from project 