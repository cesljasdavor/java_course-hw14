package hr.fer.zemris.java.hw14.app.servlets.voting;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw14.app.util.ServletUtil;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * Razred koji nasljeđuje razred {@link HttpServlet}. Primjerci ovog razreda
 * pozivom metode {@link #doGet(HttpServletRequest, HttpServletResponse)}
 * ažuriraj broj glasova odgovoru na anketno pitanje čiji je identifikator
 * predan kao klijentov parametar "id". Uz "id" ovom servletu potrebno je
 * predati i parametar "pollID" kako bi se mogli prikazati rezultati tražene
 * ankete.  Ažuriranje se neće dogoditi ukoliko vrijednost uz ključ "id" nije
 * cijeli broj ili ukoliko taj cijeli broj nije jedan od poznatih identifikatora
 * odgovra  za to pitanje.
 * 
 * @see HttpServlet
 * 
 * @author Davor Češljaš
 */
@WebServlet(name = "voting-vote", urlPatterns = { "/servlets/voting-vote", "/servleti/glasanje-glasaj" })
public class VotingVoteServlet extends HttpServlet {

	/**
	 * Konstanta koja se koristi prilikom serijalizacije objekata ovog razreda
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer pollOptionID = ServletUtil.checkAndGetValue(request, "id");
		Integer pollID = ServletUtil.checkAndGetValue(request, "pollID");

		if (pollOptionID == null || pollID == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		if (!DAOProvider.getDao().updateVotesCount(pollOptionID, pollID)) {
			ServletUtil.sendVotingError(request, response,
					String.format(
							"Nažalost, nismo uspjeli zabilježiti Vaš glas jer u našoj bazi ne postoji odgovor sa pollID=%d i id=%d",
							pollID, pollOptionID));
			return;
		}

		response.sendRedirect(request.getContextPath() + "/servleti/glasanje-rezultati?pollID=" + pollID);
	}
}
