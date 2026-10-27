package by.bsut.staris.stations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @Test
 
    // тесты новых станций и методов добавляются в конец класса

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
        assertEquals("Ельск", registry.farthest().orElseThrow().name());
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

}
