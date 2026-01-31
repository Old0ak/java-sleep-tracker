package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightsCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {

    public static final String TITLE = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        if (sleepingSessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, 0.0);
        }

        LocalDate startDateSleepingSessions = sleepingSessions.stream()
                .map(session -> session.getStart().toLocalDate())
                .min(LocalDate::compareTo)
                .orElseThrow(() -> new IllegalArgumentException("Список сессий пуст"));

        LocalDate endDateSleepingSessions = sleepingSessions.stream()
                .map(session -> session.getFinish().toLocalDate())
                .max(LocalDate::compareTo)
                .orElse(startDateSleepingSessions);

        long allNights = ChronoUnit.DAYS.between(startDateSleepingSessions, endDateSleepingSessions) + 1;

        long sleepingNightsQuantity = sleepingSessions.stream()
                .filter(this::isSleepingNights)
                .count();

        long sleeplessNightsCount = allNights - sleepingNightsQuantity;

        return new SleepAnalysisResult(TITLE, sleeplessNightsCount);
    }

    private boolean isSleepingNights(SleepingSession session) {
        LocalTime sleepWindowStart = LocalTime.of(0, 0);
        LocalTime sleepWindowEnd = LocalTime.of(6, 0);

        return (session.getStart().toLocalDate().isBefore(session.getFinish().toLocalDate()) ||
                (session.getStart().toLocalTime().isAfter(sleepWindowStart) &&
                        session.getFinish().toLocalTime().isBefore(sleepWindowEnd)));
    }
}
