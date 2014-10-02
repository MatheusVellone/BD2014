package controller;

import dao.DAO;
import dao.DAOFactory;
import dao.PostDAO;
import dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Comentario;
import model.Like;
import model.Post;
import model.Usuario;

@WebServlet(
        urlPatterns = {
            "/post/create",
            "/post/read",
            "/post/update",
            "/post/delete",
            "/post/meus",
            "/post/like",
            "/post/dislike",
            "/post/repost",
            "/post/filtro",
            "/post/comentario",
            "/post"
        }
)
public class PostController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/post/create":
                dispatcher = request.getRequestDispatcher("/view/post/create.jsp");
                dispatcher.forward(request, response);

                break;
            case "/post/repost":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    int id_post = Integer.parseInt(request.getParameter("id_post"));
                    PostDAO pdao = daoFactory.getPostDAO();
                    HttpSession session = request.getSession();
                    Usuario user = (Usuario) session.getAttribute("usuarioLogado");

                    pdao.repost(id_post, user.getId());

                    response.sendRedirect(request.getContextPath() + "/");
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/");
                }

                break;
            case "/post/like":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    PostDAO pDAO = daoFactory.getPostDAO();
                    int pessoa_id = Integer.parseInt(request.getParameter("id_usuario"));
                    int post_id = Integer.parseInt(request.getParameter("id_post"));
                    int sit_like = pDAO.qual_like(pessoa_id, post_id);

                    if (sit_like == 1) {
                        //tira o like
                        pDAO.tira_like(pessoa_id, post_id);
                    } else if (sit_like == -1) {
                        //tira o dislike e coloca like
                        pDAO.tira_like(pessoa_id, post_id);
                        pDAO.coloca_like(pessoa_id, post_id);
                    } else {
                        //so coloca like
                        pDAO.coloca_like(pessoa_id, post_id);
                    }
                    request.setAttribute("likeResult", sit_like);

                    dispatcher = request.getRequestDispatcher("/view/post/likeresult.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            case "/post/dislike":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    PostDAO pDAO = daoFactory.getPostDAO();
                    int pessoa_id = Integer.parseInt(request.getParameter("id_usuario"));
                    int post_id = Integer.parseInt(request.getParameter("id_post"));
                    int sit_like = pDAO.qual_like(pessoa_id, post_id);

                    if (sit_like == 1) {
                        //tira o like
                        //coloca dislike
                        pDAO.tira_like(pessoa_id, post_id);
                        pDAO.coloca_dislike(pessoa_id, post_id);
                    } else if (sit_like == -1) {
                        //tira o dislike
                        pDAO.tira_like(pessoa_id, post_id);
                    } else {
                        //so coloca dislike
                        pDAO.coloca_dislike(pessoa_id, post_id);
                    }
                    request.setAttribute("likeResult", sit_like);

                    dispatcher = request.getRequestDispatcher("/view/post/likeresult.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            case "/post/meus":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    PostDAO pdao = daoFactory.getPostDAO();
                    UsuarioDAO Udao = daoFactory.getUsuarioDAO();
                    HttpSession session = request.getSession();
                    Usuario user = (Usuario) session.getAttribute("usuarioLogado");

                    List<Post> postList = pdao.postsAutor(user.getId());
                    List<Usuario> repubAutores = new ArrayList<>();
                    List<Like> likeList = new ArrayList<>();

                    for (Post post : postList) {
                        if (post.isRepublicacao()) {
                            int id_original = post.getId_repub();
                            int autor_original = pdao.read(id_original).getId_autor();
                            Usuario u_autor_original = Udao.read(autor_original);
                            repubAutores.add(u_autor_original);
                        }
                        likeList.add(pdao.countLD(post.getId()));
                    }

                    request.setAttribute("likeList", likeList);
                    request.setAttribute("postList", postList);
                    request.setAttribute("repubAutores", repubAutores);
                    request.setAttribute("repost", 0);

                    dispatcher = request.getRequestDispatcher("/view/post/meus.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/post");
                }
                break;
            case "/post/read":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getPostDAO();

                    Post post = (Post) dao.read(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("post", post);

                    dispatcher = request.getRequestDispatcher("/view/post/read.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/post");
                }

                break;
            case "/post/update":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getPostDAO();

                    Post post = (Post) dao.read(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("post", post);

                    dispatcher = request.getRequestDispatcher("/view/post/update.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/post");
                }

                break;
            case "/post/delete":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getPostDAO();

                    dao.delete(Integer.parseInt(request.getParameter("id")));
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/post/meus");

                break;
            case "/post":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    PostDAO daoP = daoFactory.getPostDAO();
                    UsuarioDAO Udao = daoFactory.getUsuarioDAO();
                    List<Post> postList;
                    List<Usuario> repubAutores = new ArrayList<>();

                    if (request.getParameterMap().containsKey("tema")) {
                        String tema = request.getParameter("tema");
                        postList = daoP.all(tema);
                    } else {
                        postList = daoP.all();
                    }

                    for (Post post : postList) {
                        if (post.isRepublicacao()) {
                            int id_original = post.getId_repub();
                            int autor_original = daoP.read(id_original).getId_autor();
                            Usuario u_autor_original = Udao.read(autor_original);
                            repubAutores.add(u_autor_original);
                        }
                    }

                    request.setAttribute("repubAutores", repubAutores);
                    request.setAttribute("postList", postList);

                    dispatcher = request.getRequestDispatcher("/view/post/index.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/post");
                }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao;
        Post post = new Post();
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/post/create":
                post.setId_autor(Integer.parseInt(request.getParameter("id_autor")));
                post.setTitulo(request.getParameter("titulo"));
                post.setConteudo(request.getParameter("conteudo"));
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getPostDAO();
                    dao.create(post);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/");

                break;
            case "/post/filtro":
                try (DAOFactory daof = new DAOFactory()) {
                    String[] filtros = request.getParameter("filtro").split(" ");
                    List<String> hashtag = new ArrayList<>();
                    List<String> arroba = new ArrayList<>();

                    List<Like> likeList = new ArrayList<>();
                    List<Usuario> repubAutores = new ArrayList<>();
                    List<Post> postList = new ArrayList<>();
                    PostDAO Pdao = daof.getPostDAO();
                    UsuarioDAO Udao = daof.getUsuarioDAO();

                    for (String filtro : filtros) {
                        if (filtro.contains("#")) {
                            filtro = filtro.substring(1);
                            hashtag.add(filtro);
                        } else if (filtro.contains("@")) {
                            filtro = filtro.substring(1);
                            arroba.add(filtro);
                        }
                    }

                    PostDAO dp = daof.getPostDAO();
                    postList = dp.filtro(hashtag, arroba);

                    for (Post p : postList) {
                        if (p.isRepublicacao()) {
                            int id_original = p.getId_repub();
                            int autor_original = Pdao.read(id_original).getId_autor();
                            Usuario u_autor_original = Udao.read(autor_original);
                            repubAutores.add(u_autor_original);
                        }
                        likeList.add(Pdao.countLD(p.getId()));
                    }

                    request.setAttribute("likeList", likeList);
                    request.setAttribute("postList", postList);
                    request.setAttribute("repubAutores", repubAutores);
                    request.setAttribute("repost", 0);

                    dispatcher = request.getRequestDispatcher("/view/post/filtro.jsp");
                    dispatcher.forward(request, response);

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                break;
            case "/post/update":
                post.setId(Integer.parseInt(request.getParameter("id")));
                post.setTitulo(request.getParameter("titulo"));
                post.setConteudo(request.getParameter("conteudo"));

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getPostDAO();

                    dao.update(post);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/post");

                break;
            case "/post/comentario":

                int idPost = Integer.parseInt(request.getParameter("idPost"));
                String comentario_conteudo = request.getParameter("comentario");
                int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                
                Comentario comentario = new Comentario();

                comentario.setId_post(idPost);
                comentario.setComentario(comentario_conteudo);
                comentario.setDono(idUsuario);
                try (DAOFactory daoFactory = new DAOFactory();) {
                    PostDAO pDao = daoFactory.getPostDAO();
                    pDao.addComentario(comentario);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                request.setAttribute("comentario", comentario_conteudo);
                dispatcher = request.getRequestDispatcher("/view/post/comentario.jsp");
                dispatcher.forward(request, response);
                break;
            case "/post/delete":
                String[] posts = request.getParameterValues("delete");

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getPostDAO();

                    try {
                        daoFactory.beginTransaction();

                        for (int i = 0; i < posts.length; ++i) {
                            dao.delete(Integer.parseInt(posts[i]));
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

                response.sendRedirect(request.getContextPath() + "/post");
        }
    }
}
