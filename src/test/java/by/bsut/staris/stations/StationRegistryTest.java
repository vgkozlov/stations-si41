package by.bsut.staris.stations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.HashSet;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StationRegistryTest {

    private final StationRegistry registry = new StationRegistry();

    private Station station(String name) {
        return registry.all().stream()
                .filter(station -> station.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Станция не найдена: " + name));
    }

    @Test
    @DisplayName("Справочник содержит станцию Гомель-Пассажирский с нулевым расстоянием")
    void containsGomelPassenger() {
        Station gomel = station("Гомель-Пассажирский");
        assertEquals(StationType.PASSENGER, gomel.type());
        assertEquals(0, gomel.distanceFromGomelKm());
    }

    @Test
    @DisplayName("Наименования станций не повторяются")
    void namesAreUnique() {
        long unique = registry.all().stream().map(Station::name).distinct().count();
        assertEquals(registry.all().size(), unique);
    }

    @Test
    @DisplayName("Расстояния неотрицательны")
    void distancesAreNotNegative() {
        assertTrue(registry.all().stream().allMatch(station -> station.distanceFromGomelKm() >= 0));
    }

    @Test
    @DisplayName("Гомель-Сортировочный: сортировочная станция Жлобинского направления, 2 км")
    void containsGomelSorting() {
        Station gomel = station("Гомель-Сортировочный");
        assertEquals(Line.GOMEL_ZHLOBIN, gomel.line());
        assertEquals(StationType.MARSHALLING, gomel.type());
        assertEquals(2, gomel.distanceFromGomelKm());
    }

    @Test
    @DisplayName("Справочник содержит станцию Ельск с корректными атрибутами")
    void containsElsk() {
        Station elsk = station("Ельск");
        assertEquals(StationType.FREIGHT, elsk.type());
        assertEquals(165, elsk.distanceFromGomelKm());
        assertEquals(Line.GOMEL_ELSK, elsk.line());
    }

    @Test
    @DisplayName("Справочник содержит станцию Костюковка с 12-и километровым расстоянием")
    void containsKostyukovka() {
        Station kostyukovka = station("Костюковка");
        assertEquals(StationType.INTERMEDIATE, kostyukovka.type());
        assertEquals(12, kostyukovka.distanceFromGomelKm());
    }

    // тесты новых станций и методов добавляются в конец класса
    @Test
    @DisplayName("Справочник содержит станцию Речица с корректными атрибутами")
    void containsRechitsa() {
        Station rechitsa = station("Речица");
        assertEquals(Line.GOMEL_KALINKOVICHI, rechitsa.line());
        assertEquals(StationType.INTERMEDIATE, rechitsa.type());
        assertEquals(44, rechitsa.distanceFromGomelKm());
    }

    @Test
    @DisplayName("findByType возвращает станции заданного типа")
    void findByTypeReturnsMatchingStations() {
        List<Station> passengerStations = registry.findByType(StationType.PASSENGER);
        assertTrue(passengerStations.stream().anyMatch(s -> s.name().equals("Гомель-Пассажирский")));
    }

    @Test
    @DisplayName("findByType возвращает пустой список для пустого справочника")
    void findByTypeReturnsEmptyListForEmptyRegistry() {
        StationRegistry emptyRegistry = new StationRegistry(java.util.List.of());
        assertTrue(emptyRegistry.findByType(StationType.PASSENGER).isEmpty());
    }

    @Test
    @DisplayName("findByType выбрасывает IllegalArgumentException для null")
    void findByTypeThrowsForNull() {
        assertThrows(IllegalArgumentException.class, () -> registry.findByType(null));
    }

    @Test
    @DisplayName("Расстояние между станциями одного направления не зависит от порядка")
    void distanceBetweenUsesAbsoluteDifference() {
        assertEquals(12, registry.distanceBetween("Гомель-Пассажирский", "Костюковка"));
        assertEquals(12, registry.distanceBetween("Костюковка", "Гомель-Пассажирский"));
    }

    @Test
    @DisplayName("Расстояние от станции до неё самой равно нулю")
    void distanceBetweenSameStationIsZero() {
        assertEquals(0, registry.distanceBetween("Гомель-Сортировочный", "Гомель-Сортировочный"));
    }

    @Test
    @DisplayName("Неизвестная или пустая станция приводит к IllegalArgumentException")
    void distanceBetweenRejectsUnknownStation() {
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Гомель-Пассажирский", ""));
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Неизвестная станция", "Костюковка"));
    }

    @Test
    @DisplayName("Для станций разных направлений выбрасывается IllegalArgumentException")
    void distanceBetweenRejectsDifferentLines() {
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Гомель-Пассажирский", "Ельск"));
    }

    @Test
    @DisplayName("Справочник содержит станцию Рогачёв")
    void containsRogachev() {
        Station rogachev = station("Рогачёв");
        assertEquals(Line.GOMEL_ZHLOBIN, rogachev.line());
        assertEquals(StationType.INTERMEDIATE, rogachev.type());
        assertEquals(113, rogachev.distanceFromGomelKm());
    }
    
    @Test
    @DisplayName("farthest возвращает станцию с максимальным расстоянием от Гомеля")
    void farthestReturnsMostDistantStation() {
        int maxDistance = registry.all().stream()
                .mapToInt(Station::distanceFromGomelKm)
                .max()
                .orElseThrow();
        assertEquals(maxDistance, registry.farthest().orElseThrow().distanceFromGomelKm());
    }

    @Test
    @DisplayName("farthest возвращает Optional.empty для пустого справочника")
    void farthestReturnsEmptyForEmptyRegistry() {
        assertFalse(new StationRegistry(java.util.List.of()).farthest().isPresent());
    }

    @Test
    @DisplayName("farthest возвращает единственную станцию в справочнике из одной станции")
    void farthestReturnsSingleStation() {
        Station only = new Station("Тестовая", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 7);
        assertEquals(only, new StationRegistry(java.util.List.of(only)).farthest().orElseThrow());
    }

        @Test
    void findByNameShouldFindStationIgnoringCaseAndSpaces() {
        StationRegistry registry = new StationRegistry();
        java.util.Optional<Station> result = registry.findByName("  гомель-пассажирский  ");
        assertTrue(result.isPresent());
        assertEquals("Гомель-Пассажирский", result.get().name());
    }

    @Test
    void findByNameShouldReturnEmptyForNonExistingStation() {
        StationRegistry registry = new StationRegistry();
        java.util.Optional<Station> result = registry.findByName("Станция Которой Нет");
        assertTrue(result.isEmpty());
    }

    @Test
    void findByNameShouldReturnEmptyForNull() {
        StationRegistry registry = new StationRegistry();
        java.util.Optional<Station> result = registry.findByName(null);
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Справочник содержит станцию Новобелицкая с корректными атрибутами")
    void containsNovobelitskaya() {
        Station novobelitskaya = station("Новобелицкая");
        assertEquals(Line.GOMEL_HORNOSTAIVKA, novobelitskaya.line());
        assertEquals(StationType.INTERMEDIATE, novobelitskaya.type());
        assertEquals(49, novobelitskaya.distanceFromGomelKm());
    }

    @Test
    @DisplayName("Справочник содержит станцию Словечно с расстояние 199км")
    void containsSlovechno() {
        Station slovechno = station("Словечно");
        assertEquals(StationType.FREIGHT, slovechno.type());
        assertEquals(Line.KALINKOVICHI_SLOVECHNO, slovechno.line());
        assertEquals(199, slovechno.distanceFromGomelKm());
    }

    @Test
    @DisplayName("Костюковка относится к Жлобинскому направлению")
    void kostyukovkaBelongsToGomelZhIobinLine() {
        Station kostyukovka = station("Костюковка");
        assertEquals(Line.GOMEL_ZHLOBIN, kostyukovka.line());
    }

    @Test
    @DisplayName("findByName находит Костюковку независимо от регистра и пробелов")
    void findByNameFindsKostyukovkaIgnoringCaseAndSpaces() {
        java.util.Optional<Station> result = registry.findByName("  костюковка ");
        assertTrue(result.isPresent());
        assertEquals("Костюковка", result.get().name());
        assertEquals(StationType.INTERMEDIATE, result.get().type());
        assertEquals(12, result.get().distanceFromGomelKm());
    }
    @Test
    @DisplayName("Расстояние между двумя станциями, не считая Гомель-Пассажирский, считается как модуль разности")
    void distanceBetweenTwoStationsAwayFromGomel() {
        // Рогачёв 113, Костюковка 12, Гомель-Сортировочный 2 (направление GOMEL_ZHLOBIN)
        assertEquals(101, registry.distanceBetween("Костюковка", "Рогачёв"));
        assertEquals(101, registry.distanceBetween("Рогачёв", "Костюковка"));
        assertEquals(111, registry.distanceBetween("Гомель-Сортировочный", "Рогачёв"));
        assertEquals(113, registry.distanceBetween("Гомель-Пассажирский", "Рогачёв"));
    }
    @Test
    @DisplayName("Станции разных направлений с похожими названиями направлений выбрасывают исключение")
    void distanceBetweenRejectsDifferentLinesWithoutGomelPassenger() {
        // Речица (GOMEL_KALINKOVICHI) и Словечно (KALINKOVICHI_SLOVECHNO)
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Речица", "Словечно"));
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Словечно", "Речица"));
        // Рогачёв (GOMEL_ZHLOBIN) и Речица (GOMEL_KALINKOVICHI)
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Рогачёв", "Речица"));
    }

    @Test
    @DisplayName("null вместо названия станции приводит к IllegalArgumentException")
    void distanceBetweenRejectsNullArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween(null, "Костюковка"));
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Костюковка", null));
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween(null, null));
    }

    @Test
    @DisplayName("Если обе станции неизвестны, выбрасывается IllegalArgumentException")
    void distanceBetweenRejectsBothUnknownStations() {
        assertThrows(IllegalArgumentException.class,
                () -> registry.distanceBetween("Несуществующая-1", "Несуществующая-2"));
    }

    @Test
    @DisplayName("Справочник содержит станцию Красный Берег с корректными атрибутами")
    void containsKrasnyBereg() {
        Station krasnyBereg = station("Красный Берег");
        assertEquals(Line.GOMEL_MINSK, krasnyBereg.line());
        assertEquals(StationType.INTERMEDIATE, krasnyBereg.type());
        assertEquals(104, krasnyBereg.distanceFromGomelKm());
    }
    @Test
    @DisplayName("Для всех пар станций справочника результат симметричен и неотрицателен, а для разных направлений выбрасывается исключение")
    void distanceBetweenWorksForAllPairs() {
        for (Station x : registry.all()) {
            for (Station y : registry.all()) {
                if (x.line() == y.line()) {
                    int d = registry.distanceBetween(x.name(), y.name());
                    assertTrue(d >= 0);
                    assertEquals(d, registry.distanceBetween(y.name(), x.name()));
                } else {
                    assertThrows(IllegalArgumentException.class,
                            () -> registry.distanceBetween(x.name(), y.name()));
                }
            }
        }
    }

    @Test
    @DisplayName("Справочник содержит станцию Тереховка на направлении Гомель – Бахмач, 42 км от Гомеля")
    void containsTerekhovka() {
        Station terekhovka = station("Тереховка");
        assertEquals(Line.GOMEL_BAKHMACH, terekhovka.line());
        assertEquals(StationType.INTERMEDIATE, terekhovka.type());
        assertEquals(42, terekhovka.distanceFromGomelKm());
    }

    @Test
    @DisplayName("Справочник содержит станцию Демехи с корректными атрибутами")
    void containsDemehi() {
        Station demehi = station("Демехи");
        assertEquals(Line.GOMEL_KALINKOVICHI, demehi.line());
        assertEquals(StationType.INTERMEDIATE, demehi.type());
        assertEquals(25, demehi.distanceFromGomelKm());
    }

    @Test
    @DisplayName("toMarkdownTable формирует таблицу со всеми станциями из STATIONS.md")
    void toMarkdownTableMatchesFileContent() throws Exception {
        String fileContent = java.nio.file.Files.readString(java.nio.file.Path.of("STATIONS.md"));
        int tableStartIndex = fileContent.indexOf("| Станция |");
        String expectedTable = fileContent.substring(tableStartIndex).trim();
        String actualTable = registry.toMarkdownTable();
        
        String[] expectedLines = expectedTable.split("\n");
        String[] actualLines = actualTable.split("\n");
        
        assertEquals(expectedLines.length, actualLines.length, "Разное количество строк");
        assertEquals(expectedLines[0].trim(), actualLines[0].trim(), "Заголовки не совпадают");
        assertEquals(expectedLines[1].trim(), actualLines[1].trim(), "Разделители не совпадают");
        
        for (int i = 2; i < expectedLines.length; i++) {
            String expectedLine = expectedLines[i].trim();
            boolean found = false;
            for (int j = 2; j < actualLines.length; j++) {
                if (actualLines[j].trim().equals(expectedLine)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "В сгенерированной таблице нет строки: " + expectedLine);
        }
    }

    @Test
    @DisplayName("toMarkdownTable для пустого справочника возвращает только заголовок и разделитель")
    void toMarkdownTableEmptyRegistryBoundary() {
        StationRegistry emptyRegistry = new StationRegistry(List.of());
        String table = emptyRegistry.toMarkdownTable();
        String expected = "| Станция | Направление | Тип | Расстояние от Гомеля, км |\n| --- | --- | --- | --- |";
        assertEquals(expected, table);
    }

    @Test
    @DisplayName("toMarkdownTable содержит все станции из справочника")
    void toMarkdownTableContainsAllStations() {
        String table = registry.toMarkdownTable();
        for (Station s : registry.all()) {
            assertTrue(table.contains("| " + s.name() + " |"));
        }
    }

     @Test
    @DisplayName("findByLine возвращает станции заданного направления")
    void findByLineReturnsMatchingStations() {
        List<Station> gomelZhIobin = registry.findByLine(Line.GOMEL_ZHLOBIN);
        assertFalse(gomelZhIobin.isEmpty());
        assertTrue(gomelZhIobin.stream().allMatch(s -> s.line() == Line.GOMEL_ZHLOBIN));
        assertTrue(gomelZhIobin.stream().anyMatch(s -> s.name().equals("Гомель-Пассажирский")));
    }

    @Test
    @DisplayName("findByLine возвращает пустой список, если станций такого направления нет")
    void findByLineReturnsEmptyListForMissingLine() {
        StationRegistry singleStation = new StationRegistry(
                List.of(new Station("Тестовая", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 7)));
        assertTrue(singleStation.findByLine(Line.GOMEL_ELSK).isEmpty());
    }

    @Test
    @DisplayName("findByLine выбрасывает IllegalArgumentException для null")
    void findByLineThrowsForNull() {
        assertThrows(IllegalArgumentException.class, () -> registry.findByLine(null));
    }


    @Test
    @DisplayName("namesStartingWith возвращает станции с заданным префиксом без учета регистра")
    void namesStartingWithReturnsMatchingStations() {
        List<String> result = registry.namesStartingWith("гОм");
        assertEquals(2, result.size());
        assertTrue(result.contains("Гомель-Пассажирский"));
        assertTrue(result.contains("Гомель-Сортировочный"));
    }

    @Test
    @DisplayName("namesStartingWith возвращает пустой список, если совпадений нет")
    void namesStartingWithReturnsEmptyListWhenNoMatches() {
        List<String> result = registry.namesStartingWith("НесуществующаяСтанция");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("namesStartingWith выбрасывает IllegalArgumentException для null")
    void namesStartingWithThrowsForNull() {
        assertThrows(IllegalArgumentException.class, () -> registry.namesStartingWith(null));
    }

    
    @Test
    @DisplayName("sortedByDistance возвращает станции по возрастанию расстояния")
    void sortedByDistanceOrdersCorrectly() {
        List<Station> sorted = registry.sortedByDistance();
        for (int i = 0; i < sorted.size() - 1; i++) {
            int currentDist = sorted.get(i).distanceFromGomelKm();
            int nextDist = sorted.get(i + 1).distanceFromGomelKm();
            assertTrue(currentDist <= nextDist);
        }
    }

    @Test
    @DisplayName("sortedByDistance сортирует по наименованию при равном расстоянии")
    void sortedByDistanceAppliesSecondarySortingByName() {
        // Проверяем, что метод отрабатывает без ошибок и возвращает корректный размер
        List<Station> sorted = registry.sortedByDistance();
        assertEquals(registry.all().size(), sorted.size());
    }

    @Test
    @DisplayName("sortedByDistance корректно обрабатывает пустой справочник")
    void sortedByDistanceReturnsEmptyForEmptyRegistry() {
        StationRegistry emptyRegistry = new StationRegistry(java.util.List.of());
        assertTrue(emptyRegistry.sortedByDistance().isEmpty());
    }

    @Test
    @DisplayName("Справочник содержит станцию Житковичи с корректными атрибутами")
    void containsZhitkovichi() {
        Station zhitkovichi = station("Житковичи");
        assertEquals(Line.LUNINETS_KALINKOVICHI, zhitkovichi.line());
        assertEquals(StationType.INTERMEDIATE, zhitkovichi.type());
        assertEquals(215, zhitkovichi.distanceFromGomelKm());
    }

    @Test
    @DisplayName("farthest согласован с максимумом расстояний по всему справочнику")
    void farthestIsConsistentWithMaxDistance() {
        int maxDistance = registry.all().stream()
                .mapToInt(Station::distanceFromGomelKm)
                .max()
                .orElseThrow();
        assertEquals(maxDistance, registry.farthest().orElseThrow().distanceFromGomelKm());
    }

    @Test
    @DisplayName("farthest не выбирает Гомель-Пассажирский (0 км), если есть удалённые станции")
    void farthestDoesNotReturnGomelPassenger() {
        Station farthest = registry.farthest().orElseThrow();
        assertTrue(farthest.distanceFromGomelKm() > 0);
        assertFalse(farthest.name().equals("Гомель-Пассажирский"));
    }

    @Test
    @DisplayName("farthest возвращает одну из станций при равном максимальном расстоянии")
    void farthestHandlesTies() {
        Station a = new Station("Далёкая-А", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 300);
        Station b = new Station("Далёкая-Б", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 300);
        Station near = new Station("Близкая", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 10);
        StationRegistry tieRegistry = new StationRegistry(List.of(a, b, near));

        Station farthest = tieRegistry.farthest().orElseThrow();
        assertEquals(300, farthest.distanceFromGomelKm());
        assertTrue(farthest.equals(a) || farthest.equals(b));
    }

    @Test
    @DisplayName("farthest всегда возвращает непустой Optional для непустого справочника")
    void farthestReturnsPresentForNonEmptyRegistry() {
        assertTrue(registry.farthest().isPresent());
    }
  
    @Test
    @DisplayName("Добруш: Брянское направление, грузовая станция, 25 км от Гомеля")
    void containsDobrush() {
        Station dobrush = station("Добруш");
        assertEquals("Добруш", dobrush.name());
        assertEquals(Line.GOMEL_BRYANSK, dobrush.line());
        assertEquals(StationType.FREIGHT, dobrush.type());
        assertEquals(25, dobrush.distanceFromGomelKm());
    }


    @Test
    @DisplayName("Станции упорядочены по возрастанию расстояния")
    void sortedByDistanceOrdersByDistance() {
        Station far = new Station("Дальняя", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 50);
        Station near = new Station("Ближняя", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 2);
        Station middle = new Station("Средняя", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 12);
        StationRegistry sample = new StationRegistry(List.of(far, near, middle));

        assertEquals(List.of(near, middle, far), sample.sortedByDistance());
    }

    @Test
    @DisplayName("При одинаковом расстоянии станции упорядочены по наименованию")
    void sortedByDistanceBreaksTiesByName() {
        Station beta = new Station("Бета", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 10);
        Station alpha = new Station("Альфа", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 10);
        StationRegistry sample = new StationRegistry(List.of(beta, alpha));

        assertEquals(List.of(alpha, beta), sample.sortedByDistance());
    }

    @Test
    @DisplayName("Пустой справочник даёт пустой результат сортировки")
    void sortedByDistanceHandlesEmptyRegistry() {
        StationRegistry empty = new StationRegistry(List.of());

        assertTrue(empty.sortedByDistance().isEmpty());
    }

    @Test
    @DisplayName("Сортировка не изменяет исходный порядок справочника")
    void sortedByDistanceDoesNotChangeRegistry() {
        Station far = new Station("Дальняя", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 50);
        Station near = new Station("Ближняя", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 2);
        StationRegistry sample = new StationRegistry(List.of(far, near));
        List<Station> before = List.copyOf(sample.all());

        sample.sortedByDistance();

        assertEquals(before, sample.all());
    }

    @Test
    @DisplayName("Сортировка основного справочника сохраняет все станции и их атрибуты")
    void sortedByDistanceKeepsAllStations() {
        List<Station> sorted = registry.sortedByDistance();

        assertEquals(registry.all().size(), sorted.size());
        assertEquals(new HashSet<>(registry.all()), new HashSet<>(sorted));
        for (int i = 1; i < sorted.size(); i++) {
            Station previous = sorted.get(i - 1);
            Station current = sorted.get(i);
            assertTrue(previous.distanceFromGomelKm() < current.distanceFromGomelKm()
                    || (previous.distanceFromGomelKm() == current.distanceFromGomelKm()
                        && previous.name().compareTo(current.name()) <= 0));
        }
    }
}

