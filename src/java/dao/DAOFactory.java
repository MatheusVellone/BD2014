package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import jdbc.ConnectionFactory;

public class DAOFactory implements AutoCloseable {

    private Connection connection = null;

    public DAOFactory() {
        connection = ConnectionFactory.getInstance().getConnection();
    }

    public void beginTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public Savepoint createSavepoint(String name) throws SQLException {
        return connection.setSavepoint(name);
    }

    public void commitTransaction() throws SQLException {
        connection.commit();
    }

    public void rollbackTransaction() throws SQLException {
        connection.rollback();
    }

    public void rollbackTransactionTo(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    public void endTransaction() throws SQLException {
        connection.setAutoCommit(true);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO(connection);
    }

    public PostDAO getPostDAO() {
        return new PostDAO(connection);
    }
    
    public GrupoDAO getGrupoDAO() {
        return new GrupoDAO(connection);
    }

    @Override
    public void close() throws SQLException {
        closeConnection();
    }
}
