package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3307;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "atm";
    
    public static Connection getConnection() throws SQLException {
        //Connector genérico para la BDD
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            // Asegúrate de tener el driver de MySQL agregado en tu proyecto
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }

        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }
}
