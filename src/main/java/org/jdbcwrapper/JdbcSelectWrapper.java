package org.jdbcwrapper;

import java.sql.ResultSet;
import java.util.Optional;

/**
 * Интерфейс-обертка для выполнения SELECT SQL запросов к СУБД
 */
public interface JdbcSelectWrapper {
    /**
     * Открывает соединение
     */
    void openConnection();

    /**
     * Запрос SELECT к СУБД
     *
     * @param selectQuery - запрос, должен начинаться с SELECT
     * @return объект <code>ResultSet</code>
     */
    Optional<ResultSet> executeSelectQuery(String selectQuery);

    /**
     * Закрывает соединение
     */
    void closeConnection();
}
