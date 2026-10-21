package by.bsut.staris.stations;

import java.util.List;

/** Справочник станций Гомельского отделения Белорусской железной дороги. */
public final class StationRegistry {

    private final List<Station> stations = List.of(
            new Station("Гомель-Пассажирский", Line.GOMEL_ZHLOBIN, StationType.PASSENGER, 0),
            new Station("Речица", Line.GOMEL_KALINKOVICHI, StationType.INTERMEDIATE, 44)
            // новые станции добавляются в конец списка
    );

    /** Возвращает все станции справочника. */
    public List<Station> all() {
        return stations;
    }

    /**
     * Возвращает станции заданного типа.
     *
     * @param type тип станции
     * @return список станций; пустой список, если станций такого типа нет
     * @throws IllegalArgumentException если type равен null
     */
    public List<Station> findByType(StationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип станции не может быть null");
        }
        return stations.stream()
                .filter(station -> station.type() == type)
                .toList();
    }

    // новые методы добавляются в конец класса
}