package hr.fer.zemris.java.hw14.app.servlets.voting;

import java.io.IOException;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

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
 * {@link List} svih mogućih odgovora za to anketno pitanje, a koji su modelirani
 * razredom {@link PollOption}. Potom metoda iz liste odgovora na anketno
 * pitanje filtrira sve primjerke razreda {@link PollOption} koji imaju najveći
 * broj glasova u zasebnu {@link List}u. Ove dvije liste metoda postavlja kao
 * atribute zahtjeva i generiranje HTML dokumenta prepušta JSP datoteci
 * "/WEB-INF/pages/votingRes.jsp".
 * 
 * @see HttpServlet
 * @see PollOption
 * @see DAO
 * @see DAOProvider
 * 
 * @author Davor Češljaš
 */
@WebServlet(name = "voting-results", urlPatterns = { "/servleti/glasanje-rezultati", "/servlets/voting-results" })
public class VotingResultsServlet extends HttpServlet {

	/**
	 * Konstanta koja se koristi prilikom serijalizacije objekata ovog razreda
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer pollID = ServletUtil.checkAndGetValue(request, "pollID");

		if (pollID == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		DAO dao = DAOProvider.getDao();
		List<PollOption> pollOptions = dao.getPollOptions(pollID);
		Poll poll = dao.getPoll(pollID);

		if (poll == null || pollOptions.isEmpty()) {
			ServletUtil.sendVotingError(request, response,
					String.format("Nažalost za parametar pollId=%d ne postoje rezultati", pollID));
			return;
		}
		pollOptions.sort(ServletUtil.POLL_OPTIONS_COMPARATOR.reversed());
		request.setAttribute("pollOptions", pollOptions);
		request.setAttribute("poll", poll);

		List<PollOption> winners = findWinners(pollOptions);
		request.setAttribute("winners", winners);

		request.getRequestDispatcher("/WEB-INF/pages/votingRes.jsp").forward(request, response);
	}

	/**
	 * Pomoćna metoda koja iz predanog parametra <b>pollOptions</b> dohvaća
	 * reference na one odgovore na anketna pitanja modelirane primjerkom
	 * razreda {@link PollOption} koji su skupili najveći broj glasova.
	 * Dohvaćene reference sprema u novu {@link List}u i tu listu vraća
	 * pozivatelju.
	 *
	 * @param pollOptions
	 *            {@link List} primjeraka razreda {@link PollOption} iz koje se
	 *            dohvaćaju pobjednici
	 * @return nova {@link List} primjeraka razreda {@link PollOption} koja
	 *         sadrži odgovore s najvećim brojem glasova
	 */
	private List<PollOption> findWinners(List<PollOption> pollOptions) {
		long max = findMaxVotes(pollOptions);
		return pollOptions.stream().filter(po -> po.getVotesCount() == max).collect(Collectors.toList());
	}

	/**
	 * Pomoćna metoda koja traži najveći broj glasova koje neki primjerak
	 * razreda {@link PollOption} iz predane liste ima spremljeno. Potom taj
	 * broj glasova vraća pozivatelju. Maksimalni broj glasova biti će 0 ukoliko
	 * unutar predanog parametra <b>pollOptions</b> ne postoji niti jedan
	 * primjerak razreda {@link PollOption}, drugim riječima ukoliko je predana
	 * lista prazna
	 *
	 * @param pollOptions
	 *            {@link List} primjeraka razreda {@link PollOption} iz koje se
	 *            dohvaća najveći broj glasova
	 * @return najveći broj glasova koje je neki odgovor na anketno pitanje (ili
	 *         više njih) uspio skupiti
	 */
	private long findMaxVotes(List<PollOption> pollOptions) {
		OptionalLong optMax = pollOptions.stream().mapToLong(po -> po.getVotesCount()).max();

		if (optMax.isPresent()) {
			return optMax.getAsLong();
		}

		return 0;
	}

}
