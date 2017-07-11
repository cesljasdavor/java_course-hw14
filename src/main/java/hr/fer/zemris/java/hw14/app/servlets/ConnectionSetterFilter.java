package hr.fer.zemris.java.hw14.app.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

import hr.fer.zemris.java.hw14.dao.sql.SQLConnectionProvider;

/**
 * Razred koji implementira sučelje {@link Filter}. Primjerci ovog razreda
 * koriste se kako bi presreli zahtjev korisnika za određeni resurs na
 * postlužitelju te, prije no što odgovor proslijede resursu uspostave konekciju
 * sa bazom podataka za ovu dretvu preko {@link SQLConnectionProvider}. U
 * trenutku kada resurs krene vraćati odgovor, primjerci ovog razreda presreću i
 * odgovor te zatvaraju konekciju preko istog razreda.
 * 
 * @see Filter
 * @see SQLConnectionProvider
 * 
 * @author Davor Češljaš
 */
@WebFilter(filterName = "f1", urlPatterns = { "/servleti/*" })
public class ConnectionSetterFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		DataSource ds = (DataSource) request.getServletContext().getAttribute("hr.fer.zemris.dbpool");

		try (Connection con = ds.getConnection()) {
			SQLConnectionProvider.setConnection(con);
			// prepušta stvar obradi (servletu ili nekom statičkom resursu)
			chain.doFilter(request, response);
		} catch (SQLException e) {
			throw new IOException("Baza podataka nije dostupna.", e);
		} finally {
			SQLConnectionProvider.setConnection(null);
		}
	}

}