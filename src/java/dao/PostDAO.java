package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Comentario;
import model.Like;
import model.Post;
import outros.Outros;

public class PostDAO extends DAO<Post> {

    private static final String createQuery = "INSERT INTO post(id_autor, titulo, conteudo, created, modified) VALUES(?, ?, ?, 'now', 'now');";
    private static final String readQuery = "SELECT * FROM post WHERE id = ?;";
    private static final String autorQuery = "SELECT * FROM post WHERE id_autor = ?;";
    private static final String updateQuery = "UPDATE post SET titulo = ?, conteudo = ?, modified = 'now' WHERE id = ?;";
    private static final String deleteQuery = "DELETE FROM post WHERE id = ?;";
    private static final String allQuery = "SELECT * FROM post;";
    private static final String allTemaQuery = "SELECT * FROM post WHERE LOWER(conteudo) LIKE LOWER(?);";
    private static final String addTemaQuery = "INSERT INTO tema_post(post_id, tema) VALUES (?, ?);";
    private static final String postFollowQuery = "SELECT * FROM post p INNER JOIN seguidores s ON p.id_autor = s.id_segue WHERE s.id_seguidor = ? ORDER BY p.id DESC;";
    private static final String searchQuery = "SELECT * FROM post WHERE LOWER(titulo) LIKE LOWER(?) OR LOWER(conteudo) LIKE LOWER(?);";
    private static final String repostQuery = "INSERT INTO post(id_autor, republicacao, id_repub, created, modified) VALUES(?, true, ?, 'now', 'now');";

    private static final String likeCount = "SELECT COUNT(*) AS contador FROM likes WHERE post_id = ? AND like_sit = 1;";
    private static final String dislikeCount = "SELECT COUNT(*) AS contador FROM likes WHERE post_id = ? AND like_sit = -1;";
    private static final String situacaoLike = "SELECT like_sit AS contador FROM likes WHERE usuario_id = ? AND post_id = ?;";

    private static final String colocaLike = "INSERT INTO likes(usuario_id, post_id, like_sit, created) VALUES(?, ?, 1, 'now');";
    private static final String colocaDisLike = "INSERT INTO likes(usuario_id, post_id, like_sit, created) VALUES(?, ?, -1, 'now');";
    private static final String tiraLike = "DELETE FROM likes WHERE usuario_id = ? AND post_id = ?;";

    private static final String comentar = "INSERT INTO comentarios(id_post, comentario, dono, created, modified) VALUES(?, ?, ?, 'now', 'now')";
    private static final String comentarios = "SELECT * FROM comentarios WHERE id_post = ?";

    public PostDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Post post) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(createQuery);) {
            statement.setInt(1, post.getId_autor());
            statement.setString(2, post.getTitulo());
            statement.setString(3, post.getConteudo());

            statement.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    @Override
    public Post read(Integer id) throws SQLException {
        Post post = new Post();

        try (PreparedStatement statement = connection.prepareStatement(readQuery);) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery();) {
                if (result.next()) {
                    post.setId(result.getInt("id"));
                    post.setId_autor(result.getInt("id_autor"));
                    post.setTitulo(result.getString("titulo"));
                    post.setConteudo(result.getString("conteudo"));
                } else {
                    throw new SQLException("Falha ao atualizar: post não encontrado.");
                }
            }
        }

        return post;
    }

    @Override
    public void update(Post post) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(updateQuery);) {
            statement.setString(1, post.getTitulo());
            statement.setString(2, post.getConteudo());
            statement.setInt(3, post.getId());

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
    public List<Post> all() throws SQLException {
        List<Post> postList = new ArrayList<>();
        Outros tag = new Outros();

        try (PreparedStatement statement = connection.prepareStatement(allQuery);
                ResultSet result = statement.executeQuery();) {
            while (result.next()) {
                Post post = new Post();
                if (result.getBoolean("republicacao")) {
                    post = read(result.getInt("id_repub"));
                    post.setConteudo(tag.arruma_tags(post.getConteudo()));
                } else {
                    post.setTitulo(result.getString("titulo"));
                    post.setConteudo(tag.arruma_tags(result.getString("conteudo")));
                }
                post.setId(result.getInt("id"));
                post.setRepublicacao(result.getBoolean("republicacao"));
                post.setId_repub(result.getInt("id_repub"));
                postList.add(post);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return postList;
    }

    public List<Post> all(String tema) throws SQLException {
        List<Post> postList = new ArrayList<>();
        Outros tag = new Outros();

        try (PreparedStatement statement = connection.prepareStatement(allTemaQuery);) {
            statement.setString(1, "%" + tema + "%");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Post post = new Post();
                if (result.getBoolean("republicacao")) {
                    post = read(result.getInt("id_repub"));
                    post.setConteudo(tag.arruma_tags(post.getConteudo()));
                } else {
                    post.setTitulo(result.getString("titulo"));
                    post.setConteudo(tag.arruma_tags(result.getString("conteudo")));
                }
                post.setId(result.getInt("id"));
                post.setRepublicacao(result.getBoolean("republicacao"));
                post.setId_repub(result.getInt("id_repub"));
                postList.add(post);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return postList;
    }

    public List<Post> postsAutor(Integer id) throws SQLException {
        List<Post> postList = new ArrayList<>();
        Outros tag = new Outros();

        try (PreparedStatement statement = connection.prepareStatement(autorQuery);) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Post post = new Post();
                if (result.getBoolean("republicacao")) {
                    post = read(result.getInt("id_repub"));
                    post.setConteudo(tag.arruma_tags(post.getConteudo()));
                } else {
                    post.setTitulo(result.getString("titulo"));
                    post.setConteudo(tag.arruma_tags(result.getString("conteudo")));
                }
                post.setId(result.getInt("id"));
                post.setRepublicacao(result.getBoolean("republicacao"));
                post.setId_repub(result.getInt("id_repub"));
                postList.add(post);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return postList;
    }

    public void relaciona_post_tema(int id, String tema) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(addTemaQuery);) {
            statement.setInt(1, id);
            statement.setString(2, tema);

            statement.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public List<Post> seguidores_posts(int id_pessoa) throws SQLException {
        List<Post> postList = new ArrayList<>();
        Outros tag = new Outros();

        try (PreparedStatement statement = connection.prepareStatement(postFollowQuery);) {
            statement.setInt(1, id_pessoa);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Post post = new Post();
                if (result.getBoolean("republicacao")) {
                    post = read(result.getInt("id_repub"));
                    post.setConteudo(tag.arruma_tags(post.getConteudo()));
                } else {
                    post.setTitulo(result.getString("titulo"));
                    post.setConteudo(tag.arruma_tags(result.getString("conteudo")));
                }
                post.setId(result.getInt("id"));
                post.setRepublicacao(result.getBoolean("republicacao"));
                post.setId_repub(result.getInt("id_repub"));
                postList.add(post);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return postList;
    }

    public List<Post> search(String termo_pesquisa) throws SQLException {
        List<Post> postList = new ArrayList<>();
        Outros tag = new Outros();

        try (PreparedStatement statement = connection.prepareStatement(searchQuery)) {
            statement.setString(1, "%" + termo_pesquisa + "%");
            statement.setString(2, "%" + termo_pesquisa + "%");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Post post = new Post();
                if (result.getBoolean("republicacao")) {
                    post = read(result.getInt("id_repub"));
                    post.setConteudo(tag.arruma_tags(post.getConteudo()));
                } else {
                    post.setTitulo(result.getString("titulo"));
                    post.setConteudo(tag.arruma_tags(result.getString("conteudo")));
                }
                post.setId(result.getInt("id"));
                post.setRepublicacao(result.getBoolean("republicacao"));
                post.setId_repub(result.getInt("id_repub"));
                postList.add(post);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return postList;
    }

    public Like countLD(int post_id) throws SQLException {
        Like countLD = new Like();

        try (PreparedStatement statement = connection.prepareStatement(likeCount)) {
            statement.setInt(1, post_id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                countLD.setLike(result.getInt("contador"));
            } else {
                countLD.setLike(0);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        try (PreparedStatement statement = connection.prepareStatement(dislikeCount)) {
            statement.setInt(1, post_id);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                countLD.setDislike(result.getInt("contador"));
            } else {
                countLD.setLike(0);
            }

        } catch (SQLException ex) {
            throw ex;
        }

        return countLD;
    }

    public int qual_like(Integer id_pessoa, Integer id_post) throws SQLException {
        int ret = 0;
        try (PreparedStatement statement = connection.prepareStatement(situacaoLike)) {
            statement.setInt(1, id_pessoa);
            statement.setInt(2, id_post);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                ret = result.getInt("contador");
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return ret;
    }

    public void tira_like(Integer id_pessoa, Integer id_post) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(tiraLike)) {
            statement.setInt(1, id_pessoa);
            statement.setInt(2, id_post);

            statement.executeUpdate();

        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void coloca_like(Integer id_pessoa, Integer id_post) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(colocaLike)) {
            statement.setInt(1, id_pessoa);
            statement.setInt(2, id_post);

            statement.executeUpdate();

        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void coloca_dislike(Integer id_pessoa, Integer id_post) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(colocaDisLike)) {
            statement.setInt(1, id_pessoa);
            statement.setInt(2, id_post);

            statement.executeUpdate();

        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void repost(int id_post, Integer id_autor) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(repostQuery)) {
            statement.setInt(1, id_autor);
            statement.setInt(2, id_post);

            statement.executeUpdate();

        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void addComentario(Comentario comentario) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(comentar);) {
            statement.setInt(1, comentario.getId_post());
            statement.setString(2, comentario.getComentario());
            statement.setInt(3, comentario.getDono());

            statement.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public List<Comentario> getComentarios(int post_id) throws SQLException {
        List<Comentario> comentariosList = new ArrayList();
        try (PreparedStatement statement = connection.prepareStatement(comentarios);) {
            statement.setInt(1, post_id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Comentario comentario = new Comentario();
                comentario.setComentario(result.getString("comentario"));
                comentario.setDono(Integer.parseInt(result.getString("dono")));
                comentariosList.add(comentario);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return comentariosList;
    }

    public List<Post> filtro(List<String> hashtag, List<String> arroba) throws SQLException {
        List<Post> postList = new ArrayList<>();
        Outros tag = new Outros();
        boolean controle = false;
        String filtroQuery = "SELECT * FROM post p INNER JOIN usuario u ON p.id_autor=u.id WHERE ";
        if (!arroba.isEmpty()) {
            filtroQuery += "u.login IN (";
        }
        for (Iterator<String> it = arroba.iterator(); it.hasNext();) {
            filtroQuery += "'" + it.next() + "',";
            controle = true;
        }
        if (controle) {
            filtroQuery = filtroQuery.substring(0, filtroQuery.length() - 1);
            controle = false;
            filtroQuery += ")";
        }

        for (Iterator<String> it = hashtag.iterator(); it.hasNext();) {
            if (!controle) {
                filtroQuery += " AND (";
            }
            filtroQuery += "p.conteudo LIKE '%" + it.next() + "%' OR ";
            controle = true;
        }
        if (controle) {
            filtroQuery = filtroQuery.substring(0, filtroQuery.length() - 3);
            filtroQuery += ")";
        }

        try (PreparedStatement statement = connection.prepareStatement(filtroQuery)) {
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Post post = new Post();
                if (result.getBoolean("republicacao")) {
                    post = read(result.getInt("id_repub"));
                    post.setConteudo(tag.arruma_tags(post.getConteudo()));
                } else {
                    post.setTitulo(result.getString("titulo"));
                    post.setConteudo(tag.arruma_tags(result.getString("conteudo")));
                }
                post.setId(result.getInt("id"));
                post.setRepublicacao(result.getBoolean("republicacao"));
                post.setId_repub(result.getInt("id_repub"));
                postList.add(post);
            }

        } catch (SQLException ex) {
            throw ex;
        }
        return postList;
    }
}
