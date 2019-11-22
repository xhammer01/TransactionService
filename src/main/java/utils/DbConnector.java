package utils;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DbConnector {

    private Logger logger = LoggerFactory.getLogger(DbConnector.class);

    public JdbcPooledConnectionSource connect() {
        JdbcPooledConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcPooledConnectionSource("jdbc:h2:mem:bankDb");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("cannot connect to db");
        }
        return connectionSource;
    }
}
