package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {

    private static ConnectionFactory instance = null;

    private ConnectionFactory() {
    }

    private String dbHost;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }

        return instance;
    }

    public void readProperties() {
        Properties properties = new Properties();

        try {
            // Carrega o arquivo com as propriedades do banco de dados.
            String path = "jdbc/datasource.properties";
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(path);

            properties.load(input);

            dbHost = properties.getProperty("host");
            dbPort = properties.getProperty("port");
            dbName = properties.getProperty("name");
            dbUser = properties.getProperty("user");
            dbPassword = properties.getProperty("password");
        } catch (IOException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        Connection connection = null;

        try {
            // Registra o driver na JVM.
            Class.forName("org.postgresql.Driver");

            // Lê as propriedades do banco de dados.
            readProperties();

            // Forma a URL de conexão.
            String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

            // Estabelece uma conexão.
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return connection;
    }
}
