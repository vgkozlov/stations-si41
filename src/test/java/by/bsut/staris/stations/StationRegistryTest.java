package by.bsut.staris.stations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("Справочник содержит станцию Костюковка с 12-и километровым расстоянием")
    void containsKostyukovka() {
        Station kostyukovka = station("Костюковка");
        assertEquals(StationType.INTERMEDIATE, kostyukovka.type());
        assertEquals(12, kostyukovka.distanceFromGomelKm());
    }

    @Test
    @DisplayName("Справочник содержит станцию Словечно с расстояние 199км")
    void containsSlovechno() {
        Station slovechno = station("Словечно");
        assertEquals(StationType.FREIGHT, slovechno.type());
        assertEquals(Line.KALINKOVICHI_SLOVECHNO, slovechno.line());
        assertEquals(199, slovechno.distanceFromGomelKm());
    }

    // тесты новых станций и методов добавляются в конец класса
}
