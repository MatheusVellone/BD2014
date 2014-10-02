package controller;

import dao.DAOFactory;
import dao.GrupoDAO;
import dao.PostDAO;
import dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Grupo;
import model.Like;
import model.Post;
import model.Usuario;

@WebServlet(urlPatterns = {"/busca"})
public class BuscaController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/busca":
                DAOFactory df = new DAOFactory();
                UsuarioDAO Udao = df.getUsuarioDAO();
                PostDAO Pdao = df.getPostDAO();
                GrupoDAO Gdao = df.getGrupoDAO();
                HttpSession session = request.getSession();
                Usuario user = (Usuario) session.getAttribute("usuarioLogado");
                String termo_pesquisa = request.getParameter("termo");

                try {
                    List<Usuario> listUsuario = Udao.search(termo_pesquisa);
                    List<Post> listPost = Pdao.search(termo_pesquisa);
                    List<Grupo> listGrupo = Gdao.search(termo_pesquisa);
                    List<Integer> segue = Udao.segue(user.getId());
                    List<Boolean> segue_ou_nao = new ArrayList<>();
                    List<Usuario> repubAutores = new ArrayList<>();

                    for (Usuario usuario : listUsuario) {
                        if (segue.contains(usuario.getId())) {
                            segue_ou_nao.add(Boolean.TRUE);
                        } else {
                            segue_ou_nao.add(Boolean.FALSE);
                        }
                    }
                    
                    List<Like> likeList = new ArrayList<>();
                    for (Post post : listPost) {
                        if(post.isRepublicacao()){
                            int id_original = post.getId_repub();
                            int autor_original = Pdao.read(id_original).getId_autor();
                            Usuario u_autor_original = Udao.read(autor_original);
                            repubAutores.add(u_autor_original);
                        }
                        likeList.add(Pdao.countLD(post.getId()));
                    }
                    
                    request.setAttribute("repubAutores", repubAutores);
                    request.setAttribute("likeList", likeList);
                    request.setAttribute("listUsuario", listUsuario);
                    request.setAttribute("listPost", listPost);
                    request.setAttribute("listGrupo", listGrupo);
                    request.setAttribute("segue_ou_nao", segue_ou_nao);
                    request.setAttribute("repost", 0);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(BuscaController.class.getName()).log(Level.SEVERE, null, ex);
                }

                request.setAttribute("termo_pesquisa", termo_pesquisa);
                dispatcher = request.getRequestDispatcher("/view/busca/index.jsp");
                dispatcher.forward(request, response);
        }
    }
}
