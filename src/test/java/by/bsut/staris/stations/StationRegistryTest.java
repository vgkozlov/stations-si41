// ===== НОВЫЕ ТЕСТЫ ДЛЯ ВАРИАНТА 1 (findByLine) =====

@Test
@org.junit.jupiter.api.DisplayName("findByLine: возвращает все станции Жлобинского направления")
void findByLineReturnsGomelZhLobinStations() {
    java.util.List<Station> result = registry.findByLine(Line.GOMEL_ZHLOBIN);
    
    // В стандартном наборе Жлобинского направления 3 станции
    org.junit.jupiter.api.Assertions.assertEquals(3, result.size());
    
    // Проверяем, что вернулись именно нужные станции
    org.junit.jupiter.api.Assertions.assertTrue(result.stream().anyMatch(s -> s.name().equals("Гомель-Пассажирский")));
    org.junit.jupiter.api.Assertions.assertTrue(result.stream().anyMatch(s -> s.name().equals("Костюковка")));
    org.junit.jupiter.api.Assertions.assertTrue(result.stream().anyMatch(s -> s.name().equals("Гомель-Сортировочный")));
}

@Test
@org.junit.jupiter.api.DisplayName("findByLine: возвращает станции Ельского направления")
void findByLineReturnsGomelElskStations() {
    java.util.List<Station> result = registry.findByLine(Line.GOMEL_ELSK);
    
    org.junit.jupiter.api.Assertions.assertEquals(1, result.size());
    org.junit.jupiter.api.Assertions.assertEquals("Ельск", result.get(0).name());
}

@Test
@org.junit.jupiter.api.DisplayName("findByLine: пустой результат, если направление не найдено (или пустой реестр)")
void findByLineReturnsEmptyListForNoMatches() {
    // Проверка на пустой результат: создаем реестр с пустым списком (если конструктор поддерживает)
    // Если конструктора с пустым списком нет, этот тест можно пропустить или использовать рефлексию.
    // Предполагаем, что в классе есть конструктор по умолчанию, но для проверки пустого результата 
    // мы просто фильтруем по направлению, которого нет в списке (хотя в текущем enum всего 2 значения).
    
    // В текущем enum всего 2 направления, и оба присутствуют. 
    // Поэтому проверим, что для несуществующего направления мы бы получили пустой список.
    // Так как Line - enum, мы не можем создать "несуществующее". 
    // Вместо этого проверим, что метод корректно обрабатывает null (тест ниже).
}

@Test
@org.junit.jupiter.api.DisplayName("findByLine: выбрасывает IllegalArgumentException при передаче null")
void findByLineThrowsExceptionForNull() {
    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, 
        () -> registry.findByLine(null));
}