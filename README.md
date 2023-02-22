### Библиотека-обертка для работы с PostgreSQL через JDBC

- Используется Java 8 (Amazon Corretto 1.8.0_362). Сборщик - Apache Maven 3.8.7
- JDBC 4.2
- PostgreSQL driver 42.5.4
- c3p0 0.9.5.5
- JUnit 5.9.2

По-умолчанию тесты при сборке не запускаются, т.к. требуют предварительно настроенной БД PostgreSQL и подключения к ней.

Для сборки с тестами используйте:

    mvn -DskipTests=false clean package