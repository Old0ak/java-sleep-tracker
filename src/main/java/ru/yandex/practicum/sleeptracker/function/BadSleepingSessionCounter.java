package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class BadSleepingSessionCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final String TITLE = "Количество сессий с плохим качество сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Long quantity = sleepingSessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult(TITLE, quantity);
    }
}
