package org.jdbcwrapper;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестовый класс для PostgreSqlSelectJdbcWrapper
 */
class PostgreSqlSelectJdbcWrapperTest {

    String sqlSelectQuery = "SELECT * FROM test_table";
    String serverName = "localhost";
    int port = 5433;
    String databaseName = "newdb";
    String user = "jdbc";
    String password = "jdbc";


    /**
     * Тест для проверки количества записей, возвращаемых в ResultSet. Необходимо предварительно настроить БД и
     * установить в тесте необходимые параметры подключения, запрос и количество ожидаемых записей.
     */
    @Test
    void executeSelectQueryWithC3P0() throws SQLException {
        int expectedSize = 3;
        PostgreSqlSelectJdbcWrapper postgreSqlSelectJdbcWrapper = PostgreSqlSelectJdbcWrapperFactory
                .getInstanceWithC3P0Pool(serverName, port, databaseName, user, password);

        assertNotNull(postgreSqlSelectJdbcWrapper);
        postgreSqlSelectJdbcWrapper.openConnection();
        Optional<ResultSet> actual = postgreSqlSelectJdbcWrapper.executeSelectQuery(sqlSelectQuery);

        assertTrue(actual.isPresent());
        int size = 0;
        while (actual.get().next()) {
            size++;
        }
        postgreSqlSelectJdbcWrapper.closeConnection();

        assertEquals(expectedSize, size);
    }

    @Test
    void shouldReturnEmptyOptionalWhenExecuteQueryWithBadConnectionForC3P0() {
        String serverName = "localhostwithbadName";
        PostgreSqlSelectJdbcWrapper postgreSqlSelectJdbcWrapper = PostgreSqlSelectJdbcWrapperFactory
                .getInstanceWithC3P0Pool(serverName, port, databaseName, user, password);

        assertNotNull(postgreSqlSelectJdbcWrapper);
        postgreSqlSelectJdbcWrapper.openConnection();
        Optional<ResultSet> actual = postgreSqlSelectJdbcWrapper.executeSelectQuery(sqlSelectQuery);
        postgreSqlSelectJdbcWrapper.closeConnection();

        assertFalse(actual.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenExecuteQueryWithNoConnectionForC3P0() {
        PostgreSqlSelectJdbcWrapper postgreSqlSelectJdbcWrapper = PostgreSqlSelectJdbcWrapperFactory
                .getInstanceWithC3P0Pool(serverName, port, databaseName, user, password);

        assertNotNull(postgreSqlSelectJdbcWrapper);
        Optional<ResultSet> actual = postgreSqlSelectJdbcWrapper.executeSelectQuery(sqlSelectQuery);

        assertFalse(actual.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenExecuteNotASelectQueryForC3P0() {
        String sqlQuery = "DELETE FROM test_table";
        PostgreSqlSelectJdbcWrapper postgreSqlSelectJdbcWrapper = PostgreSqlSelectJdbcWrapperFactory
                .getInstanceWithC3P0Pool(serverName, port, databaseName, user, password);

        assertNotNull(postgreSqlSelectJdbcWrapper);
        postgreSqlSelectJdbcWrapper.openConnection();
        Optional<ResultSet> actual = postgreSqlSelectJdbcWrapper.executeSelectQuery(sqlQuery);
        postgreSqlSelectJdbcWrapper.closeConnection();

        assertFalse(actual.isPresent());
    }

    @Test
    void shouldReturnEmptyOptionalWhenExecuteBadQueryForC3P0() {
        String sqlQuery = "SELECT FROM test_table bad syntax";
        PostgreSqlSelectJdbcWrapper postgreSqlSelectJdbcWrapper = PostgreSqlSelectJdbcWrapperFactory
                .getInstanceWithC3P0Pool(serverName, port, databaseName, user, password);

        assertNotNull(postgreSqlSelectJdbcWrapper);
        postgreSqlSelectJdbcWrapper.openConnection();
        Optional<ResultSet> actual = postgreSqlSelectJdbcWrapper.executeSelectQuery(sqlQuery);
        postgreSqlSelectJdbcWrapper.closeConnection();

        assertFalse(actual.isPresent());
    }
}