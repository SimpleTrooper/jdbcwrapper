package org.jdbcwrapper;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlSelectJdbcWrapperFactory {
    private final static Logger staticLogger = Logger.getLogger("PostgreSqlSelectJdbcWrapperStatic");

    /**
     * Метод, возвращающий экземпляр класса {@code PostgreSqlSelectJdbcWrapper},
     * использующий пул соединений c3p0 к PostgreSQL
     *
     * @param serverName - имя сервера
     * @param port       - номер порта
     * @param database   - имя базы данных
     * @param user       - имя пользователя
     * @param password   - пароль
     * @return - экземпляр класса {@code PostgreSqlSelectJdbcWrapper} с пулом соединений, возвращает null в случае
     * ошибки
     */
    public static PostgreSqlSelectJdbcWrapper getInstanceWithC3P0Pool(String serverName,
                                                                      int port,
                                                                      String database,
                                                                      String user,
                                                                      String password) {
        final int acquireRetryAttempts = 5;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setAcquireRetryAttempts(acquireRetryAttempts);

        try {
            dataSource.setDriverClass("org.postgresql.Driver");
        } catch (PropertyVetoException exception) {
            staticLogger.log(Level.SEVERE, "Exception while setting driver class for c3p0 datasource!", exception);
            return null;
        }

        dataSource.setJdbcUrl("jdbc:postgresql://" + serverName + ":" + port + "/" + database);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return new PostgreSqlSelectJdbcWrapper(dataSource);
    }

    /**
     * Метод, возвращающий экземпляр класса {@code PostgreSqlSelectJdbcWrapper} с {@code PGSimpleDataSource}
     *
     * @param serverName - имя сервера
     * @param port       - номер порта
     * @param database   - имя базы данных
     * @param user       - имя пользователя
     * @param password   - пароль
     * @return - экземпляр класса {@code PostgreSqlSelectJdbcWrapper} с {@code PGSimpleDataSource}
     */
    public static PostgreSqlSelectJdbcWrapper getInstanceWithSimpleDatasource(String serverName,
                                                                              int port,
                                                                              String database,
                                                                              String user,
                                                                              String password) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{serverName});
        dataSource.setPortNumbers(new int[]{port});
        dataSource.setDatabaseName(database);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return new PostgreSqlSelectJdbcWrapper(dataSource);
    }
}
