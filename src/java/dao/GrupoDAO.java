package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Grupo;

public class GrupoDAO extends DAO<Grupo> {

    private static final String createQuery = "INSERT INTO grupo(id_admin, nome) VALUES(?, ?);";
    private static final String updateQuery = "UPDATE grupo SET nome = ? WHERE id = ?;";
    private static final String readQuery = "SELECT * FROM grupo WHERE id = ?;";
    private static final String deleteQuery = "DELETE FROM grupo WHERE id = ?;";
    private static final String allQuery = "SELECT * FROM grupo;";
    private static final String allQueryAdmin = "SELECT * FROM grupo WHERE id_admin = ?;";
    private static final String allQueryIntegrante = "SELECT * FROM integrantes_grupo WHERE id_usuario = ?;";
    private static final String novoIntegranteQuery = "INSERT INTO integrantes_grupo(id_grupo, id_usuario) VALUES(?, ?)";
    private static final String SairGrupoQuery = "DELETE FROM integrantes_grupo WHERE id_usuario = ? AND id_grupo = ?";
    private static final String searchQuery = "SELECT * FROM grupo WHERE LOWER(nome) LIKE LOWER(?)";

    public GrupoDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Grupo grupo) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);) {
            statement.setInt(1, grupo.getId_admin());
            statement.setString(2, grupo.getNome());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                novo_integrante(Integer.parseInt(rs.getString("id")), grupo.getId_admin());
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    @Override
    public void update(Grupo grupo) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(updateQuery);) {
            statement.setString(1, grupo.getNome());
            statement.setInt(2, grupo.getId());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Falha ao editar: usuário não encontrado.");
            }
        }
    }

    @Override
    public Grupo read(Integer id) throws SQLException {
        Grupo grupo = new Grupo();

        try (PreparedStatement statement = connection.prepareStatement(readQuery);) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery();) {
                if (result.next()) {
                    grupo.setId(result.getInt("id"));
                    grupo.setNome(result.getString("nome"));
                    grupo.setId_admin(result.getInt("id_admin"));
                } else {
                    throw new SQLException("Falha ao visualizar: grupo não encontrado.");
                }
            }
        }

        return grupo;
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery);) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Falha ao excluir: usuário não encontrado.");
            }
        }
    }
    
    public void sairDoGrupo(Integer id_usuario, Integer id_grupo) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SairGrupoQuery);) {
            statement.setInt(1, id_usuario);
            statement.setInt(2, id_grupo);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Falha ao excluir: usuário não encontrado.");
            }
        }
    }

    @Override
    public List<Grupo> all() throws SQLException {
        List<Grupo> grupoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(allQuery);
                ResultSet result = statement.executeQuery();) {
            while (result.next()) {
                Grupo grupo = new Grupo();
                grupo.setId(result.getInt("id"));
                grupo.setId_admin(result.getInt("id_admin"));
                grupo.setNome(result.getString("nome"));

                grupoList.add(grupo);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return grupoList;
    }
    
    public List<Grupo> allAdmin(Integer id) throws SQLException {
        List<Grupo> grupoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(allQueryAdmin)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Grupo grupo = new Grupo();
                grupo.setId(result.getInt("id"));
                grupo.setId_admin(result.getInt("id_admin"));
                grupo.setNome(result.getString("nome"));

                grupoList.add(grupo);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return grupoList;
    }
    
    public List<Grupo> allIntegrante(Integer id) throws SQLException {
        List<Grupo> grupoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(allQueryIntegrante)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Grupo grupo = read(result.getInt("id_grupo"));

                grupoList.add(grupo);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return grupoList;
    }

    public void novo_integrante(Integer id_grupo, Integer id_usuario) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(novoIntegranteQuery);) {
            statement.setInt(1, id_grupo);
            statement.setInt(2, id_usuario);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Falha ao adicionar novo integrante.");
            }
        }
    }
    
    public List<Grupo> search(String termo_pesquisa) throws SQLException {
        List<Grupo> grupoList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
            statement.setString(1, "%"+termo_pesquisa+"%");
            ResultSet result = statement.executeQuery();
            
            while(result.next()){
                Grupo grupo = new Grupo();
                grupo.setId(result.getInt("id"));
                grupo.setId_admin(result.getInt("id_admin"));
                grupo.setNome(result.getString("nome"));

                grupoList.add(grupo);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return grupoList;
    }
}
