package ru.yandex.practicum.sleeptracker.model;

public class SleepAnalysisResult {

    private final String functionTitle;
    private final Object result;

    public SleepAnalysisResult(String functionName, Object result) {
        this.functionTitle = functionName;
        this.result = result;
    }

    public String getFunctionTitle() {
        return functionTitle;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public String toString() {
        return functionTitle + ": " + result;
    }
}
