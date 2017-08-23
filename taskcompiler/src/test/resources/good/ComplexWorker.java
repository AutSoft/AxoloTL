package hu.axolotl.test;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;

import java.util.List;
import java.util.Map;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.annotation.CreateTask;

public class ComplexWorker {

    @CreateTask
    public void simple() {
    }

    @CreateTask
    public int simpleWithIntReturn() {
        return 0;
    }

    @CreateTask
    public Object simpleWithObjectReturn() {
        return null;
    }

    @CreateTask
    public int[] simpleWithIntArrayReturn() {
        return null;
    }

    @CreateTask
    public Integer[] simpleWithIntegerArrayReturn() {
        return null;
    }

    @CreateTask
    public List<String> simpleWithGenericReturn() {
        return null;
    }

    @CreateTask
    public Map<Integer, String> simpleWithGenericMapReturn() {
        return null;
    }

    @CreateTask
    public Map<List<Integer>, HashBasedTable<Integer, HashMultimap<Integer, String>, String>> simpleWithComplexGenericReturn() {
        return null;
    }

    @CreateTask
    public void intParam(int param) {
    }

    @CreateTask
    public void intArrayParam(int[] param) {
    }

    @CreateTask
    public void objectParam(Object param) {
    }

    @CreateTask
    public void boxedDoubleArrayParam(Double[] param) {
    }

    @CreateTask
    public void multipleParam(Object param1, Double param2, int param3) {
    }

    @CreateTask
    public Object multipleParamWithObjectReturn(Object param1, Double param2, int param3) {
        return null;
    }

    @CreateTask
    public void taskAgent(TaskAgent taskAgent) {
    }

    @CreateTask
    public void taskAgentWithParam(TaskAgent<Double> taskAgent, int param) {
    }

    @CreateTask(5)
    public void customExecutor() {
    }
}