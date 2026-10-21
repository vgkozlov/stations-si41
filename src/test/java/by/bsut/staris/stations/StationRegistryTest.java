package by.bsut.staris.stations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        @Test
    @DisplayName("findByType возвращает станции заданного типа")
    void findByTypeReturnsMatchingStations() {
        List<Station> passengerStations = registry.findByType(StationType.PASSENGER);
        assertTrue(passengerStations.stream().anyMatch(s -> s.name().equals("Гомель-Пассажирский")));
    }

    @Test
    @DisplayName("findByType возвращает пустой список, если станций такого типа нет")
    void findByTypeReturnsEmptyListForUnknownType() {
        assertTrue(registry.findByType(StationType.MARSHALLING).isEmpty());
    }

    @Test
    @DisplayName("findByType выбрасывает IllegalArgumentException для null")
    void findByTypeThrowsForNull() {
        assertThrows(IllegalArgumentException.class, () -> registry.findByType(null));
    }
    // тесты новых станций и методов добавляются в конец класса
}
