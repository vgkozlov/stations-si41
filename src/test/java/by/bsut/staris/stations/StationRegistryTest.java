package by.bsut.staris.stations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Справочник содержит станцию Василевичи с корректными атрибутами")
    void containsVasilevichi() {
        Station vasilevichi = station("Василевичи");
        assertEquals(StationType.PASSENGER, vasilevichi.type());
        assertEquals(89, vasilevichi.distanceFromGomelKm());
        assertEquals(Line.GOMEL_LUNINETS, vasilevichi.line());
    }

    @Test
    @DisplayName("Возвращает отсортированный список станций, начинающихся с заданного префикса")
    void namesStartingWith_findsStations() {
        java.util.List<String> result = registry.namesStartingWith("го");
        assertEquals(java.util.List.of("Гомель-Пассажирский"), result);
    }

    @Test
    @DisplayName("Возвращает пустой список, если совпадений нет")
    void namesStartingWith_returnsEmpty() {
        java.util.List<String> result = registry.namesStartingWith("яблоко");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Выбрасывает исключение при передаче null")
    void namesStartingWith_throwsExceptionOnNull() {
        assertThrows(IllegalArgumentException.class, () -> registry.namesStartingWith(null));
    }
    // тесты новых станций и методов добавляются в конец класса
}
