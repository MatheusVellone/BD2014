package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Usuario;

public class UsuarioDAO extends DAO<Usuario> {

    private static final String createQuery = "INSERT INTO usuario(login, senha, nome, nascimento, descricao, foto, created, modified, servidor) VALUES(?, md5(?), ?, ?, ?, ?, ?, ?, ?) RETURNING id;";
    private static final String readQuery = "SELECT * FROM usuario WHERE id = ?;";
    private static final String updateQuery = "UPDATE usuario SET login = ?, nome = ?, nascimento = ?, descricao = ?, foto = ?, modified = 'now' WHERE id = ?;";
    private static final String updateWithPasswordQuery = "UPDATE usuario SET login = ?, nome = ?, nascimento = ?, descricao = ?, foto = ?, senha = md5(?), modified='now' WHERE id = ?;";
    private static final String deleteQuery = "DELETE FROM usuario WHERE id = ?;";
    private static final String allQuery = "SELECT * FROM usuario;";
    private static final String todosUsuariosQuery = "SELECT * FROM usuario WHERE servidor = 23;";
    private static final String authenticateQuery = "SELECT id, senha, nome, nascimento, descricao, foto FROM usuario WHERE login = ?;";
    private static final String seguirQuery = "INSERT INTO seguidores(id_seguidor, id_segue, created) VALUES(?, ?, 'now')";
    private static final String quemEuSigoQuery = "SELECT * FROM seguidores s INNER JOIN usuario u ON s.id_segue = u.id WHERE id_seguidor = ?";
    private static final String quemMeSegue = "SELECT * FROM seguidores s INNER JOIN usuario u ON s.id_seguidor = u.id WHERE id_segue = ?";
    private static final String paraDeSeguirQuery = "DELETE FROM seguidores WHERE id_seguidor = ? AND id_segue = ?";
    private static final String searchQuery = "SELECT * FROM usuario WHERE LOWER(login )LIKE LOWER(?) OR LOWER(nome) LIKE LOWER(?) OR LOWER(descricao) LIKE LOWER(?)";
    private static final String euSigoCountQuery = "SELECT COUNT(*) AS contador FROM seguidores WHERE id_seguidor = ?;";
    private static final String meSegueCountQuery = "SELECT COUNT(*) AS contador FROM seguidores WHERE id_segue = ?;";
    private static final String limpa_dados = "DELETE FROM comentarios WHERE servidor = ?; DELETE FROM grupo WHERE servidor = ?; DELETE FROM integrantes_grupo WHERE servidor = ?; DELETE FROM likes WHERE servidor = ?; DELETE FROM post WHERE servidor = ?; DELETE FROM seguidores WHERE servidor = ?; DELETE FROM usuario WHERE servidor = ?;";

    public UsuarioDAO(Connection connection) {
        super(connection);
    }

    public void excluiDadosServidor(int servidor) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(limpa_dados)) {
            statement.setInt(1, servidor);
            statement.setInt(2, servidor);
            statement.setInt(3, servidor);
            statement.setInt(4, servidor);
            statement.setInt(5, servidor);
            statement.setInt(6, servidor);
            statement.setInt(7, servidor);
            statement.executeUpdate();
        }catch(SQLException ex){
            throw ex;
        }
    }

    @Override
    public void create(Usuario usuario) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(createQuery);) {
            statement.setString(1, usuario.getLogin());
            if (usuario.getSenha() != null) {
                statement.setString(2, usuario.getSenha());
            } else {
                statement.setString(2, "");
            }
            statement.setString(3, usuario.getNome());
            statement.setDate(4, usuario.getNascimento());
            statement.setString(5, usuario.getDescricao());
            statement.setString(6, usuario.getFoto());
            if (usuario.getData() == null) {
                java.util.Date utilDate = new java.util.Date();
                Timestamp sqlDate = new Timestamp(utilDate.getTime());
                statement.setTimestamp(7, sqlDate);
                statement.setTimestamp(8, sqlDate);
            } else {
                statement.setTimestamp(7, usuario.getData());
                statement.setTimestamp(8, usuario.getData());
            }
            statement.setInt(9, usuario.getServidor());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    @Override
    public Usuario read(Integer id) throws SQLException {
        Usuario usuario = new Usuario();

        try (PreparedStatement statement = connection.prepareStatement(readQuery);) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery();) {
                if (result.next()) {
                    usuario.setId(result.getInt("id"));
                    usuario.setLogin(result.getString("login"));
                    usuario.setNome(result.getString("nome"));
                    usuario.setNascimento(result.getDate("nascimento"));
                    usuario.setDescricao(result.getString("descricao"));
                    usuario.setFoto(result.getString("foto"));
                } else {
                    System.out.println(id);
                    throw new SQLException("Falha ao visualizar: usuário não encontrado.");
                }
            }
        }

        return usuario;
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        String query;

        if (usuario.getSenha() == null) {
            query = updateQuery;
        } else {
            query = updateWithPasswordQuery;
        }
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1, usuario.getLogin());
            statement.setString(2, usuario.getNome());
            statement.setDate(3, usuario.getNascimento());
            statement.setString(4, usuario.getDescricao());
            if (usuario.getFoto() == null) {
                usuario.setFoto(read(usuario.getId()).getFoto());
            }
            statement.setString(5, usuario.getFoto());
            if (usuario.getSenha() == null) {
                statement.setInt(6, usuario.getId());
            } else {
                statement.setString(6, usuario.getSenha());
                statement.setInt(7, usuario.getId());
            }

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Falha ao editar: usuário não encontrado.");
            }
        }
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

    @Override
    public List<Usuario> all() throws SQLException {
        List<Usuario> usuarioList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(allQuery);
                ResultSet result = statement.executeQuery();) {
            while (result.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setLogin(result.getString("login"));
                usuario.setNome(result.getString("nome"));
                usuario.setDescricao(result.getString("descricao"));
                usuario.setFoto(result.getString("foto"));
                usuario.setServidor(result.getInt("servidor"));
                usuario.setNascimento(result.getDate("nascimento"));

                usuarioList.add(usuario);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return usuarioList;
    }
    
    public List<Usuario> todosUsuarios() throws SQLException {
        List<Usuario> usuarioList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(todosUsuariosQuery);
                ResultSet result = statement.executeQuery();) {
            while (result.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setLogin(result.getString("login"));
                usuario.setNome(result.getString("nome"));
                usuario.setDescricao(result.getString("descricao"));
                usuario.setFoto(result.getString("foto"));
                usuario.setServidor(result.getInt("servidor"));
                usuario.setNascimento(result.getDate("nascimento"));

                usuarioList.add(usuario);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return usuarioList;
    }

    public void authenticate(Usuario usuario) throws SQLException, SecurityException {
        try (PreparedStatement statement = connection.prepareStatement(authenticateQuery);) {
            statement.setString(1, usuario.getLogin());

            try (ResultSet result = statement.executeQuery();) {
                if (result.next()) {
                    MessageDigest md5;

                    String senhaForm;
                    String senhaUsuario;

                    try {
                        md5 = MessageDigest.getInstance("MD5");
                        md5.update(usuario.getSenha().getBytes());

                        senhaForm = new BigInteger(1, md5.digest()).toString(16);
                        senhaUsuario = result.getString("senha");

                        if (!senhaForm.equals(senhaUsuario)) {
                            throw new SecurityException("Senha incorreta.");
                        }
                    } catch (NoSuchAlgorithmException ex) {
                        System.err.println(ex.getMessage());
                    }

                    usuario.setId(result.getInt("id"));
                    usuario.setNome(result.getString("nome"));
                    usuario.setNascimento(result.getDate("nascimento"));
                    usuario.setDescricao(result.getString("descricao"));
                    usuario.setFoto(result.getString("foto"));
                } else {
                    throw new SecurityException("Login incorreto.");
                }
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void seguir(int id_seguidor, int id_segue) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(seguirQuery);) {
            statement.setInt(1, id_seguidor);
            statement.setInt(2, id_segue);

            statement.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public List<Integer> segue(int id_seguidor) throws SQLException {
        List<Integer> segue = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(quemEuSigoQuery);) {
            statement.setInt(1, id_seguidor);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                segue.add(rs.getInt("id_segue"));
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return segue;
    }

    public List<Usuario> quemEuSigo(int id_seguidor) throws SQLException {
        List<Usuario> segue = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(quemEuSigoQuery);) {
            statement.setInt(1, id_seguidor);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_segue"));
                usuario.setNome(rs.getString("nome"));
                usuario.setFoto(rs.getString("foto"));

                segue.add(usuario);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return segue;
    }

    public List<Usuario> quemMeSegue(int id) throws SQLException {
        List<Usuario> segue = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(quemMeSegue);) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_seguidor"));
                usuario.setNome(rs.getString("nome"));
                usuario.setFoto(rs.getString("foto"));

                segue.add(usuario);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return segue;
    }

    public List<Usuario> search(String termo_pesquisa) throws SQLException {
        List<Usuario> usuarioList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
            statement.setString(1, "%" + termo_pesquisa + "%");
            statement.setString(2, "%" + termo_pesquisa + "%");
            statement.setString(3, "%" + termo_pesquisa + "%");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(result.getInt("id"));
                usuario.setLogin(result.getString("login"));
                usuario.setNome(result.getString("nome"));
                usuario.setDescricao(result.getString("descricao"));
                usuario.setFoto(result.getString("foto"));

                usuarioList.add(usuario);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return usuarioList;
    }

    public void pararDeSeguir(int id_seguidor, int id_segue) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(paraDeSeguirQuery);) {
            statement.setInt(1, id_seguidor);
            statement.setInt(2, id_segue);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Falha ao parar de seguir: usuário não encontrado.");
            }
        }
    }

    public int quantosEuSigo(int id_seguidor) throws SQLException {
        int ret = -1;
        try (PreparedStatement statement = connection.prepareStatement(euSigoCountQuery);) {
            statement.setInt(1, id_seguidor);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                ret = rs.getInt("contador");
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return ret;
    }

    public int quantosMeSeguem(int id_segue) throws SQLException {
        int ret = -1;
        try (PreparedStatement statement = connection.prepareStatement(meSegueCountQuery);) {
            statement.setInt(1, id_segue);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                ret = rs.getInt("contador");
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return ret;
    }

}
