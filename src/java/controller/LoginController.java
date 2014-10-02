package controller;

import dao.DAOFactory;
import dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Usuario;

@WebServlet(urlPatterns = {"",
    "/login",
    "/logout"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session;
        RequestDispatcher dispatcher;

        switch (request.getServletPath()) {
            case "":
                session = request.getSession(false);

                if (session != null && session.getAttribute("usuarioLogado") != null) {
                    response.sendRedirect(request.getContextPath() + "/usuario/welcome");
                } else {
                    dispatcher = request.getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                }

                break;
            case "/logout":
                session = request.getSession(false);

                if (session != null) {
                    session.invalidate();
                }

                response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioDAO dao;
        Usuario usuario = new Usuario();

        switch (request.getServletPath()) {
            case "/login":
                usuario.setLogin(request.getParameter("login"));
                usuario.setSenha(request.getParameter("senha"));

                try (DAOFactory daoFactory = new DAOFactory();) {
                    dao = daoFactory.getUsuarioDAO();

                    dao.authenticate(usuario);

                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioLogado", usuario);
                } catch (SQLException | SecurityException ex) {
                    System.err.println(ex.getMessage());
                }

                response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
