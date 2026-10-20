package by.bsut.staris.stations;

import java.util.List;

/** Справочник станций Гомельского отделения Белорусской железной дороги. */
public final class StationRegistry {

    private final List<Station> stations = List.of(
        new Station("Гомель-Пассажирский", Line.GOMEL_ZHLOBIN, StationType.PASSENGER, 0),
        new Station ("Костюковка", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 12),
        new Station ("Словечно", Line.KALINKOVICHI_SLOVECHNO, StationType.FREIGHT, 199)
        // новые станции добавляются в конец списка
    );

    /** Возвращает все станции справочника. */
    public List<Station> all() {
        return stations;
    }

    // новые методы добавляются в конец класса
}
