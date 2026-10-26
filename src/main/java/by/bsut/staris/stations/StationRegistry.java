package by.bsut.staris.stations;

import java.util.List;
import java.util.Optional;

/** Справочник станций Гомельского отделения Белорусской железной дороги. */
public final class StationRegistry {

    private final List<Station> stations;

    /** Создаёт справочник с заданным набором станций. */
    public StationRegistry(List<Station> stations) {
        this.stations = List.copyOf(stations);
    }

    /** Создаёт справочник со стандартным набором станций. */
    public StationRegistry() {
        this.stations = List.of(
            new Station("Гомель-Пассажирский", Line.GOMEL_ZHLOBIN, StationType.PASSENGER, 0),
            new Station("Ельск", Line.GOMEL_ELSK, StationType.FREIGHT, 165),
            new Station("Костюковка", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 12),
            new Station("Гомель-Сортировочный", Line.GOMEL_ZHLOBIN, StationType.MARSHALLING, 2),
            new Station("Рогачёв", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 113)
        );
    }

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
    /** Возвращает станцию, наиболее удалённую от Гомеля, если справочник не пуст. */
    public Optional<Station> farthest() {
        return stations.stream()
                .max(java.util.Comparator.comparingInt(Station::distanceFromGomelKm));
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
