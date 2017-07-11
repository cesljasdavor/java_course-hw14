package hr.fer.zemris.java.hw14.app.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Razred koji nasljeđuje razred {@link HttpServlet}. Primjerci ovog razreda
 * koriste se za redirekciju upita na resurs index.html servletu koji je
 * modeliran razredom {@link HomeServlet}, a pomoću metode
 * {@link HttpServletResponse#sendRedirect(String)}
 * 
 * @see HttpServlet
 * @see HomeServlet
 * @see HttpServletResponse#sendRedirect(String)
 * 
 * @author Davor Češljaš
 */
@WebServlet(name = "toHomeRedirection", urlPatterns = { "/index.html" })
public class ToHomeRedirectionServlet extends HttpServlet {

	/**
	 * Konstanta koja se koristi prilikom serijalizacije objekata ovog razreda
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("servleti/index.html ");
	}

}
