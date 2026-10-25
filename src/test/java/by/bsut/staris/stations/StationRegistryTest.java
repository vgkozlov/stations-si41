package by.bsut.staris.stations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationRegistryTest {

    private final StationRegistry registry = new StationRegistry();

    @Test
    @DisplayName("Реестр содержит станции")
    void registryContainsStations() {
        assertTrue(registry.getAll().size() > 0);
    }

    @Test
    @DisplayName("Можно найти станцию по названию")
    void findsStationByName() {
        assertTrue(registry.findByName("Ельск").isPresent());
    }

    @Test
    @DisplayName("Неизвестная станция не найдена")
    void unknownStationIsNotFound() {
        assertTrue(registry.findByName("Несуществующая станция").isEmpty());
    }

    @Test
    @DisplayName("Можно получить станции по линии")
    void findsStationsByLine() {
        assertTrue(registry.findByLine(Line.GOMEL_ZHLOBIN).size() > 0);
    }

    @Test
    @DisplayName("Можно получить станции по типу")
    void findsStationsByType() {
        assertTrue(registry.findByType(StationType.INTERMEDIATE).size() > 0);
    }

    @Test
    @DisplayName("Станции отсортированы по расстоянию")
    void stationsAreSortedByDistance() {
        List<Station> stations = registry.getAll();

        for (int i = 1; i < stations.size(); i++) {
            assertTrue(
                    stations.get(i - 1).distanceFromGomelKm()
                            <= stations.get(i).distanceFromGomelKm()
            );
        }
    }

    @Test
    @DisplayName("Реестр не содержит дубликатов названий")
    void registryHasUniqueStationNames() {
        List<Station> stations = registry.getAll();

        long uniqueNames = stations.stream()
                .map(Station::name)
                .distinct()
                .count();

        assertEquals(stations.size(), uniqueNames);
    }

    @Test
    @DisplayName("Нельзя создать станцию с отрицательным расстоянием")
    void rejectsNegativeDistance() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Station(
                        "Тестовая станция",
                        Line.GOMEL_ZHLOBIN,
                        StationType.INTERMEDIATE,
                        -1
                )
        );
    }

    @Test
    @DisplayName("Нельзя создать станцию с пустым названием")
    void rejectsBlankName() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Station(
                        " ",
                        Line.GOMEL_ZHLOBIN,
                        StationType.INTERMEDIATE,
                        10
                )
        );
    }

    @Test
    @DisplayName("Можно найти станцию Гомель-Пассажирский")
    void findsGomelPassengerStation() {
        assertTrue(
                registry.findByName("Гомель-Пассажирский").isPresent()
        );
    }

    @Test
    @DisplayName("Возвращает самую удалённую станцию — Ельск")
    void farthestReturnsMostDistantStation() {
        assertTrue(registry.farthest().isPresent());
        assertEquals("Ельск", registry.farthest().orElseThrow().name());
    }

    @Test
    @DisplayName("Для пустого справочника возвращает пустой Optional")
    void farthestReturnsEmptyForEmptyRegistry() {
        StationRegistry emptyRegistry = new StationRegistry(List.of());

        assertTrue(emptyRegistry.farthest().isEmpty());
    }

    @Test
    @DisplayName("Для справочника с одной станцией возвращает эту станцию")
    void farthestReturnsSingleStation() {
        Station station = new Station(
                "Тестовая станция",
                Line.GOMEL_ZHLOBIN,
                StationType.INTERMEDIATE,
                50
        );

        StationRegistry singleRegistry = new StationRegistry(List.of(station));

        assertEquals(station, singleRegistry.farthest().orElseThrow());
    }
}
