package by.bsut.staris.stations;

/**
 * Железнодорожная станция.
 *
 * @param name                 наименование станции
 * @param line                 железнодорожное направление
 * @param type                 тип станции
 * @param distanceFromGomelKm  расстояние от станции Гомель-Пассажирский по железной дороге, км
 */
public record Station(String name, Line line, StationType type, int distanceFromGomelKm) {
}
