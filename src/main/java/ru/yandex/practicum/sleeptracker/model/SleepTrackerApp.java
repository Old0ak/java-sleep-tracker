package ru.yandex.practicum.sleeptracker.model;

import ru.yandex.practicum.sleeptracker.function.*;
import ru.yandex.practicum.sleeptracker.function.ChronotypeDetermination;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SleepTrackerApp {

    public static final String SEPARATOR = ";";
    private static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYTIC_FUNCTION = List.of(
            new SleepingSessionCounter(),
            new MinimalSleepingSession(),
            new MaximumSleepingSession(),
            new AverageSleepingSession(),
            new BadSleepingSessionCounter(),
            new SleeplessNightsCounter(),
            new ChronotypeDetermination()
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите путь к файлу.");
            return;
        }

        try {
            List<SleepingSession> sessions = readFile(getFile(args[0]));
            List<SleepAnalysisResult> results = analyzingSessions(sessions);
            results.forEach(System.out::println);

        } catch (IOException exception) {
            System.err.println("Ошибка при чтении файла: " + exception.getMessage());
        }

    }

    public static List<SleepAnalysisResult> analyzingSessions(List<SleepingSession> sessions) {
        return ANALYTIC_FUNCTION.stream()
                .map(function -> function.apply(sessions))
                .toList();
    }

    private static File getFile(String filename) throws FileNotFoundException {
        Path filePath = Paths.get(filename);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException(String.format("Не существует файла с именем %s", filename));
        }

        return file;
    }

    private static List<SleepingSession> readFile(File file) {
        List<SleepingSession> sessions = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(fileReader)) {
            sessions = reader.lines()
                    .map(SleepTrackerApp::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            if (sessions.isEmpty()) {
                System.out.println("Прочитан пустой файл " + file.getName());
            }

        } catch (IOException exception) {
            System.out.println("Произошла ошибка при чтении из файла" + file.getName());
        }

        return sessions;
    }

    public static Optional<SleepingSession> parseLine(String line) {
        try {
            String[] parts = line.split(SEPARATOR);
            LocalDateTime start = LocalDateTime.parse(parts[0].trim(), LOG_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(parts[1].trim(), LOG_TIME_FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(parts[2].trim().toUpperCase());

            if (start.isAfter(end)) {
                end = end.plusDays(1);
            }

            return Optional.of(new SleepingSession(start, end, quality));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}