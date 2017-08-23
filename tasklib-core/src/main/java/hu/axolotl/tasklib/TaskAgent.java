package hu.axolotl.tasklib;

public interface TaskAgent<T> {
    void publishProgress(T progress);
}