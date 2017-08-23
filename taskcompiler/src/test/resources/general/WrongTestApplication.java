package hu.axolotl.test;

import hu.axolotl.tasklib.annotation.Injector;

public class WrongTestApplication {
    @Injector
    private static StaticInjector injector;
}