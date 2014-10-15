package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DAOFactory;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Comentario;
import model.Like;
import model.Post;
import model.Usuario;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@WebServlet(
        name = "WebserviceController",
        urlPatterns = {
            "/exportar",
            "/importar"
        }
)
public class WebserviceController extends HttpServlet {

    int server = 23;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/importar":
                importarGet(request, response);
                break;
            case "/exportar":
                Pacote pacote = new Pacote();

                try (DAOFactory factory = new DAOFactory();) {
                    List<Post> postsDB = factory.getPostDAO().todosPosts();
                    for (Post postDB : postsDB) {
                        PostJSON postJSON = new PostJSON();
                        postJSON.id = postDB.getId();
                        postJSON.id_usuario = postDB.getId_autor();
                        postJSON.titulo = postDB.getTitulo();
                        postJSON.conteudo = postDB.getConteudo();

                        Date t = new Date(postDB.getData().getTime());
                        postJSON.data = t;

                        pacote.posts.add(postJSON);
                    }

                    List<Post> repostsDB = factory.getPostDAO().todosReposts();
                    for (Post repostDB : repostsDB) {
                        RepostJSON repostJSON = new RepostJSON();
                        repostJSON.id = repostDB.getId();
                        repostJSON.id_usuario = repostDB.getId_autor();

                        Date t = new Date(repostDB.getData().getTime());
                        repostJSON.data = t;

                        repostJSON.id_post = repostDB.getId_repub();
                        pacote.reposts.add(repostJSON);
                    }

                    List<Like> likesDB = factory.getPostDAO().todosLikes();
                    for (Like likeDB : likesDB) {
                        LikeJSON likeJSON = new LikeJSON();
                        likeJSON.id_usuario = likeDB.getId_usuario();
                        likeJSON.id_post = likeDB.getId_post();

                        Date t = new Date(likeDB.getData().getTime());
                        likeJSON.data = t;

                        pacote.likes.add(likeJSON);
                    }

                    List<Like> dislikesDB = factory.getPostDAO().todosDislikes();
                    for (Like dislikeDB : dislikesDB) {
                        DislikeJSON dislikeJSON = new DislikeJSON();
                        dislikeJSON.id_usuario = dislikeDB.getId_usuario();
                        dislikeJSON.id_post = dislikeDB.getId_post();

                        Date t = new Date(dislikeDB.getData().getTime());
                        dislikeJSON.data = t;

                        pacote.dislikes.add(dislikeJSON);
                    }

                    List<Usuario> usuariosDB = factory.getUsuarioDAO().todosUsuarios();
                    for (Usuario usuarioDB : usuariosDB) {
                        UsuarioJSON usuarioJSON = new UsuarioJSON();
                        usuarioJSON.id = usuarioDB.getId();
                        usuarioJSON.login = usuarioDB.getLogin();
                        usuarioJSON.nome = usuarioDB.getNome();
                        usuarioJSON.nascimento = usuarioDB.getNascimento();
                        usuarioJSON.descricao = usuarioDB.getDescricao();

                        pacote.usuarios.add(usuarioJSON);
                    }

                    List<Comentario> comentariosDB = factory.getPostDAO().todosComentarios();
                    for (Comentario comentarioDB : comentariosDB) {
                        ComentarioJSON comentarioJSON = new ComentarioJSON();
                        comentarioJSON.id = comentarioDB.getId();
                        comentarioJSON.conteudo = comentarioDB.getComentario();

                        Date t = new Date(comentarioDB.getData().getTime());
                        comentarioJSON.data = t;

                        comentarioJSON.id_post = comentarioDB.getId_post();
                        comentarioJSON.id_usuario = comentarioDB.getDono();

                        pacote.comentarios.add(comentarioJSON);
                    }

                } catch (SQLException ex) {
                    throw new ServletException(ex);
                }

                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .setDateFormat("dd/MM/yyyy HH:mm")
                        .create();
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");

                try (PrintWriter out = response.getWriter()) {
                    gson.toJson(pacote, out);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/importar":
                try {
                    importarPOST(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(WebserviceController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }

    private void importarGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        dispatcher = request.getRequestDispatcher("/view/importar.jsp");
        dispatcher.forward(request, response);
    }

    static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("dd/MM/yyyy HH:mm")
            .create();

    private void importarPOST(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String url = request.getParameter("url");
        Pacote pacote = pegarPacoteDaURL(url);
        Map<Integer, Integer> mapaIdUsuarios = new TreeMap<>();
        Map<Integer, Integer> mapaIdPosts = new TreeMap<>();
        try (DAOFactory factory = new DAOFactory();) {

            factory.getUsuarioDAO().excluiDadosServidor(pacote.id_servidor);
            //Usuarios
            for (UsuarioJSON usuarioJSON : pacote.usuarios) {
                Usuario usuarioDB = new Usuario();
                usuarioDB.setLogin(usuarioJSON.login + "_" + pacote.id_servidor);
                usuarioDB.setNome(usuarioJSON.nome);
                usuarioDB.setNascimento(new Date(usuarioJSON.nascimento.getTime()));
                usuarioDB.setDescricao(usuarioJSON.descricao);
                usuarioDB.setServidor(pacote.id_servidor);

                factory.getUsuarioDAO().create(usuarioDB);
                int iDLocal = usuarioDB.getId();
                int idRemoto = usuarioJSON.id;
                mapaIdUsuarios.put(idRemoto, iDLocal);
            }

            //Posts
            for (PostJSON postJSON : pacote.posts) {
                Post postDB = new Post();
                postDB.setConteudo(postJSON.conteudo);
                postDB.setTitulo(postJSON.titulo);
                Timestamp t = new Timestamp(postJSON.data.getTime());
                postDB.setData(t);
                postDB.setId_autor(mapaIdUsuarios.get(postJSON.id_usuario));
                postDB.setServidor(pacote.id_servidor);

                factory.getPostDAO().create(postDB);
                int iDLocal = postDB.getId();
                int idRemoto = postJSON.id;
                mapaIdPosts.put(idRemoto, iDLocal);
            }

            //Reposts
            for (RepostJSON repostJSON : pacote.reposts) {
                Post repostDB = new Post();
                repostDB.setId_autor(mapaIdUsuarios.get(repostJSON.id_usuario));
                repostDB.setRepublicacao(true);
                repostDB.setId_repub(mapaIdPosts.get(repostJSON.id_post));
                Timestamp t = new Timestamp(repostJSON.data.getTime());
                repostDB.setData(t);
                repostDB.setServidor(pacote.id_servidor);

                factory.getPostDAO().repost(repostDB);
                int iDLocal = repostDB.getId();
                int idRemoto = repostJSON.id;
                mapaIdPosts.put(idRemoto, iDLocal);
            }

            //Comentarios
            for (ComentarioJSON comentarioJSON : pacote.comentarios) {
                Comentario comentarioDB = new Comentario();
                comentarioDB.setComentario(comentarioJSON.conteudo);
                Timestamp t = new Timestamp(comentarioJSON.data.getTime());
                comentarioDB.setData(t);
                comentarioDB.setDono(mapaIdUsuarios.get(comentarioJSON.id_usuario));
                comentarioDB.setId_post(mapaIdPosts.get(comentarioJSON.id_post));
                comentarioDB.setServidor(pacote.id_servidor);

                factory.getPostDAO().addComentario(comentarioDB);
            }

            //Likes
            for (LikeJSON likeJSON : pacote.likes) {
                Like likeDB = new Like();
                likeDB.setId_post(mapaIdPosts.get(likeJSON.id_post));
                likeDB.setId_usuario(mapaIdUsuarios.get(likeJSON.id_usuario));
                Timestamp t = new Timestamp(likeJSON.data.getTime());
                likeDB.setData(t);
                likeDB.setServidor(pacote.id_servidor);

                factory.getPostDAO().coloca_like(likeDB);
            }

            //Dislikes
            for (DislikeJSON dislikeJSON : pacote.dislikes) {
                Like dislikeDB = new Like();
                dislikeDB.setId_post(mapaIdPosts.get(dislikeJSON.id_post));
                dislikeDB.setId_usuario(mapaIdUsuarios.get(dislikeJSON.id_usuario));
                Timestamp t = new Timestamp(dislikeJSON.data.getTime());
                dislikeDB.setData(t);
                dislikeDB.setServidor(pacote.id_servidor);

                factory.getPostDAO().coloca_dislike(dislikeDB);
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    private Pacote pegarPacoteDaURL(String url) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse jsonResponse = httpClient.execute(new HttpGet(url)); InputStreamReader jsonContent = new InputStreamReader(jsonResponse.getEntity().getContent(), "utf-8")) {

                return gson.fromJson(jsonContent, Pacote.class);
            }
        }
    }

    static class Pacote {

        int id_servidor = 23;

        List<PostJSON> posts;
        List<RepostJSON> reposts;
        List<UsuarioJSON> usuarios;
        List<LikeJSON> likes;
        List<DislikeJSON> dislikes;
        List<ComentarioJSON> comentarios;

        public Pacote() {
            posts = new LinkedList<>();
            reposts = new LinkedList<>();
            likes = new LinkedList<>();
            dislikes = new LinkedList<>();
            comentarios = new LinkedList<>();
            usuarios = new LinkedList<>();

        }

    }

    static class PostJSON {

        int id;
        int id_usuario;
        String titulo;
        String conteudo;
        Date data;
    }

    static class RepostJSON {

        int id;
        int id_usuario;
        int id_post;
        Date data;
    }

    static class LikeJSON {

        int id_usuario;
        int id_post;
        Date data;
    }

    static class DislikeJSON {

        int id_usuario;
        int id_post;
        Date data;
    }

    static class ComentarioJSON {

        int id;
        int id_usuario;
        int id_post;
        String conteudo;
        Date data;
    }

    static class UsuarioJSON {

        int id;
        String nome;
        String login;
        Date nascimento;
        String descricao;
    }
}
