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
            new Station("Речица", Line.GOMEL_KALINKOVICHI, StationType.INTERMEDIATE, 44),
            new Station("Гомель-Сортировочный", Line.GOMEL_ZHLOBIN, StationType.MARSHALLING, 2),
            new Station("Рогачёв", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 113),
            new Station("Новобелицкая", Line.GOMEL_HORNOSTAIVKA, StationType.INTERMEDIATE, 49),
            new Station ("Словечно", Line.KALINKOVICHI_SLOVECHNO, StationType.FREIGHT, 199)
            // новые станции добавляются в конец списка
        );
    }


    /** Возвращает все станции справочника. */
    public List<Station> all() {
        return stations;
    }

    /** Возвращает станцию, наиболее удалённую от Гомеля, если справочник не пуст. */
    public Optional<Station> farthest() {
        return stations.stream()
                .max(java.util.Comparator.comparingInt(Station::distanceFromGomelKm));
    }

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

    /** Возвращает станции заданного типа. */
    public List<Station> findByType(StationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип станции не может быть null");
        }
        return stations.stream()
                .filter(station -> station.type() == type)
                .toList();
    }
     public java.util.Optional<Station> findByName(String name) {
        if (name == null) {
            return java.util.Optional.empty();
        }
        String searchName = name.trim();
        return stations.stream()
                .filter(station -> station.name().equalsIgnoreCase(searchName))
                .findFirst();
    }

     /** Возвращает станции заданного направления. */
    public List<Station> findByLine(Line line) {
        if (line == null) {
            throw new IllegalArgumentException("Направление не может быть null");
        }
        return stations.stream()
                .filter(station -> station.line() == line)
                .toList();
    }
}
