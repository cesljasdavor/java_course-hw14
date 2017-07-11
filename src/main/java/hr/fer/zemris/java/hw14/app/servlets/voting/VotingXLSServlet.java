package hr.fer.zemris.java.hw14.app.servlets.voting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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
 * modelirani razredom {@link PollOption}. Iz dohvaćene liste metoda generira
 * .xls datoteku.
 * <p>
 * Ova datoteka imati će samo jednu stranicu na kojoj će biti četiri stupca.
 * Prvi stupac će predstavljati identifikator, drugi stupac naziv, treći link na
 * određeni resurs na internetu, a četvrti će predstavljati broj glasova za taj
 * odgovor na anketno pitanje
 * </p>
 * 
 * @see HttpServlet
 * @see PollOption
 * 
 * @author Davor Češljaš
 */
@WebServlet(name = "voting-xls", urlPatterns = { "/servleti/glasanje-xls", "/servlets/voting-xls" })
public class VotingXLSServlet extends HttpServlet {

	/**
	 * Konstanta koja se koristi prilikom serijalizacije objekata ovog razreda
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstanta koja predstavlja poziciju u redku na kojoj se nalazi
	 * identifikator odgovora na anketno pitanje
	 */
	private static final int ID_INDEX = 0;

	/**
	 * Konstanta koja predstavlja poziciju u redku na kojoj se nalazi naziv
	 * odgovora na anketno pitanje
	 */
	private static final int POLL_OPTION_INDEX = 1;

	/**
	 * Konstanta koja predstavlja poziciju u redku na kojoj se nalazi link na
	 * neki resurs na internetu za odgovora na anketno pitanje
	 */
	private static final int POLL_OPTION_LINK_INDEX = 2;

	/**
	 * Konstanta koja predstavlja poziciju u redku na kojoj se nalazi broj
	 * glasova za odgovora na anketno pitanje
	 */
	private static final int VOTES_INDEX = 3;

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

		response.setContentType("application/vnd.ms-excel");
		response.getOutputStream().write(createVotingXLS(pollOptions, poll));
	}

	/**
	 * Pomoćna metoda koja generira .xls datoteku sa rezultatima glasanja iz
	 * predanog parametra <b>pollOptions</b>. Ova datoteka imati će samo jednu
	 * stranicu na kojoj će biti četiri stupca. Prvi stupac će predstavljati
	 * identifikator, drugi stupac naziv, treći link na neki resurs na
	 * internetu, a četvrti će predstavljati broj glasova za to anketno pitanje.
	 * 
	 *
	 * @param pollOptions
	 *            {@link List} primjeraka razreda {@link PollOption} iz koje se
	 *            stvara .xls datoteka
	 * @return polje okteta koje predstavlja stvorenu .xls datoteku
	 */
	private byte[] createVotingXLS(List<PollOption> pollOptions, Poll poll) {
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
			setWorkbookInfo(workbook, poll);

			HSSFSheet sheet = workbook.createSheet("Rezultati glasanja");
			createFirstRow(sheet);
			for (int row = 1, noOfRows = pollOptions.size(); row <= noOfRows; row++) {
				createEntry(pollOptions.get(row - 1), sheet.createRow(row));
			}

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			workbook.write(os);

			return os.toByteArray();
		} catch (IOException ignorable) {
		}
		return null;
	}

	/**
	 * Pomoćna metoda koja postaljva naziv , autora te vrijeme nastajanja .xls
	 * dokumenta koji je predstavljen razredom {@link HSSFWorkbook}.
	 *
	 * @param workbook
	 *            primjerak razreda {@link HSSFWorkbook} koji modelira .xls
	 *            datoteku, a čije se informacije namještaju
	 * @param poll
	 *            primjerak razreda {@link Poll} koji modelira anketno pitanje.
	 *            Ovdje se koristi za dohvat naziva anketnog pitanja.
	 */
	private void setWorkbookInfo(HSSFWorkbook workbook, Poll poll) {
		workbook.createInformationProperties();
		SummaryInformation info = workbook.getSummaryInformation();
		info.setTitle(poll.getTitle());
		info.setAuthor("Davor Češljaš");
		info.setCreateDateTime(new Date());
	}

	/**
	 * Pomoćna metoda koja stvara jedan redak (koji predstavlja podatke za jedan
	 * odgovr na anketno pitanje) .xls dokumenta. Detaljniji opis svakog stupca
	 * moguće je pronaći u dokumentaciji ovog razreda
	 *
	 * @param pollOption
	 *            primjerak razreda {@link PollOption} iz kojeg se stvara redak
	 * @param row
	 *            primjerak razreda {@link HSSFRow} koji modelira jedan redak
	 *            stranice .xls dokumenta
	 */
	private void createEntry(PollOption pollOption, HSSFRow row) {
		row.createCell(ID_INDEX).setCellValue(String.valueOf(pollOption.getId()));
		row.createCell(POLL_OPTION_INDEX).setCellValue(pollOption.getOptionTitle());
		row.createCell(POLL_OPTION_LINK_INDEX).setCellValue(pollOption.getOptionLink());
		row.createCell(VOTES_INDEX).setCellValue(String.valueOf(pollOption.getVotesCount()));
	}

	/**
	 * Pomoćna metoda koja stvara prvi redak datoteke. U prvom redku navedeni su
	 * opisi pojedinih stupaca. Tako će u prvom stupcu biti zapisan "ID", u
	 * drugom "Odgovor" u trećem "Link odgovora", a u četvrtom "Broj glasova".
	 * Za detaljnija značenja pojedinih stupaca korisnika se navodi na
	 * dokumentaciju ovog razreda.
	 *
	 * @param sheet
	 *            primjerak razreda {@link HSSFSheet} koji modelira jednu
	 *            stranicu .xls datoteke
	 */
	private void createFirstRow(HSSFSheet sheet) {
		HSSFRow row = sheet.createRow(0);

		row.createCell(ID_INDEX).setCellValue("ID");
		row.createCell(POLL_OPTION_INDEX).setCellValue("Odgovor");
		row.createCell(POLL_OPTION_LINK_INDEX).setCellValue("Link odgovora");
		row.createCell(VOTES_INDEX).setCellValue("Broj glasova");
	}

}
