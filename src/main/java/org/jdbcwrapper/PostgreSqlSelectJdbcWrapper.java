package org.jdbcwrapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс-обертка для выполнения запросов SELECT к PostgreSQL
 */
public class PostgreSqlSelectJdbcWrapper implements JdbcSelectWrapper {
    private final Logger logger = Logger.getLogger("PostgreSqlSelectJdbcWrapper");
    private Connection connection;
    private final List<ResultSet> openedResultSets = new ArrayList<>();
    private final List<PreparedStatement> openedPreparedStatements = new ArrayList<>();
    private final DataSource dataSource;

    protected PostgreSqlSelectJdbcWrapper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Открывает новое соединение. Закрывает предыдущее соединение, если оно открыто
     */
    @Override
    public void openConnection() {
        try {
            openConnectionUnhandled();
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Exception while opening connection!", exception);
        }
    }

    private void openConnectionUnhandled() throws SQLException {
        closeResource(connection);
        if (dataSource == null) {
            throw new SQLException("Datasource is not set!");
        }
        connection = dataSource.getConnection();
    }

    /**
     * Закрывает текущее соединение и открытые ресурсы (ResultSet, PreparedStatement)
     */
    @Override
    public void closeConnection() {
        closeResources();
        closeResource(connection);
    }

    /**
     * Закрывает все открытые экземпляры <code>ResultSet</code> и <code>PreparedStatement</code>
     */
    private void closeResources() {
        closeResources(openedResultSets);
        closeResources(openedPreparedStatements);
    }

    private void closeResources(List<? extends AutoCloseable> resources) {
        Iterator<? extends AutoCloseable> iterator = resources.iterator();
        while (iterator.hasNext()) {
            AutoCloseable next = iterator.next();
            closeResource(next);
            iterator.remove();
        }
    }

    private void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception exception) {
                logger.log(Level.WARNING, "Exception while closing resource!", exception);
            }
        }
    }

    /**
     * Выполняет SQL SELECT запрос к PostgreSQL
     *
     * @param selectQuery - запрос, должен начинаться с SELECT
     * @return экземпляр <code>Optional<ResultSet></code>, в случае ошибки возвращает <code>Optional.empty()</code>
     */
    @Override
    public Optional<ResultSet> executeSelectQuery(String selectQuery) {
        try {
            return Optional.of(executeSelectQueryUnhandled(selectQuery));
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Exception while executing select query!", exception);
            return Optional.empty();
        }
    }

    private ResultSet executeSelectQueryUnhandled(String selectQuery) throws SQLException {
        if (!isSelectQuery(selectQuery)) {
            throw new SQLException("Method is supporting only SELECT queries! Query: " + "'" + selectQuery + "'");
        }
        if (connection == null) {
            throw new SQLException("Connection is not opened!");
        }

        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        openedPreparedStatements.add(preparedStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        openedResultSets.add(resultSet);
        return resultSet;
    }

    private boolean isSelectQuery(String query) {
        return query != null && query.toLowerCase().startsWith("select");
    }
}
