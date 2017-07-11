package hr.fer.zemris.java.hw14.app.servlets;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import hr.fer.zemris.java.hw14.db.util.SQLUtil;

/**
 * Razred koji implementira sučelje {@link ServletContextListener}. Primjerci
 * ovog razreda prilikom pokretanja poslužitelja uspostavljaju vezu s bazom
 * podataka kreirajući {@link ComboPooledDataSource}. Potom po potrebi
 * inicijaliziraju bazu podataka ukoliko je ona prazna ili ukoliko potrebne
 * tablice ne postoje u bazi podataka. Ukoliko i jedna od ovih akcija završi s
 * iznimkom, ovaj razred zaustavlja rad psolužitelja pozivom
 * {@link System#exit(int)}. Ukoliko je sve korektno inicijalizirano, ovaj
 * razred čeka na kraj rada serevera te prilikom gašenja otpušta sve zauzete
 * resurse.
 * 
 * @see ServletContextListener
 * @see ComboPooledDataSource
 * 
 * @author Davor Češljaš
 */
@WebListener
public class DatabaseInitializationListener implements ServletContextListener {

	/**
	 * Konstanta koja predstavlja niz znakova koji predstavljaju putanju do
	 * datoteke sa postavkama za konekciju s bazom podataka.
	 */
	private static final String PROPERTIES_PATH = "/WEB-INF/dbsettings.properties";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ComboPooledDataSource cpds = null;
		try {
			cpds = loadDBConnectionPool(sce.getServletContext());
		} catch (IOException e) {
			System.exit(1);
		}

		try (Connection con = cpds.getConnection()) {
			SQLUtil.initializeDatabase(con);
		} catch (SQLException | IOException e) {
			System.exit(1);
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds == null) {
			return;
		}

		try {
			DataSources.destroy(cpds);
		} catch (SQLException igonrable) {
		}
	}

	/**
	 * Pomoćna metoda koja stvara primjerak razreda
	 * {@link ComboPooledDataSource}. Kako bi stvorila primjerak ovog razreda
	 * metoda mora pročitati sadržaj konfiguracijske datoteke predsatvljene sa
	 * {@link #PROPERTIES_PATH}.
	 *
	 * @param sc
	 *            primjerak razreda koji implementira sučelje
	 *            {@link ServletContext}, a koji se koristi za određivanje
	 *            apsolutnih putanja do konfiguracijske datoteke
	 * @return primjerak razreda {@link ComboPooledDataSource} koji modelira
	 *         bazen konekcija sa bazom podataka
	 * @throws IOException
	 *             Ukoliko datoteku sa putanjom {@link #PROPERTIES_PATH} nije
	 *             moguće otvoriti za čitanje
	 */
	private ComboPooledDataSource loadDBConnectionPool(ServletContext sc) throws IOException {
		Properties properties = new Properties();
		properties.load(Files.newInputStream(Paths.get(sc.getRealPath(PROPERTIES_PATH))));
		// jdbc:derby://localhost:1527/baza1DB
		String connectionURL = String.format("jdbc:derby://%s:%s/%s", properties.getProperty("host"),
				properties.getProperty("port"), properties.getProperty("name"));

		ComboPooledDataSource cpds = new ComboPooledDataSource();

		String driverClass = "org.apache.derby.jdbc.ClientDriver";
		try {
			cpds.setDriverClass(driverClass);
		} catch (PropertyVetoException e) {
			throw new IOException("Ne mogu učitati driver: " + driverClass);
		}
		cpds.setJdbcUrl(connectionURL);
		cpds.setUser(properties.getProperty("user"));
		cpds.setPassword(properties.getProperty("password"));

		return cpds;
	}

}
