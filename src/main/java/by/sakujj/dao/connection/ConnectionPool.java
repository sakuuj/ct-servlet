package by.sakujj.dao.connection;

import java.sql.Connection;

public interface ConnectionPool extends AutoCloseable {
    Connection getConnection();
}
