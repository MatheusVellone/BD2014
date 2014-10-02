package controller;

import dao.DAO;
import dao.DAOFactory;
import dao.PostDAO;
import dao.UsuarioDAO;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import model.Comentario;
import model.Like;
import model.Post;
import model.Usuario;

@WebServlet(
        urlPatterns = {
            "/usuario/create",
            "/usuario/perfil",
            "/usuario/welcome",
            "/usuario/read",
            "/usuario/update",
            "/usuario/delete",
            "/usuario",
            "/usuario/seguir",
            "/usuario/pararDEseguir",
            "/upload"
        }
)
@MultipartConfig
public class UsuarioController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/usuario/create":
                dispatcher = request.getRequestDispatcher("/view/usuario/create.jsp");
                dispatcher.forward(request, response);

                break;
            case "/usuario/welcome":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    PostDAO Pdao = daoFactory.getPostDAO();
                    UsuarioDAO Udao = daoFactory.getUsuarioDAO();
                    HttpSession session = request.getSession();

                    Usuario user = (Usuario) session.getAttribute("usuarioLogado");

                    List<Post> postList = Pdao.seguidores_posts(user.getId());
                    List<Like> likeList = new ArrayList<>();
                    List<Usuario> repubAutores = new ArrayList<>();
                    List<List<Comentario>> comentarios = new ArrayList<>();
                    List<List<Usuario>> dono_comentarios = new ArrayList<>();

                    for (Post post : postList) {
                        List<Comentario> l_comentario = Pdao.getComentarios(post.getId());
                        List<Usuario> l_dono_comentario = new ArrayList<>();
                        comentarios.add(l_comentario);
                        for (Comentario com : l_comentario) {
                            l_dono_comentario.add(Udao.read(com.getDono()));
                        }
                        dono_comentarios.add(l_dono_comentario);
                        if (post.isRepublicacao()) {
                            int id_original = post.getId_repub();
                            int autor_original = Pdao.read(id_original).getId_autor();
                            Usuario u_autor_original = Udao.read(autor_original);
                            repubAutores.add(u_autor_original);
                        }
                        likeList.add(Pdao.countLD(post.getId()));
                    }

                    request.setAttribute("comentarios", comentarios);
                    request.setAttribute("dono_comentarios", dono_comentarios);
                    request.setAttribute("repubAutores", repubAutores);
                    request.setAttribute("likeList", likeList);
                    request.setAttribute("postList", postList);
                    request.setAttribute("repost", 0);

                    dispatcher = request.getRequestDispatcher("/view/usuario/welcome.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/usuario");
                }

                break;
            case "/usuario/perfil":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    PostDAO Pdao = daoFactory.getPostDAO();
                    UsuarioDAO usuario_dao = daoFactory.getUsuarioDAO();
                    Usuario usuario = (Usuario) usuario_dao.read(id);

                    List<Post> posts = Pdao.postsAutor(id);

                    int euSigo = usuario_dao.quantosEuSigo(id);
                    int meSegue = usuario_dao.quantosMeSeguem(id);

                    List<Usuario> pessoasEuSigo = usuario_dao.quemEuSigo(id);
                    List<Usuario> pessoasMeSeguem = usuario_dao.quemMeSegue(id);

                    List<Usuario> repubAutores = new ArrayList<>();
                    List<Like> likeList = new ArrayList<>();
                    for (Post post : posts) {
                        if (post.isRepublicacao()) {
                            int id_original = post.getId_repub();
                            int autor_original = Pdao.read(id_original).getId_autor();
                            Usuario u_autor_original = usuario_dao.read(autor_original);
                            repubAutores.add(u_autor_original);
                        }
                        likeList.add(Pdao.countLD(post.getId()));
                    }

                    request.setAttribute("pessoasEuSigo", pessoasEuSigo);
                    request.setAttribute("posts", posts);
                    request.setAttribute("usuario", usuario);
                    request.setAttribute("euSigo", euSigo);
                    request.setAttribute("meSegue", meSegue);
                    request.setAttribute("pessoasMeSeguem", pessoasMeSeguem);
                    request.setAttribute("repubAutores", repubAutores);
                    request.setAttribute("likeList", likeList);
                    request.setAttribute("repost", 0);

                    dispatcher = request.getRequestDispatcher("/view/usuario/perfil.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/usuario");
                }

                break;
            case "/usuario/read":
            case "/usuario/update":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getUsuarioDAO();

                    Usuario usuario = (Usuario) dao.read(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("usuario", usuario);

                    dispatcher = request.getRequestDispatcher("/view" + request.getServletPath() + ".jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/usuario");
                }

                break;
            case "/usuario/delete":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getUsuarioDAO();

                    dao.delete(Integer.parseInt(request.getParameter("id")));
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath());

                break;
            case "/usuario/pararDEseguir":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    UsuarioDAO Udao = daoFactory.getUsuarioDAO();
                    HttpSession session = request.getSession();
                    Usuario user = (Usuario) session.getAttribute("usuarioLogado");

                    Udao.pararDeSeguir(user.getId(), Integer.parseInt(request.getParameter("id_segue")));
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/");

                break;
            case "/usuario/seguir":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    UsuarioDAO daoU = daoFactory.getUsuarioDAO();
                    HttpSession session = request.getSession();
                    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

                    daoU.seguir(usuario.getId(), Integer.parseInt(request.getParameter("seguir")));

                    response.sendRedirect(request.getContextPath() + "/usuario");
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;
            case "/usuario":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    UsuarioDAO Udao = daoFactory.getUsuarioDAO();
                    HttpSession session = request.getSession();
                    Usuario user = (Usuario) session.getAttribute("usuarioLogado");

                    List<Usuario> usuarioList = Udao.all();
                    List<Integer> segue = Udao.segue(user.getId());
                    List<Boolean> segue_ou_nao = new ArrayList<>();

                    for (Usuario usuario : usuarioList) {
                        if (segue.contains(usuario.getId())) {
                            segue_ou_nao.add(Boolean.TRUE);
                        } else {
                            segue_ou_nao.add(Boolean.FALSE);
                        }
                    }

                    request.setAttribute("usuarioList", usuarioList);
                    request.setAttribute("segue_ou_nao", segue_ou_nao);

                    dispatcher = request.getRequestDispatcher("/view/usuario/index.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/usuario");
                }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao;
        Usuario usuario = new Usuario();

        switch (request.getServletPath()) {
            case "/usuario/create":
                usuario.setLogin(request.getParameter("login"));
                usuario.setSenha(request.getParameter("senha"));
                usuario.setNome(request.getParameter("nome"));
                usuario.setDescricao(request.getParameter("descricao"));
                usuario.setNascimento(Date.valueOf(request.getParameter("nascimento")));

                if (!getFilename(request.getPart("foto")).equals("")) {
                    usuario.setFoto(upload(request, response, usuario));
                } else {
                    usuario.setFoto("null.png");
                }

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getUsuarioDAO();

                    dao.create(usuario);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/");

                break;
            case "/usuario/update":
                usuario.setId(Integer.parseInt(request.getParameter("id")));
                usuario.setLogin(request.getParameter("login"));
                usuario.setNome(request.getParameter("nome"));
                usuario.setDescricao(request.getParameter("descricao"));
                usuario.setNascimento(Date.valueOf(request.getParameter("nascimento")));

                if (!getFilename(request.getPart("foto")).equals("")) {
                    usuario.setFoto(upload(request, response, usuario));
                }

                if (!request.getParameter("senha").isEmpty()) {
                    usuario.setSenha(request.getParameter("senha"));
                }

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getUsuarioDAO();

                    dao.update(usuario);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/");

                break;
            case "/usuario/delete":
                String[] usuarios = request.getParameterValues("delete");

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getUsuarioDAO();

                    try {
                        daoFactory.beginTransaction();

                        for (int i = 0; i < usuarios.length; ++i) {
                            dao.delete(Integer.parseInt(usuarios[i]));
                        }

                        daoFactory.commitTransaction();
                        daoFactory.endTransaction();
                    } catch (SQLException ex) {
                        System.err.println(ex.getMessage());
                        daoFactory.rollbackTransaction();
                    }
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/usuario");

                break;
        }
    }

    public String upload(HttpServletRequest request, HttpServletResponse response, Usuario usuario) throws IOException, ServletException {
        String dir = "img";
        Part filePart = request.getPart("foto");
        String fileName = usuario.getLogin() + ".png";

        filePart.write(getServletContext().getRealPath("") + File.separator + dir + File.separator + fileName);
        return fileName;
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
