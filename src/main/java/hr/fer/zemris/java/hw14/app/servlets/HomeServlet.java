package hr.fer.zemris.java.hw14.app.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.app.models.Poll;
import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * Razred koji nasljeđuje razred {@link HttpServlet}. Primjerci ovog razreda
 * unutar svoje metoda {@link #doGet(HttpServletRequest, HttpServletResponse)}
 * učitava sve primjerke razreda {@link Poll} čije vrijednosti postoje unutar
 * baze podataka. Potom dobivene primjerke sprema koristeći
 * {@link ServletContext#setAttribute(String, Object)} Nakon toga iscratvanje
 * odgovora prepuštaju JSP datoteci sa putanjom "/WEB-INF/pages/index.jsp", koja
 * potom
 * 
 * @see HttpServlet
 * @see Poll
 * 
 * @author Davor Češljaš
 */
@WebServlet(name = "home", urlPatterns = { "/servleti/index.html" })
public class HomeServlet extends HttpServlet {

	/**
	 * Konstanta koja se koristi prilikom serijalizacije objekata ovog razreda
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DAO dao = DAOProvider.getDao();
		List<Poll> polls = dao.getPolls();

		request.setAttribute("polls", polls);

		request.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(request, response);
	}

}
