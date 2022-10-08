import org.h2.tools.Server;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";

    public static void setup() throws SQLException {
        Server.createTcpServer().start();
        DataSource dataSource = new JdbcDataSource();
        ((JdbcDataSource) dataSource).setURL(DB_URL);
        ((JdbcDataSource) dataSource).setUser("sa");
        ((JdbcDataSource) dataSource).setPassword("");

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE securities ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "stock_code VARCHAR(10),"
                    + "type VARCHAR(10),"
                    + "strike_price DECIMAL(10, 2),"
                    + "expiration_date DATE"
                    + ")");
            // Insert sample data
            stmt.execute("INSERT INTO securities (stock_code, type, strike_price, expiration_date) VALUES "
                    + "('AAPL', 'stock', NULL, NULL),"
                    + "('AAPL_C', 'call', 150.00, '2024-01-01'),"
                    + "('AAPL_P', 'put', 140.00, '2024-01-01')");
        }
    }
}