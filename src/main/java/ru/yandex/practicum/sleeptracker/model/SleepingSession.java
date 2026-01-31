package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class SleepingSession {

    private LocalDateTime start;
    private LocalDateTime finish;
    private SleepQuality quality;

    public SleepingSession(LocalDateTime start, LocalDateTime finish, SleepQuality quality) {
        this.start = start;
        this.finish = finish;
        this.quality = quality;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SleepingSession that = (SleepingSession) o;
        return start.equals(that.start) &&
                finish.equals(that.finish) &&
                quality.equals(that.quality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, finish, quality);
    }
}
