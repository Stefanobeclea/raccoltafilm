package it.prova.raccoltafilm.web.servlet.regista;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import it.prova.raccoltafilm.exceptions.ElementNotFoundException;
import it.prova.raccoltafilm.exceptions.RegistaAssociatoException;
import it.prova.raccoltafilm.service.MyServiceFactory;


@WebServlet("/ExecuteDeleteRegistaServlet")
public class ExecuteDeleteRegistaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public ExecuteDeleteRegistaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idRegistaParam = request.getParameter("idRegista");

		if (!NumberUtils.isCreatable(idRegistaParam)) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.(id)");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		}
		
		

		try {
			// novità rispetto al passato: abbiamo un overload di rimuovi che agisce per id
			// in questo modo spostiamo la logica di caricamento/rimozione nel service
			// usando la stessa finestra di transazione e non aprendo e chiudendo due volte
			// inoltre mi torna utile quando devo fare rimozioni eager
			MyServiceFactory.getRegistaServiceInstance().rimuovi(Long.parseLong(idRegistaParam));
		} catch (ElementNotFoundException e) {
			request.getRequestDispatcher("ExecuteListRegistaServlet?operationResult=NOT_FOUND").forward(request, response);
			return;
		} catch (RegistaAssociatoException e) {
			request.getRequestDispatcher("ExecuteListRegistaServlet?operationResult=ASSOCIATO").forward(request, response);
			return;
		}catch (Exception e) {
			// qui ci andrebbe un messaggio nei file di log costruito ad hoc se fosse attivo
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("home").forward(request, response);
			return;
		} 
		response.sendRedirect("ExecuteListRegistaServlet?operationResult=SUCCESS");
	}

}
