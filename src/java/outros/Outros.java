package outros;

import dao.DAO;
import dao.DAOFactory;
import dao.PostDAO;
import dao.UsuarioDAO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
            "/file/upload"
        }
)
@MultipartConfig
public class Outros extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DAO dao;
        Post post = new Post();

        switch (request.getServletPath()) {
            case "/file/upload":
                Part part = request.getPart("file");
                if (part != null) {
                    String nomeImagem = upload(request, response);
                    System.out.println("Imagem " + nomeImagem + " feito upload com sucesso.");
                } else {
                    throw new IOException("Imagem não está fazendo upload");
                }
                break;
        }
    }

    public String arruma_tags(String mensagem) {
        mensagem = " " + mensagem + " ";
        mensagem = hashtag(mensagem);
        mensagem = link(mensagem);
        mensagem = imagem(mensagem);
        mensagem = video(mensagem);
        mensagem = mensagem.substring(1, mensagem.length() - 1);
        return mensagem;
    }

    public String hashtag(String mensagem) {
        while (mensagem.contains(" #")) {
            String tema = mensagem.substring(mensagem.indexOf(" #") + 2, mensagem.indexOf(" ", mensagem.indexOf(" #") + 2));
            if (!tema.equals("")) {
                String link = " <a href=\"/bd2014/post?tema=" + tema + "\">#" + tema + "</a>";
                String padrao = " #" + tema;
                mensagem = mensagem.replace(padrao, link);
            }
        }
        return mensagem;
    }

    public String link(String mensagem) {
        while (mensagem.contains("$l:")) {
            String url = mensagem.substring(mensagem.indexOf("$l:"));
            url = url.substring(url.indexOf("\"") + 1);
            url = url.substring(0, url.indexOf("\""));
            if (!url.equals("")) {
                String padrao = "$l:\"" + url + "\"";
                if (!url.contains("http")) {
                    url = "http://" + url;
                }
                String link = "<a href=\"" + url + "\" target=\"_blank\">" + url + "</a>";
                mensagem = mensagem.replace(padrao, link);
            }
        }
        return mensagem;
    }

    public String imagem(String mensagem) {
        while (mensagem.contains("$i:")) {
            String url = mensagem.substring(mensagem.indexOf("$i:"));
            url = url.substring(url.indexOf("\"") + 1);
            url = url.substring(0, url.indexOf("\""));
            if (!url.equals("")) {
                String imagem = "<img src=\"" + url + "\" height=\"240\">";
                String padrao = "$i:\"" + url + "\"";
                mensagem = mensagem.replace(padrao, imagem);
            }
        }
        return mensagem;
    }

    public String video(String mensagem) {
        while (mensagem.contains("$v:")) {
            String url = mensagem.substring(mensagem.indexOf("$v:"));
            url = url.substring(url.indexOf("\"") + 1);
            url = url.substring(0, url.indexOf("\""));
            if (!url.equals("")) {
                String video;
                if (url.contains("youtube")) {
                    String id_video = url.substring(url.indexOf('=') + 1);
                    video = "<iframe width=\"500\" height=\"350\" src=\"//www.youtube.com/embed/" + id_video + "\" frameborder=\"0\"></iframe>";
                } else {
                    video = "<video width='320' height='240' controls><source src=" + url + ">O navegador nao suporta a tag <video>.</video>";
                }
                String padrao = "$v:\"" + url + "\"";
                mensagem = mensagem.replace(padrao, video);
            }
        }
        return mensagem;
    }

    public String upload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String dir = "img/posts";
        Part filePart = request.getPart("file");
        String fileName = getFilename(filePart);

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
