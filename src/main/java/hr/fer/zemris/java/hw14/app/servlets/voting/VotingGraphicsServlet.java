package hr.fer.zemris.java.hw14.app.servlets.voting;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import hr.fer.zemris.java.hw14.app.models.PollOption;
import hr.fer.zemris.java.hw14.app.util.ServletUtil;
import hr.fer.zemris.java.hw14.dao.DAOProvider;

/**
 * Razred koji nasljeđuje razred {@link HttpServlet}. Primjerci ovog razreda
 * pozivom metode {@link #doGet(HttpServletRequest, HttpServletResponse)} šalju
 * sliku formata "png" koja predstavlja kružni dijagram glasanja unutar jedne od
 * anketa. Za generiranje slike koristi se metoda
 * {@link ServletUtil#createPieChart(String, PieDataset)}.
 * 
 * @see ServletUtil
 * @see PieDataset
 * @see HttpServlet
 * 
 * @author Davor Češljaš
 */
@WebServlet(name = "voting-graphics", urlPatterns = { "/servleti/glasanje-grafika", "/servlets/voting-graphics" })
public class VotingGraphicsServlet extends HttpServlet {

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
		}

		PieDataset dataset = loadDataSet(pollID);
		if (dataset == null) {
			ServletUtil.sendVotingError(request, response, "Nažalost, nemamo dovoljno podataka za generirati sliku");
			return;
		}

		response.setContentType("image/png");
		response.getOutputStream().write(ServletUtil.createPieChart("Rezultati ankete", dataset));
	}

	/**
	 * Pomoćna metoda koja se koristi za učitavanje podataka iz kojih se
	 * generira slika koja predstavlja kružni dijagram.
	 *
	 * @param pollID
	 *            predstavlja identifikator jedne ankete modlirane razredom
	 *            {@link Poll}, a čiji se odgovori i brojevi glasova za te
	 *            odgovore prikazuju unutar kružnog dijagrama
	 * 
	 * @return primjerak razreda koje implementira sučelje {@link PieDataset}, a
	 *         koji modelira podatke korištene u izradi kružnog dijagrama
	 */
	private PieDataset loadDataSet(long pollID) {
		List<PollOption> pollOptions = DAOProvider.getDao().getPollOptions(pollID);

		if (pollOptions.isEmpty()) {
			return null;
		}

		DefaultPieDataset dataset = new DefaultPieDataset();
		for (PollOption pollOption : pollOptions) {
			dataset.setValue(pollOption.getOptionTitle(), pollOption.getVotesCount());
		}

		return dataset;
	}
}
