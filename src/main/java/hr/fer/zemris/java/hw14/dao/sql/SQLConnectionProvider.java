package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;

/**
 * Razred koji se koristi za pohranu veza prema bazi podataka unutar primjerka
 * razreda {@link ThreadLocal}. ThreadLocal je zapravo mapa čiji su ključevi
 * identifikator dretve koji radi operaciju nad mapom.
 * 
 * @see ThreadLocal
 * 
 * @author Davor Češljaš
 *
 */
public class SQLConnectionProvider {

	private static ThreadLocal<Connection> connections = new ThreadLocal<>();

	/**
	 * Metoda koja se koristi za postavljanje veze za trenutnu dretvu. Kao
	 * parametar ove metode predaje se primjerak razreda koji implementira
	 * sučelje {@link Connection} te modelira vezu prema bazi podataka. Ukoliko
	 * se kao parametar preda <code>null</code> metoda uklanja unos u mapi
	 * konekcija za trenutnu dretvu.
	 * 
	 * @param con
	 *            primjerak razreda koji implementira sučelje {@link Connection}
	 *            te modelira vezu prema bazi podataka
	 */
	public static void setConnection(Connection con) {
		if (con == null) {
			connections.remove();
		} else {
			connections.set(con);
		}
	}

	/**
	 * Metoda koja se koristi za dohvat konekcije prema bazi podataka modelirane
	 * sučelje {@link Connection} za trenutnu dretvu
	 * 
	 * @return konekcija prema bazi podataka modelirane sučelje
	 *         {@link Connection} za trenutnu dretvu
	 */
	public static Connection getConnection() {
		return connections.get();
	}

}