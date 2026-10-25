package by.bsut.staris.stations;

public record Station(String name, Line line, StationType type, int distanceFromGomelKm) {

    public Station {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название станции не может быть пустым");
        }
        if (line == null) {
            throw new IllegalArgumentException("Направление не может быть null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Тип станции не может быть null");
        }
        if (distanceFromGomelKm < 0) {
            throw new IllegalArgumentException("Расстояние не может быть отрицательным");
        }
    }
}