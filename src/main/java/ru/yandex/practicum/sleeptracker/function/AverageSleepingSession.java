package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class AverageSleepingSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Средняя продолжительность сна (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        if (sleepingSessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, 0.0);
        }

        long allDurationInMinutes = sleepingSessions.stream()
                .mapToLong(session ->
                        Duration.between(session.getStart(), session.getFinish()).toMinutes())
                .sum();

        double averageDurationInMinutes = (double) allDurationInMinutes / sleepingSessions.size();

        return new SleepAnalysisResult(TITLE, averageDurationInMinutes);
    }
}
