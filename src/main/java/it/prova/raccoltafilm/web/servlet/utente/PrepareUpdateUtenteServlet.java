package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.service.MyServiceFactory;


@WebServlet("/admin/PrepareUpdateUtenteServlet")
public class PrepareUpdateUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long idUtenteInput = Long.parseLong(request.getParameter("idUtente"));

		try {
			// metto un bean 'vuoto' in request perché per la pagina risulta necessario
			request.setAttribute("update_utente_attr",
					MyServiceFactory.getUtenteServiceInstance().caricaSingoloElemento(idUtenteInput));
			// questo mi serve per la select di registi in pagina
			request.setAttribute("ruoli_list_attribute",
					MyServiceFactory.getRuoloServiceInstance().listAll());
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}

		request.getRequestDispatcher("/utente/update.jsp").forward(request, response);
	}

}
