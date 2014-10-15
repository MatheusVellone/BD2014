package controller;

import dao.DAOFactory;
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
import model.Similaridade;
import model.Top20;
import model.Top20P;
import model.Usuario;

@WebServlet(urlPatterns = {
    "/relatorio",
    "/relatorio/top20usuarios",
    "/relatorio/top20publicacoes",
    "/relatorio/influencia",
    "/relatorio/similares",
    "/relatorio/flutuacao"
}
)
public class RelatorioController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        List<Usuario> usuarios = new ArrayList();
        int influencia = 0;
        try (DAOFactory dfactory = new DAOFactory();) {
            UsuarioDAO uDAO = dfactory.getUsuarioDAO();
            usuarios = uDAO.all();
        } catch (SQLException ex) {
            Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        request.setAttribute("usuarios", usuarios);
        request.setAttribute("influencia", influencia);
        dispatcher = request.getRequestDispatcher("/view/relatorio/index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        String data_inicio;
        String data_fim;
        switch (request.getServletPath()) {
            case "/relatorio/flutuacao":
                dispatcher = request.getRequestDispatcher("/view/relatorio/influencia3usuarios.jsp");
                dispatcher.forward(request, response);
                break;
            case "/relatorio/top20usuarios":
                List<Top20> top20 = null;
                try (DAOFactory factory = new DAOFactory();) {
                    data_inicio = request.getParameter("data_inicio");
                    data_fim = request.getParameter("data_fim");
                    data_inicio = data_inicio.replace('T', ' ');
                    data_fim = data_fim.replace('T', ' ');
                    top20 = factory.getRelatorioDAO().top20usuarios(data_inicio, data_fim);
                } catch (SQLException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }

                request.setAttribute("top20", top20);

                dispatcher = request.getRequestDispatcher("/view/relatorio/top20usuario.jsp");
                dispatcher.forward(request, response);
                break;
            case "/relatorio/top20publicacoes":
                System.out.println("entrou");
                List<Top20P> top20p = null;
                try (DAOFactory factory = new DAOFactory();) {
                    data_inicio = request.getParameter("data_inicio");
                    data_fim = request.getParameter("data_fim");
                    data_inicio = data_inicio.replace('T', ' ');
                    data_fim = data_fim.replace('T', ' ');
                    top20p = factory.getRelatorioDAO().top20publicacoes(data_inicio, data_fim);
                    System.out.println("saiu");
                } catch (SQLException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }

                request.setAttribute("top20", top20p);

                dispatcher = request.getRequestDispatcher("/view/relatorio/top20publicacao.jsp");
                dispatcher.forward(request, response);
                break;
            case "/relatorio/similares":
                try (DAOFactory factory = new DAOFactory();) {
                    int id = Integer.parseInt(request.getParameter("usuario"));
                    List<Similaridade> similaridade = factory.getRelatorioDAO().similaridade(id);
                    request.setAttribute("similaridade", similaridade);
                } catch (SQLException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }

                dispatcher = request.getRequestDispatcher("/view/relatorio/similaridade.jsp");
                dispatcher.forward(request, response);
                break;
            case "/relatorio/influencia":
                try (DAOFactory factory = new DAOFactory();) {
                    int id = Integer.parseInt(request.getParameter("usuario"));
                    int influencia = factory.getRelatorioDAO().influencia(id);
                    Usuario usuario_ = factory.getUsuarioDAO().read(id);
                    request.setAttribute("influencia", influencia);
                    request.setAttribute("usuario", usuario_);
                } catch (SQLException ex) {
                    Logger.getLogger(RelatorioController.class.getName()).log(Level.SEVERE, null, ex);
                }

                dispatcher = request.getRequestDispatcher("/view/relatorio/influencia.jsp");
                dispatcher.forward(request, response);
                break;
            default:
                dispatcher = request.getRequestDispatcher("/view/relatorio/index.jsp");
                dispatcher.forward(request, response);
        }
    }
}
