# Aut task lib
-keep class hu.axolotl.tasklib.** { *; }
-dontwarn hu.axolotl.tasklib.**
-keepclassmembers class ** {
    *** onTaskResult*(**);
}
-keepclassmembers class ** {
    *** onTaskGlobalError*(**);
}

-keepclassmembers class ** {
    *** onTaskProgress*(...);
}
