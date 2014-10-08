package controller;

import dao.DAOFactory;
import dao.UsuarioDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Usuario;

@WebServlet(urlPatterns = {
    "/relatorio",
    "/relatorio/top20usuarios",
    "/relatorio/similares"
}
)
public class RelatorioController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        List<Usuario> usuarios = new ArrayList();

        try (DAOFactory dfactory = new DAOFactory();) {
            UsuarioDAO uDAO = dfactory.getUsuarioDAO();
            usuarios = uDAO.all();
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        request.setAttribute("usuarios", usuarios);
        dispatcher = request.getRequestDispatcher("/view/relatorio/index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        Date data_inicio = null;
        Date data_fim = null;
        switch (request.getServletPath()) {
            case "/relatorio/top20usuarios":
//                System.out.println(request.getParameter("data_inicio")+" inicio");
//                System.out.println(request.getParameter("data_fim")+" fim");
//                try {
//                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//                    data_inicio = new Date(format.parse(request.getParameter("data_inicio")).getTime());
//                    data_fim = new Date(format.parse(request.getParameter("data_fim")).getTime());
//                } catch (ParseException ex) {
//                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                System.out.println(data_inicio.toString());
                System.out.println(data_fim.toString());
                dispatcher = request.getRequestDispatcher("/view/relatorio/top20usuario.jsp");
                dispatcher.forward(request, response);
                break;
            case "/relatorio/similares":
                int usuario = Integer.parseInt(request.getParameter("usuario"));
                break;
            default:
                dispatcher = request.getRequestDispatcher("/view/relatorio/index.jsp");
                dispatcher.forward(request, response);
        }
    }
}
