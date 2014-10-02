package controller;

import dao.DAO;
import dao.DAOFactory;
import dao.GrupoDAO;
import java.io.IOException;
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
import model.Grupo;
import model.Usuario;

@WebServlet(urlPatterns = {"/grupo/create",
    "/grupo/update",
    "/grupo/delete",
    "/grupo/entrar",
    "/grupo/gerencia",
    "/grupo/participa",
    "/grupo/sair",
    "/grupo"})
@MultipartConfig
public class GrupoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "/grupo/create":
                dispatcher = request.getRequestDispatcher("/view/grupo/create.jsp");
                dispatcher.forward(request, response);

                break;
            case "/grupo/gerencia":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    GrupoDAO dao_admin = daoFactory.getGrupoDAO();
                    List<Grupo> grupoList = dao_admin.allAdmin(Integer.parseInt(request.getParameter("id")));

                    request.setAttribute("grupoList", grupoList);

                    dispatcher = request.getRequestDispatcher("/view/grupo/gerencia.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
                break;
            case "/grupo/participa":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    GrupoDAO dao_admin = daoFactory.getGrupoDAO();
                    List<Grupo> grupoList = dao_admin.allIntegrante(Integer.parseInt(request.getParameter("id")));

                    request.setAttribute("grupoList", grupoList);

                    dispatcher = request.getRequestDispatcher("/view/grupo/participa.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
                break;
            case "/grupo/entrar":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    GrupoDAO daoG = daoFactory.getGrupoDAO();
                    daoG.novo_integrante(Integer.parseInt(request.getParameter("grupo")), Integer.parseInt(request.getParameter("usuario")));

                    response.sendRedirect(request.getContextPath() + "/");
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;
            case "/grupo/sair":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    GrupoDAO daoG = daoFactory.getGrupoDAO();
                    daoG.sairDoGrupo(Integer.parseInt(request.getParameter("id_usuario")), Integer.parseInt(request.getParameter("id_grupo")));

                    dispatcher = request.getRequestDispatcher("/");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/");
                }
                break;
            case "/grupo/update":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getGrupoDAO();

                    Grupo grupo = (Grupo) dao.read(Integer.parseInt(request.getParameter("id")));
                    request.setAttribute("grupo", grupo);

                    dispatcher = request.getRequestDispatcher("/view/grupo/update.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/");
                }

                break;
            case "/grupo/delete":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getGrupoDAO();

                    dao.delete(Integer.parseInt(request.getParameter("id")));
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/grupo/gerencia?id=1");

                break;
            case "/grupo":
                try (DAOFactory daoFactory = new DAOFactory();) {
                    List<String> listAdmin = new ArrayList<String>();
                    DAO dao_admin = daoFactory.getUsuarioDAO();
                    dao = daoFactory.getGrupoDAO();

                    List<Grupo> grupoList = dao.all();

                    for (Grupo grupo : grupoList) {
                        Usuario user = (Usuario) dao_admin.read(grupo.getId_admin());
                        listAdmin.add(user.getNome());
                    }

                    request.setAttribute("grupoList", grupoList);
                    request.setAttribute("listAdmin", listAdmin);

                    dispatcher = request.getRequestDispatcher("/view/grupo/index.jsp");
                    dispatcher.forward(request, response);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao;
        Grupo grupo = new Grupo();

        switch (request.getServletPath()) {
            case "/grupo/create":
                grupo.setId_admin(Integer.parseInt(request.getParameter("id_admin")));
                grupo.setNome(request.getParameter("nome"));

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getGrupoDAO();
                    dao.create(grupo);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/grupo");

                break;
            case "/grupo/update":
                grupo.setId(Integer.parseInt(request.getParameter("id")));
                grupo.setId_admin(Integer.parseInt(request.getParameter("id_admin")));
                grupo.setNome(request.getParameter("nome"));

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getGrupoDAO();

                    dao.update(grupo);
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/");

                break;
        }
    }
}
