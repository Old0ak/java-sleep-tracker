package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeDetermination implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final String TITLE = "Ваш хронотип";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .collect(Collectors.groupingBy(
                        session -> determineSleepType(session.getStart().toLocalTime(), session.getFinish().toLocalTime()),
                        Collectors.counting()
                ));

        long maxCount = counts.values().stream().max(Long::compare).orElse(0L);

        long countOfMax = counts.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxCount))
                .count();

        Chronotype mostChronotype;
        if (countOfMax > 1) {
            mostChronotype = Chronotype.PIGEON;
        } else {
            mostChronotype = counts.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(Chronotype.PIGEON);
        }

        return new SleepAnalysisResult(TITLE, mostChronotype);
    }

    private Chronotype determineSleepType(LocalTime sleepStartTime, LocalTime sleepEndTime) {
        if (sleepStartTime.isAfter(LocalTime.of(23, 0)) &&
                sleepEndTime.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        } else if (sleepStartTime.isBefore(LocalTime.of(22, 0)) &&
                sleepEndTime.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }
}
