package by.bsut.staris.stations;

import java.util.List;

/** Справочник станций Гомельского отделения Белорусской железной дороги. */
public final class StationRegistry {

    private final List<Station> stations = List.of(
        new Station("Гомель-Пассажирский", Line.GOMEL_ZHLOBIN, StationType.PASSENGER, 0),
        new Station("Ельск", Line.GOMEL_ELSK, StationType.FREIGHT, 165),
        new Station("Костюковка", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 12),
        new Station("Гомель-Сортировочный", Line.GOMEL_ZHLOBIN, StationType.MARSHALLING, 2),
        new Station("Буда-Кошелёво", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 43)
        // новые станции добавляются в конец списка
    );

    /** Возвращает все станции справочника. */
    public List<Station> all() {
        return stations;
    }

    // новые методы добавляются в конец класса

    /** Возвращает модуль разности расстояний для станций одного направления. */
    public int distanceBetween(String a, String b) {
        Station first = stations.stream()
                .filter(station -> station.name().equals(a))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестная станция: " + a));
        Station second = stations.stream()
                .filter(station -> station.name().equals(b))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестная станция: " + b));

        if (first.line() != second.line()) {
            throw new IllegalArgumentException("Станции находятся на разных направлениях");
        }

        return Math.abs(first.distanceFromGomelKm() - second.distanceFromGomelKm());
    }
}