package by.bsut.staris.stations;

import java.util.List;


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


    // новые методы добавляются в конец класса

       /** Возвращает все станции справочника. */
    public List<Station> getAll() {
    return stations.stream()
            .sorted(java.util.Comparator.comparingInt(
                    Station::distanceFromGomelKm))
            .toList();
}

    /** Ищет станцию по названию. */
    public java.util.Optional<Station> findByName(String name) {
        return stations.stream()
                .filter(station -> station.name().equals(name))
                .findFirst();
    }

    /** Возвращает станции указанной линии. */
    public List<Station> findByLine(Line line) {
        return stations.stream()
                .filter(station -> station.line() == line)
                .toList();
    }

    /** Возвращает станции указанного типа. */
    public List<Station> findByType(StationType type) {
        return stations.stream()
                .filter(station -> station.type() == type)
                .toList();
    }

    /** Возвращает самую удалённую от Гомеля станцию. */
    public java.util.Optional<Station> farthest() {
        return stations.stream()
                .max(java.util.Comparator.comparingInt(
                        Station::distanceFromGomelKm));
    }

}
