package hr.fer.zemris.java.hw14.db.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import hr.fer.zemris.java.hw14.db.seed.SeedUtil;

/**
 * Razred koji se koristi kao bibliotka statičkih metoda preko kojih se
 * inicijalizira baza podataka. Popis metoda prikazan je u nastavku:
 * <ul>
 * <li>{@link #isTableEmpty(Connection, String)}</li>
 * <li>{@link #tableAlreadyExists(SQLException)}</li>
 * <li>{@link #initializeDatabase(Connection)}</li>
 * <li>{@link #createPolls(Connection)}</li>
 * <li>{@link #createPollOptions(Connection)}</li>
 * </ul>
 * 
 * @author Davor Češljaš
 */
public class SQLUtil {

	/** Konstanta koja predstavlja naziv tablice sa anketnim pitanjima */
	public static final String POLLS_DB_NAME = "Polls";

	/**
	 * Konstanta koja predstavlja naziv tablice sa odgovorima na anketna pitanja
	 */
	public static final String POLL_OPTIONS_DB_NAME = "PollOptions";

	/**
	 * Privatni konstruktor koji služi tome da se primjerci ovog razreda ne mogu
	 * stvarati izvan samog razreda.
	 */
	private SQLUtil() {
	}

	/**
	 * Statička metoda koja na temelju predane veze sa bazom podataka
	 * predstavljene sa sučeljem {@link Connection} provjerava postoji li
	 * tablica sa imenom <b>tableName</b> te ima li u njoj zapisa
	 *
	 * @param con
	 *            veze sa bazom podataka predstavljene sa sučeljem
	 *            {@link Connection}
	 * @param tableName
	 *            naziv tablice unutar baze podataka čija se prisutnost, odnosno
	 *            napunjenost provjerava
	 * @return <code>true</code> ukoliko u tablici ne postoji niti jedan unos
	 *         ili ukoliko tablica ne postoji u bazi podataka,
	 *         <code>false</code> inače
	 */
	public static boolean isTableEmpty(Connection con, String tableName) {
		try (PreparedStatement pst = con.prepareStatement("select * from " + tableName);
				ResultSet rset = pst.executeQuery()) {

			if (rset != null && rset.next()) {
				return false;
			}

		} catch (SQLException e) {
		}

		return true;
	}

	/**
	 * Statička metoda koja provjerava status bačene iznimke, koja me modelirana
	 * razredom {@link SQLException}. Ukoliko je njezin status jednak "X0Y32",
	 * metoda zaključuje da tablica već postoji u bazi podataka
	 *
	 * @param exception
	 *            primjerak razreda {@link SQLException} koji je bačen
	 * @return <code>true</code> ukoliko status ukazuje da tablica već postoji u
	 *         bazi podataka, <code>false</code> inače
	 */
	public static boolean tableAlreadyExists(SQLException exception) {
		return exception.getSQLState().equals("X0Y32");
	}

	/**
	 * Statička metoda koja inicijalizira bazu podataka. Ukoliko tablica
	 * {@value #POLLS_DB_NAME} već postoji u memoriji te ukoliko je ona
	 * napunjena metoda samo kreira tablicu {@value #POLL_OPTIONS_DB_NAME}, ako
	 * ona nije prisutna u bazi podataka. Ukoliko su obe tablice barem prazne
	 * (dakle mogu postojati u bazi), one se pune inicijalnim vrijednostima
	 * preko {@link SeedUtil} biblioteke.
	 *
	 * @param con
	 *            veze sa bazom podataka predstavljene sa sučeljem
	 *            {@link Connection}
	 * @throws IOException
	 *             Ukoliko metode za stvaranje ili metode za punjenje tablice
	 *             bace ovu iznimku
	 * 
	 * @see SeedUtil
	 */
	public static void initializeDatabase(Connection con) throws IOException {
		List<Long> foreignKeys = null;
		if (isTableEmpty(con, POLLS_DB_NAME)) {
			createPolls(con);
			foreignKeys = SeedUtil.seedPolls(con);
		}

		if (isTableEmpty(con, POLL_OPTIONS_DB_NAME)) {
			createPollOptions(con);

			if (foreignKeys != null) {
				SeedUtil.seedPollOptions(con, foreignKeys);
			}
		}
	}

	/**
	 * Statička metoda koja vrši izradu tablice {@value #POLLS_DB_NAME} unutar
	 * baze podataka. Metoda ukoliko ne uspije kreirati tablicu baca
	 * {@link IOException}
	 *
	 * @param con
	 *            veze sa bazom podataka predstavljene sa sučeljem
	 *            {@link Connection}
	 * @throws IOException
	 *             Ukoliko metoda nije uspjela kreirati tablicu
	 */
	public static void createPolls(Connection con) throws IOException {
		try {
			PreparedStatement pst = con
					.prepareStatement("CREATE TABLE Polls " 
							+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
							+ "title VARCHAR(150) NOT NULL," 
							+ "message CLOB(2048) NOT NULL)");
			pst.executeUpdate();
		} catch (SQLException e) {
			if (!tableAlreadyExists(e)) {
				throw new IOException("Ne mogu kreirati tablicu 'Polls'", e);
			}
		}
	}

	/**
	 * Statička metoda koja vrši izradu tablice {@value #POLL_OPTIONS_DB_NAME}
	 * unutar baze podataka. Metoda ukoliko ne uspije kreirati tablicu baca
	 * {@link IOException}
	 *
	 * @param con
	 *            veze sa bazom podataka predstavljene sa sučeljem
	 *            {@link Connection}
	 * @throws IOException
	 *             Ukoliko metoda nije uspjela kreirati tablicu
	 */
	public static void createPollOptions(Connection con) throws IOException {
		try {
			PreparedStatement pst = con.prepareStatement(
					"CREATE TABLE PollOptions " 
							+ "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
							+ "optionTitle VARCHAR(100) NOT NULL," 
							+ "optionLink VARCHAR(150) NOT NULL,"
							+ "pollID BIGINT," + "votesCount BIGINT," 
							+ "FOREIGN KEY (pollID) REFERENCES Polls(id))");
			pst.executeUpdate();
		} catch (SQLException e) {
			if (!tableAlreadyExists(e)) {
				throw new IOException("Ne mogu kreirati tablicu 'PollOptions'", e);
			}
		}
	}
}
