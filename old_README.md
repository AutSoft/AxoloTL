# AxoloTL
Axolo Task Library

# OLD-DEPRECATED

## Task usage


## Errors in tasks
* A worker függvényen belüli exception kijuthat, a task engine elkapja és lekezeli (mint hiba)
* A worker függvényben dobhatunk TaskMessageException-t (ez RuntimeException)
	* kétféle paraméterrel hozhatjuk létre: int vagy String (ez inkább csak nagyon kezdeti debug célra)
* Ha a worker függvényén belül exception keletkezik, akkor az engine elkapja és a túloldalon a task hasError függvénye true-val tér vissza
	* utána pedig kiolvashat a getMessageCode() illetve getMessage() függvénnyel az int vagy String érték

## Injektálás a Task-on belül
* Ahhoz, hogy dagger-rel együtt lehessen használni a tasklib-et, fel kell annotálni az injektort, hogy az egyes worker osztályokba injektálhatóak legyenek a különböző dolgok, hogy a task-ok használhassák. Ehhez @Inject annotációval kell felannotálni a dagger injektor:
```
@Inject
public static TestInjector injector;
```

## Kiegészítők
### Task futtatása egyedi ExecutorService-en
* NEXTVER (deprecated def)
* `@Executor` annotációval megadható egy új ExecutorService osztály
* Ebből a TaskLib létrehoz egy példányt és minden olyan task-ot ebbe az egy példányba ütemez, ami ugyanezzel a class-szal van felannotálva

### Task progress kezelése (2.1-től)
* Ehhez a @CreateTask-os worker függvény első paramétere egy TaskAgent kell legyen
	* ezen hívható egy updateProgress függvény és itt át lehet adni a progresst
	* A küldés helyén onTaskProgress függvényt kell létrehozni, első paramétere maga a task, második pedig egy Object, ami a progress
		* itt tehát cast-olni kell az Object-et arra, amit dobhatunk
* Megjegyzés: garantált, hogy egy task lefutása után már nem fogunk ahhoz progress-t kapni

### Globális hibakezelés (2.1-től)
* A worker függvényben dobhatunk GlobalTaskMessageException-t
	* ugyanúgy működik, mint a TaskMessageException, csak ez fennakad a global handler-en és le lehet kezelni globálisan is
	* Ehhez egy boolean onTaskGlobalError(GlobalError error) függvényt kell definiálni. Addig fogja hívogatni az ilyen aktív függvényeket a TaskEngine, amíg valahol true-t nem kap vissza.
	* PÉLDA: bármilyen hálózati kérés elhal azért, mert lejárt a login session, akkor a global handler-ben lekezelhető és ki lehet dobni a user-t a login képernyőre
// FIXME

### Testing
* To test task execution, use test Executor: TaskGlobalUtil.setTestExecutor();
* To emulate successful task execution: onTaskResult(new ConcreteTask().testWithSuccess(result));
* To emulate task error: onTaskResult(new ConcreteTask().testWithError(errorCode));


[Changelog](changelog.md)


# License

```
Copyright 2017 AutSoft Kft.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
