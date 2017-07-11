package hr.fer.zemris.java.hw14.app.servlets.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.app.models.Poll;
import hr.fer.zemris.java.hw14.app.models.PollOption;
import hr.fer.zemris.java.hw14.app.util.ServletUtil;
import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * Razred koji nasljeđuje razred {@link HttpServlet}. Primjerci ovog razreda
 * pozivom metode {@link #doGet(HttpServletRequest, HttpServletResponse)} preko
 * veze s bazom podataka koju pruža {@link DAOProvider#getDao()} dohvaćaju
 * anketu s određenim identifikatorom koji je predan kao argument zahtjeva, te
 * {@link List} svih mogućih odgovora za to anketno pitanje, a koji su
 * modelirani razredom {@link PollOption}. Potom tu listu i primjerak razreda
 * {@link Poll} koji modelira anketno pitanje postavlja kao atribute zahtjeva.
 * Nakon toga generiranje HTML dokumenta deliegiraju JSP datoteci
 * "/WEB-INF/pages/votingIndex.jsp".
 * 
 * @see HttpServlet
 * @see PollOption
 * @see DAO
 * @see DAOProvider
 * 
 * @author Davor Češljaš
 */
@WebServlet(name = "voting", urlPatterns = { "/servleti/glasanje", "/servlets/voting" })
public class VotingServlet extends HttpServlet {

	/**
	 * Konstanta koja se koristi prilikom serijalizacije objekata ovog razreda
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer pollID = ServletUtil.checkAndGetValue(request, "pollID");

		if (pollID == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		DAO dao = DAOProvider.getDao();
		Poll poll = dao.getPoll(pollID);
		if (poll == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		List<PollOption> pollOptions = dao.getPollOptions(pollID);
		if (pollOptions.isEmpty()) {
			ServletUtil.sendVotingError(request, response,
					"Nažalost, za ovu anketu u našoj bazi podataka ne nalazi se niti jedan unos");
			return;
		}

		request.setAttribute("pollOptions", pollOptions);
		request.setAttribute("poll", poll);
		request.getRequestDispatcher("/WEB-INF/pages/votingIndex.jsp").forward(request, response);
	}
}
