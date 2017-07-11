package hr.fer.zemris.java.hw14.app.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import hr.fer.zemris.java.hw14.app.models.PollOption;

/**
 * Razred koji se koristi kao pomoćna biblioteka za sve servlete unutar ove
 * aplikacije. Ovom razredu ne mogu se stvarati primjerci. Razred nudi sljedeće
 * metode:
 * <ul>
 * <li>{@link #createPieChart(String, PieDataset)}</li>
 * <li>{@link #checkAndGetValue(HttpServletRequest, String)}</li>
 * <li>{@link #sendVotingError(HttpServletRequest, HttpServletResponse, String)}</li>
 * </ul>
 * 
 * @see PollOption
 * @see JFreeChart
 * 
 * @author Davor Češljaš
 */
public class ServletUtil {

	/**
	 * Konstanta koja predstavlja ekstenziju datoteke slike koja se stvara u
	 * okviru metode {@link #createPieChart(String, PieDataset)}
	 */
	private static final String IMAGE_EXTENSION = "png";

	/**
	 * Konstanta koja predstavlja primjerak razreda koji implementira sučelje
	 * {@link Comparator}. Ovaj komparator primjerke razreda {@link PollOption}
	 * uspoređuje na temelju broja prikupljenih glasova
	 */
	public static final Comparator<PollOption> POLL_OPTIONS_COMPARATOR = (po1,
			po2) -> (int) Math.signum(po1.getVotesCount() - po2.getVotesCount());

	/**
	 * Privatni konstruktor koji služi tome da se primjerci ovog razreda ne mogu
	 * stvarati izvan samog razreda.
	 */
	private ServletUtil() {
	}

	/**
	 * Metoda koja se koristi za stvaranje primjerka razreda {@link JFreeChart}
	 * koji predstavlja kružni dijagram. Podatke koji će se iscrtati kao i
	 * naslov kružnog dijagrama metoda dobiva kroz parametre <b>dataset</b> i
	 * <b>chartTitle</b>. Nakon što se uspješno stvori primjerak razreda
	 * {@link JFreeChart}, on se pretvara u sliku formata
	 * {@value #IMAGE_EXTENSION}.
	 *
	 * @param chartTitle
	 *            naslov koji će biti ispisan uz kružni dijagaram
	 * @param dataset
	 *            podaci iz kojih se generira kružni dijagram
	 * @return polje okteta koje predsatvlja sliku generiranog kružnog dijagrama
	 *         u formatu {@value #IMAGE_EXTENSION}
	 */
	public static byte[] createPieChart(String chartTitle, PieDataset dataset) {
		JFreeChart pieChart = ChartFactory.createPieChart3D(chartTitle, dataset, true, true, false);

		PiePlot3D plot = (PiePlot3D) pieChart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(1.0f);

		return toImageBytes(pieChart);
	}

	/**
	 * Pomoćna metoda koja iz predanog parametra koji je primjerak razreda
	 * {@link JFreeChart} stvara polje okteta koje predstavlja sliku kružnog
	 * dijagrama formata {@value #IMAGE_EXTENSION}. Slika će biti široka 600, a
	 * visoka 500 piksela i ovaj podatak nije moguće mijenjati
	 *
	 * @param pieChart
	 *            primjerak razreda {@link JFreeChart} koji modelira kružni
	 *            dijagram koji se pretvara u sliku
	 * @return polje okteta koje predstavlja sliku kružnog dijagrama formata
	 *         {@value #IMAGE_EXTENSION}. Slika će biti široka 600, a visoka 500
	 *         piksela i ovaj podatak nije moguće mijenjati
	 */
	private static byte[] toImageBytes(JFreeChart pieChart) {
		BufferedImage bim = pieChart.createBufferedImage(600, 500);
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			ImageIO.write(bim, IMAGE_EXTENSION, os);
		} catch (IOException e) {
			return null;
		}

		return os.toByteArray();
	}

	/**
	 * Metoda koja iz predanog primjerka razreda koji implementira sučelje
	 * {@link HttpServletRequest} dohvaća vrijednost parametra pod nazivom
	 * <b>name</b>. Ukoliko ta vrijednost ne postoji ili se ne može parsirati u
	 * cijeli broj metoda vraća <code>null</code>.
	 *
	 * @param request
	 *            klijentov zahtjev modeliran sučeljem
	 *            {@link HttpServletRequest}
	 * @param name
	 *            ključ pod kojim je vrijednost parametra unutar klijentovog
	 *            zahtjeva modeliranog sa {@link HttpServletRequest}
	 * @return vrijednost mapiranu pod ključem <b>name</b> unutar parametara
	 *         koje je korisnik predao ili <code>null</code> u slučaju neke od
	 *         gore opisanih pogrešaka
	 */
	public static Integer checkAndGetValue(HttpServletRequest request, String name) {
		String value = request.getParameter(name);

		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException ignorable) {
			}
		}

		return null;
	}

	/**
	 * Pomoćna metoda koja se koristi za slanje upozorenja korisniku da je došlo
	 * do greške prilikom glasanja unutar pojedinog anketnog pitanja. Metoda će
	 * pospremiti predani parametar <b>message</b> kao atribut predanog zahtjeva
	 * te će generiranje HTML dokumenta upozorenja prepusiti JSP dokumentu sa
	 * putanjom "/WEB-INF/pages/voting-error.jsp"
	 *
	 * @param request
	 *            primjerak razreda koji implementira sučelje
	 *            {@link HttpServletRequest}, a koji modelira kontekst zahtjeva
	 * @param response
	 *            primjerak razreda koji implementira sučelje
	 *            {@link HttpServletResponse}, a koji modelira kontekst odgovora
	 * @param message
	 *            poruka upozorenja koju je korisniku potrebno vratiti u okviru
	 *            izgeneriranog HTML dokumenta
	 * @throws ServletException
	 *             ukoliko resurs kojemu delegiramo iscrtavanje baci ovu iznimku
	 * @throws IOException
	 *             ukoliko ne postoji JSP dokument sa putanjom
	 *             "/WEB-INF/pages/voting-error.jsp"
	 */
	public static void sendVotingError(HttpServletRequest request, HttpServletResponse response, String message)
			throws ServletException, IOException {
		request.setAttribute("message", message);
		request.getRequestDispatcher("/WEB-INF/pages/voting-error.jsp").forward(request, response);
	}
}
