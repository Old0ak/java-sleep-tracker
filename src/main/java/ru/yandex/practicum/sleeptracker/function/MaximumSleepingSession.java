package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MaximumSleepingSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Максимальная продолжительность сна (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long minDurationInMinutes = sleepingSessions.stream()
                .mapToLong(session ->
                        Duration.between(session.getStart(), session.getFinish()).toMinutes())
                .max()
                .orElse(0);

        return new SleepAnalysisResult(TITLE, minDurationInMinutes);
    }
}
