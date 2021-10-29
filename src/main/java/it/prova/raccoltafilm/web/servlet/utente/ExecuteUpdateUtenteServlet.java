package it.prova.raccoltafilm.web.servlet.utente;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.prova.raccoltafilm.model.StatoUtente;
import it.prova.raccoltafilm.model.Utente;
import it.prova.raccoltafilm.service.MyServiceFactory;
import it.prova.raccoltafilm.utility.UtilityForm;


@WebServlet("/admin/ExecuteUpdateUtenteServlet")
public class ExecuteUpdateUtenteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idParam = request.getParameter("idUtente");
		String nomeParam = request.getParameter("nome");
		String cognomeParam = request.getParameter("cognome");
		String usernameParam = request.getParameter("username");
		String passwordParam = request.getParameter("password");
		String confermaPasswordParam = request.getParameter("conpassword");
		String[] ruoliParam = request.getParameterValues("ruolo");
		String statoParam = request.getParameter("stato");
		
		
		Utente example = new Utente(Long.parseLong(idParam), usernameParam, passwordParam, nomeParam, cognomeParam);

		try {
			if(!passwordParam.equals(confermaPasswordParam)) {
				request.setAttribute("update_utente_attr", example);
				request.setAttribute("ruoli_list_attribute",
						MyServiceFactory.getRuoloServiceInstance().listAll());
				request.setAttribute("errorMessage", "Attenzione, Conferma password diversa da password");
				request.getRequestDispatcher("/utente/update.jsp").forward(request, response);
				return;
			}
			
			example.setDateCreated(new Date());
			example.setStato(StatoUtente.valueOf(statoParam));
			
			if(!UtilityForm.validateUtenteBean(example)) {
				request.setAttribute("update_utente_attr", example);
				request.setAttribute("ruoli_list_attribute",
						MyServiceFactory.getRuoloServiceInstance().listAll());
				request.setAttribute("errorMessage", "Attenzione sono presenti errori di validazione");
				request.getRequestDispatcher("/utente/update.jsp").forward(request, response);
				return;
			}		
			
			for (String ruoloItem : ruoliParam) {
				example.getRuoli().add(MyServiceFactory.getRuoloServiceInstance().caricaSingoloElemento(Long.parseLong(ruoloItem)));
			}
			
			MyServiceFactory.getUtenteServiceInstance().aggiorna(example);
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Attenzione si è verificato un errore.");
			request.getRequestDispatcher("/home").forward(request, response);
			return;
		}
		response.sendRedirect("ExecuteListUtenteServlet?operationResult=SUCCESS");
	}

}
