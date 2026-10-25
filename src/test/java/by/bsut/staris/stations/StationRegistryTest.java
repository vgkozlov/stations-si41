package by.bsut.staris.stations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("Справочник содержит станцию Буда-Кошелёво с ожидаемыми атрибутами")
    void containsBudaKoshelevo() {
        Station buda = station("Буда-Кошелёво");
        assertEquals(Line.GOMEL_ZHLOBIN, buda.line());
        assertEquals(StationType.INTERMEDIATE, buda.type());
        assertEquals(43, buda.distanceFromGomelKm());
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

    // тесты новых станций и методов добавляются в конец класса
}