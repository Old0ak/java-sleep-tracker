package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.function.*;
import ru.yandex.practicum.sleeptracker.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SleepTrackerAppTest {

    @Test
    void testAnalyzingSessions() {
        List<SleepingSession> testSessions = new ArrayList<>();
        testSessions.add(new SleepingSession(LocalDateTime.of(26, 1, 1, 22, 0),
                LocalDateTime.of(26, 1, 2, 8, 0), SleepQuality.GOOD));
        testSessions.add(new SleepingSession(LocalDateTime.of(26, 1, 3, 23, 0),
                LocalDateTime.of(26, 1, 4, 9, 0), SleepQuality.BAD));

        List<SleepAnalysisResult> results = SleepTrackerApp.analyzingSessions(testSessions);

        assertEquals(7, results.size());
    }

    @Test
    void testParseLine() {
        String logLine = "01.01.26 22:00;02.01.26 08:00;GOOD";

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 22, 0);
        LocalDateTime finish = LocalDateTime.of(2026, 1, 2, 8, 0);
        SleepingSession session = new SleepingSession(start, finish, SleepQuality.GOOD);

        Optional<SleepingSession> result = SleepTrackerApp.parseLine(logLine);

        assertTrue(result.isPresent());
        assertEquals(session, result.get());
    }

    @Test
    void testFunctionSleepingSessionCounterByAllEmptyLine() {
        List<SleepingSession> emptySessions = new ArrayList<>();

        SleepAnalysisResult result = new SleepingSessionCounter().apply(emptySessions);

        assertEquals("Количество сессий сна", result.getFunctionTitle());
        assertEquals(0, result.getResult());
    }

    @Test
    void testFunctionSleepingSessionCounterByThreeLine() {
        List<SleepingSession> sessions = new ArrayList<>();

        sessions.add(new SleepingSession(LocalDateTime.of(26, 1, 1, 22, 0),
                LocalDateTime.of(26, 1, 2, 8, 0), SleepQuality.GOOD));
        sessions.add(new SleepingSession(LocalDateTime.of(26, 1, 3, 23, 0),
                LocalDateTime.of(26, 1, 4, 9, 0), SleepQuality.BAD));
        sessions.add(new SleepingSession(LocalDateTime.of(26, 1, 5, 21, 0),
                LocalDateTime.of(26, 1, 6, 7, 0), SleepQuality.NORMAL));

        SleepAnalysisResult result = new SleepingSessionCounter().apply(sessions);

        assertEquals("Количество сессий сна", result.getFunctionTitle());
        assertEquals(3, result.getResult());
    }

    @Test
    void testFunctionMinimalSleepingSessionByEmptyList() {
        MinimalSleepingSession minimalSleepingSession = new MinimalSleepingSession();
        List<SleepingSession> emptyList = new ArrayList<>();

        SleepAnalysisResult result = minimalSleepingSession.apply(emptyList);

        assertEquals("Минимальная продолжительность сна (в минутах)", result.getFunctionTitle());
        assertEquals((long) 0, result.getResult());
    }

    @Test
    void testFunctionMinimalSleepingSessionBySixtyMinutes() {
        MinimalSleepingSession minimalSleepingSession = new MinimalSleepingSession();

        LocalDateTime start1 = LocalDateTime.of(26, 1, 1, 22, 0);
        LocalDateTime end1 = LocalDateTime.of(26, 1, 1, 23, 0); // 60 минут
        SleepingSession session1 = new SleepingSession(start1, end1, SleepQuality.GOOD);

        LocalDateTime start2 = LocalDateTime.of(26, 1, 2, 22, 0);
        LocalDateTime end2 = LocalDateTime.of(26, 1, 2, 23, 1); // 61 минута
        SleepingSession session2 = new SleepingSession(start2, end2, SleepQuality.BAD);

        List<SleepingSession> twoSessionsList = List.of(session1, session2);
        SleepAnalysisResult result = minimalSleepingSession.apply(twoSessionsList);

        assertEquals("Минимальная продолжительность сна (в минутах)", result.getFunctionTitle());
        assertEquals((long) 60, result.getResult());
    }

    @Test
    void testFunctionMaximumSleepingSessionByEmptyList() {
        MaximumSleepingSession maximumSleepingSession = new MaximumSleepingSession();
        List<SleepingSession> emptySession = new ArrayList<>();

        SleepAnalysisResult result = maximumSleepingSession.apply(emptySession);

        assertEquals("Максимальная продолжительность сна (в минутах)", result.getFunctionTitle());
        assertEquals((long) 0, result.getResult());
    }

    @Test
    void testFunctionMaximumSleepingSessionBySixtyOneMinutes() {
        MaximumSleepingSession maximumSleepingSession = new MaximumSleepingSession();

        LocalDateTime start1 = LocalDateTime.of(26, 1, 1, 22, 0);
        LocalDateTime end1 = LocalDateTime.of(26, 1, 1, 23, 0); // 60 минут
        SleepingSession session1 = new SleepingSession(start1, end1, SleepQuality.GOOD);

        LocalDateTime start2 = LocalDateTime.of(26, 1, 2, 22, 0);
        LocalDateTime end2 = LocalDateTime.of(26, 1, 2, 23, 1); // 61 минута
        SleepingSession session2 = new SleepingSession(start2, end2, SleepQuality.BAD);

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = maximumSleepingSession.apply(sessions);

        assertEquals("Максимальная продолжительность сна (в минутах)", result.getFunctionTitle());
        assertEquals((long) 61, result.getResult());
    }

    @Test
    void testFunctionAverageSleepingSessionByEmptyList() {
        AverageSleepingSession averageSleepingSession = new AverageSleepingSession();
        List<SleepingSession> emptySession = new ArrayList<>();

        SleepAnalysisResult result = averageSleepingSession.apply(emptySession);

        assertEquals("Средняя продолжительность сна (в минутах)", result.getFunctionTitle());
        assertEquals((double) 0, result.getResult());
    }

    @Test
    void testFunctionAverageSleepingSessionBy60Minutes() {
        AverageSleepingSession averageSleepingSession = new AverageSleepingSession();

        LocalDateTime start1 = LocalDateTime.of(26, 1, 1, 22, 0);
        LocalDateTime end1 = LocalDateTime.of(26, 1, 1, 22, 59); // 59 минут
        SleepingSession session1 = new SleepingSession(start1, end1, SleepQuality.GOOD);

        LocalDateTime start2 = LocalDateTime.of(26, 1, 2, 22, 0);
        LocalDateTime end2 = LocalDateTime.of(26, 1, 2, 23, 1); // 61 минута
        SleepingSession session2 = new SleepingSession(start2, end2, SleepQuality.BAD);

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = averageSleepingSession.apply(sessions);

        assertEquals("Средняя продолжительность сна (в минутах)", result.getFunctionTitle());
        assertEquals(60, (Double) result.getResult());
    }

    @Test
    void testFunctionBadSleepingSessionByEmptyList() {
        BadSleepingSessionCounter badSleepingSessionCounter = new BadSleepingSessionCounter();

        List<SleepingSession> emptySession = new ArrayList<>();

        SleepAnalysisResult result = badSleepingSessionCounter.apply(emptySession);

        assertEquals("Количество сессий с плохим качество сна", result.getFunctionTitle());
        assertEquals((long) 0, result.getResult());
    }

    @Test
    void testFunctionBadSleepingSessionByTwoSession() {
        BadSleepingSessionCounter badSleepingSessionCounter = new BadSleepingSessionCounter();

        LocalDateTime start1 = LocalDateTime.of(26, 1, 1, 22, 0);
        LocalDateTime end1 = LocalDateTime.of(26, 1, 1, 23, 0);
        SleepingSession session1 = new SleepingSession(start1, end1, SleepQuality.GOOD);

        LocalDateTime start2 = LocalDateTime.of(26, 1, 2, 22, 0);
        LocalDateTime end2 = LocalDateTime.of(26, 1, 2, 23, 0);
        SleepingSession session2 = new SleepingSession(start2, end2, SleepQuality.BAD);

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = badSleepingSessionCounter.apply(sessions);

        assertEquals("Количество сессий с плохим качество сна", result.getFunctionTitle());
        assertEquals((long) 1,  result.getResult());
    }

    @Test
    public void testFunctionSleeplessNightsCounterByEmptySessions() {
        SleeplessNightsCounter sleeplessNightsCounter = new SleeplessNightsCounter();

        List<SleepingSession> emptySessions = List.of();
        SleepAnalysisResult result = sleeplessNightsCounter.apply(emptySessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals((double) 0, result.getResult());
    }

    @Test
    public void testFunctionSleeplessNightsCounterByOneSleeplessNight() {
        SleeplessNightsCounter sleeplessNightsCounter = new SleeplessNightsCounter();

        LocalDateTime start = LocalDateTime.of(26, 1, 1, 23, 0);
        LocalDateTime finish = LocalDateTime.of(26, 1, 1, 23, 30);
        SleepingSession session = new SleepingSession(start, finish, SleepQuality.BAD);

        List<SleepingSession> sessions = List.of(session);
        SleepAnalysisResult result = sleeplessNightsCounter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals((long) 1, result.getResult());
    }

    @Test
    public void testFunctionSleeplessNightsCounterByAcrossMonths() {
        SleeplessNightsCounter sleeplessNightsCounter = new SleeplessNightsCounter();

        LocalDateTime start = LocalDateTime.of(26, 1, 31, 23, 0);
        LocalDateTime finish = LocalDateTime.of(26, 2, 1, 0, 30);
        SleepingSession session = new SleepingSession(start, finish, SleepQuality.BAD);

        List<SleepingSession> sessions = List.of(session);
        SleepAnalysisResult result = sleeplessNightsCounter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals((long) 1, result.getResult());
    }

    @Test
    public void testFunctionSleeplessNightsCounterByMultipleSessionsInDifferentTimes() {
        SleeplessNightsCounter sleeplessNightsCounter = new SleeplessNightsCounter();

        LocalDateTime start1 = LocalDateTime.of(26, 1, 1, 22, 0);
        LocalDateTime finish1 = LocalDateTime.of(26, 1, 2, 6, 0);
        SleepingSession session1 = new SleepingSession(start1, finish1, SleepQuality.GOOD);

        LocalDateTime start2 = LocalDateTime.of(26, 1, 5, 23, 30);
        LocalDateTime finish2 = LocalDateTime.of(26, 1, 6, 2, 30);
        SleepingSession session2 = new SleepingSession(start2, finish2, SleepQuality.BAD);

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = sleeplessNightsCounter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        // Пустой промежуток в 3 бессонных ночи + 1 сессия с бессонной ночью: итого 4
        assertEquals((long) 4, result.getResult());
    }

    @Test
    public void testChronotypeDeterminationByLarkOrPigeon() {
        ChronotypeDetermination chronotypeDetermination = new ChronotypeDetermination();

        LocalDateTime start1 = LocalDateTime.of(2023, 10, 1, 21, 59);
        LocalDateTime finish1 = LocalDateTime.of(2023, 10, 1, 6, 59);
        SleepingSession session1 = new SleepingSession(start1, finish1, SleepQuality.GOOD); // Жаворонок

        LocalDateTime start2 = LocalDateTime.of(2023, 10, 2, 23, 0);
        LocalDateTime finish2 = LocalDateTime.of(2023, 10, 2, 8, 59);
        SleepingSession session2 = new SleepingSession(start2, finish2, SleepQuality.GOOD); // Голубь

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = chronotypeDetermination.apply(sessions);

        assertEquals("Ваш хронотип", result.getFunctionTitle());
        assertEquals(Chronotype.PIGEON, result.getResult());
    }

    @Test
    public void testChronotypeDeterminationByOwlOrPigeon() {
        ChronotypeDetermination chronotypeDetermination = new ChronotypeDetermination();

        LocalDateTime start1 = LocalDateTime.of(2023, 10, 1, 23, 1);
        LocalDateTime finish1 = LocalDateTime.of(2023, 10, 1, 9, 1);
        SleepingSession session1 = new SleepingSession(start1, finish1, SleepQuality.GOOD); // Сова

        LocalDateTime start2 = LocalDateTime.of(2023, 10, 2, 23, 0);
        LocalDateTime finish2 = LocalDateTime.of(2023, 10, 2, 9, 0);
        SleepingSession session2 = new SleepingSession(start2, finish2, SleepQuality.GOOD); // Голубь

        List<SleepingSession> sessions = List.of(session1, session2);
        SleepAnalysisResult result = chronotypeDetermination.apply(sessions);

        assertEquals("Ваш хронотип", result.getFunctionTitle());
        assertEquals(Chronotype.PIGEON, result.getResult());
    }

    @Test
    public void testChronotypeDeterminationByEmptySessions() {
        ChronotypeDetermination chronotypeDetermination = new ChronotypeDetermination();

        List<SleepingSession> emptySessions = List.of();
        SleepAnalysisResult result = chronotypeDetermination.apply(emptySessions);

        assertEquals("Ваш хронотип", result.getFunctionTitle());
        assertEquals(Chronotype.PIGEON, result.getResult());
    }
}