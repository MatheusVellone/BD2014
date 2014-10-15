package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Flutuacao;
import model.Post;
import model.Similaridade;
import model.Top20;
import model.Top20P;
import outros.Outros;

public class RelatorioDAO extends DAO<Post> {

    private static final String top20usuarios = "SELECT a.id_usuario as id, a.nome, a.foto, CASE WHEN num_comentario IS NULL THEN 0 ELSE num_comentario*2 END + CASE WHEN num_repost IS NULL THEN 0 ELSE num_repost*2 END + CASE WHEN num_like IS NULL THEN 0 ELSE num_like END - CASE WHEN num_dislike IS NULL THEN 0 ELSE num_dislike END as influencia FROM (SELECT u.id AS id_usuario, u.nome, u.foto FROM usuario u, post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false AND p.id_autor = u.id GROUP BY id_usuario, u.nome) AS a NATURAL LEFT JOIN (SELECT b.id_autor AS id_usuario, count(c.id) as num_comentario FROM comentarios c,(SELECT p.id_autor, p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false ORDER BY p.id_autor) AS b WHERE b.id = c.id_post GROUP BY id_usuario ORDER BY id_usuario) AS c NATURAL LEFT JOIN (SELECT l.usuario_id AS id_usuario, count(l.like_sit) AS num_like FROM likes l,(SELECT p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false) AS d WHERE l.like_sit = 1 AND d.id = l.post_id GROUP BY id_usuario) as e NATURAL LEFT JOIN (SELECT l.usuario_id AS id_usuario, count(l.like_sit) AS num_dislike FROM likes l,(SELECT p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false) AS f WHERE l.like_sit = -1 AND f.id = l.post_id GROUP BY id_usuario) as g NATURAL LEFT JOIN (SELECT p.id_autor as id_usuario, count(p.id) as num_repost FROM post p,(SELECT p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false) AS h WHERE h.id = p.id_repub AND p.republicacao = true GROUP BY id_usuario) as i order by influencia desc limit 20";
    private static final String top20publicacoes = "SELECT a.id_usuario AS id ,a.nome ,a.foto ,a.titulo ,a.conteudo ,CASE  WHEN num_comentario IS NULL THEN 0 ELSE num_comentario * 3 END + CASE  WHEN num_repost IS NULL THEN 0 ELSE num_repost * 3 END + CASE  WHEN num_like IS NULL THEN 0 ELSE num_like * 2 END + CASE  WHEN num_dislike IS NULL THEN 0 ELSE num_dislike END AS impacto FROM ( SELECT u.id AS id_usuario ,u.nome ,u.foto ,p.titulo ,p.conteudo ,p.id AS id_post FROM usuario u ,post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false AND p.id_autor = u.id ) AS a NATURAL LEFT JOIN ( SELECT c.id_post ,count(c.id) AS num_comentario FROM comentarios c ,( SELECT p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false ) AS b WHERE b.id = c.id_post GROUP BY id_post ORDER BY id_post ) AS c NATURAL LEFT JOIN ( SELECT l.post_id AS id_post ,count(l.like_sit) AS num_like FROM likes l ,( SELECT p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false ) AS d WHERE l.like_sit = 1 AND d.id = l.post_id GROUP BY id_post ) AS e NATURAL LEFT JOIN ( SELECT l.post_id AS id_post ,count(l.like_sit) AS num_dislike FROM likes l ,( SELECT p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false ) AS f WHERE l.like_sit = - 1 AND f.id = l.post_id GROUP BY id_post ) AS g NATURAL LEFT JOIN ( SELECT p.id AS id_post ,count(p.id) AS num_repost FROM post p ,( SELECT p.id FROM post p WHERE p.created <= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.created >= TO_DATE(?, 'YYYY-MM-DD HH24:MI') AND p.republicacao = false ) AS h WHERE h.id = p.id_repub AND p.republicacao = true GROUP BY id_post ) AS i ORDER BY impacto DESC limit 20";
    private static final String influencia = "SELECT COUNT(DISTINCT(id_seguidor)) AS influencia FROM ( (SELECT * FROM seguidores s WHERE s.id_segue = ?) UNION (SELECT * FROM seguidores s WHERE s.id_segue IN  (SELECT s2.id_seguidor FROM seguidores s2 WHERE s2.id_segue = ?) ) ) as a";
    private static final String similaridade = "select *, similaridade(u.id,?) as similaridade from usuario u order by similaridade desc limit 10;";

    public RelatorioDAO(Connection connection) {
        super(connection);
    }

    public int influencia(int id) throws SQLException {
        int influ = 0;

        try (PreparedStatement statement = connection.prepareStatement(influencia);) {
            statement.setInt(1, id);
            statement.setInt(2, id);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                influ = result.getInt(1);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return influ;
    }
    
    public List<Flutuacao> flutuacao(String inicio, String fim) throws SQLException{
        List<Flutuacao> flut = new ArrayList();
        
        return flut;
    }
    
    public List<Similaridade> similaridade(int id) throws SQLException {
        List<Similaridade> top10 = new ArrayList();

        try (PreparedStatement statement = connection.prepareStatement(similaridade);) {
            statement.setInt(1, id);
            
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Similaridade t = new Similaridade();
                t.setSimilaridade(result.getFloat("similaridade"));
                t.setFoto(result.getString("foto"));
                t.setId(result.getInt("id"));
                t.setNome(result.getString("nome"));

                top10.add(t);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return top10;
    }

    @Override
    public void create(Post post) throws SQLException {
    }

    @Override
    public Post read(Integer id) throws SQLException {
        Post post = new Post();
        return post;
    }

    @Override
    public void update(Post post) throws SQLException {
    }

    @Override
    public void delete(Integer id) throws SQLException {
    }

    @Override
    public List<Post> all() throws SQLException {
        List<Post> postList = new ArrayList<>();
        return postList;
    }

    public List<Top20> top20usuarios(String inicio, String fim) throws SQLException {
        List<Top20> top20 = new ArrayList();

        try (PreparedStatement statement = connection.prepareStatement(top20usuarios);) {
            statement.setString(1, fim);
            statement.setString(2, inicio);
            statement.setString(3, fim);
            statement.setString(4, inicio);
            statement.setString(5, fim);
            statement.setString(6, inicio);
            statement.setString(7, fim);
            statement.setString(8, inicio);
            statement.setString(9, fim);
            statement.setString(10, inicio);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Top20 top = new Top20();

                top.setId(result.getInt("id"));
                top.setNome(result.getString("nome"));
                top.setFoto(result.getString("foto"));
                top.setInfluencia(result.getInt("influencia"));

                top20.add(top);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return top20;
    }

    public List<Top20P> top20publicacoes(String inicio, String fim) throws SQLException {
        List<Top20P> top20 = new ArrayList();
        Outros tag = new Outros();

        try (PreparedStatement statement = connection.prepareStatement(top20publicacoes);) {
            statement.setString(1, fim);
            statement.setString(2, inicio);
            statement.setString(3, fim);
            statement.setString(4, inicio);
            statement.setString(5, fim);
            statement.setString(6, inicio);
            statement.setString(7, fim);
            statement.setString(8, inicio);
            statement.setString(9, fim);
            statement.setString(10, inicio);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Top20P top = new Top20P();

                top.setId(result.getInt("id"));
                top.setNome(result.getString("nome"));
                top.setFoto(result.getString("foto"));
                top.setTitulo(result.getString("titulo"));
                top.setConteudo(tag.arruma_tags(result.getString("conteudo")));
                top.setImpacto(result.getInt("impacto"));

                top20.add(top);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return top20;
    }
}
