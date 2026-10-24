package by.bsut.staris.stations;

import java.util.List;

/** Справочник станций Гомельского отделения Белорусской железной дороги. */
public final class StationRegistry {

    private final List<Station> stations;

    /** Конструктор по умолчанию — заполняет справочник стандартным набором станций. */
    public StationRegistry() {
        this(List.of(
            new Station("Гомель-Пассажирский", Line.GOMEL_ZHLOBIN, StationType.PASSENGER, 0),
            new Station("Ельск", Line.GOMEL_ELSK, StationType.FREIGHT, 165),
            new Station("Костюковка", Line.GOMEL_ZHLOBIN, StationType.INTERMEDIATE, 12),
            new Station("Гомель-Сортировочный", Line.GOMEL_ZHLOBIN, StationType.MARSHALLING, 2)
        ));
    }

    /** Конструктор для тестов: позволяет создать реестр с произвольным (в т.ч. пустым) списком. */
    public StationRegistry(List<Station> stations) {
        this.stations = List.copyOf(stations);
    }

    public List<Station> all() {
        return stations;
    }

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

    /**
     * Формирует Markdown-таблицу станций в точном формате STATIONS.md.
     * Возвращает только таблицу (шапка + разделитель + строки станций), без завершающего \n.
     */
    public String toMarkdownTable() {
        StringBuilder sb = new StringBuilder();

        // Шапка таблицы — точно как в STATIONS.md
        sb.append("| Станция | Направление | Тип | Расстояние от Гомеля, км |\n");
        // Разделитель — точно как в STATIONS.md (с пробелами вокруг дефисов)
        sb.append("| --- | --- | --- | --- |");

        for (Station station : stations) {
            sb.append("\n| ")
              .append(station.name())
              .append(" | ")
              .append(station.line())
              .append(" | ")
              .append(station.type())
              .append(" | ")
              .append(station.distanceFromGomelKm())
              .append(" |");
        }

        return sb.toString();
    }
}