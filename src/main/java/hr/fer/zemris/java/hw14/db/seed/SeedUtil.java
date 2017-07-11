package hr.fer.zemris.java.hw14.db.seed;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Razred koji se koristi kao bibliotka statičkih metoda preko kojih se iz
 * datotečnih sustava učitavaju tzv. seed datoteke ili datoteke koje se koriste
 * za postaljanje inicijalnih vrijednosti unutar baze podataka.
 * 
 * @author Davor Češljaš
 */
public class SeedUtil {

	/**
	 * Konstanta koja predstavlja naziv direktorija unutar kojeg se nalaze svi
	 * odgovori na anketna pitanja koji bi inicijalno trebali postojati u bazi
	 * podataka
	 */
	private static final String POLL_OPTIONS_DIRECTORY = "/poll_options_seeds/";

	/**
	 * Konstanta koja se koristi prilikom parsiranja datoteka sa odgovorima na
	 * anketna pitanja
	 */
	private static final String POLL_OPTIONS_DELIMITER = "\t";

	/**
	 * Konstanta koja predstavlja naziv datoteke unutar koje se nalaze sva
	 * inicijalna anketna pitanja koja se trebaju ubaciti u bazu podataka
	 */
	private static final String POLLS_FILE = "polls.seed";

	/**
	 * Konstanta koja se koristi prilikom parsiranja datoteka sa anketnim
	 * pitanjima
	 */
	private static final String POLLS_ENTRY_DELIMITER = "=";

	/**
	 * Konstanta koja predstavlja ekstenziju datoteka koje se koriste za
	 * inicijalizaciju vrijednosti unutar baze podataka
	 */
	private static final String SEED_FILE_EXTENSION = ".seed";

	/**
	 * Konstanta koja se koristi kao gornja granica nasumično izgeneriranog
	 * broja glasova
	 */
	private static final int RANDOM_BOUND = 150;

	/**
	 * Privatni konstruktor koji služi tome da se primjerci ovog razreda ne mogu
	 * stvarati izvan samog razreda.
	 */
	private SeedUtil() {
	}

	/**
	 * Statička metoda koja se koristi za inicijalizaciju svih anketnih pitanja
	 * unutar baze podataka. Konstanta kao parametar prima primjerak sučelja
	 * {@link Connection} koji predstavlja vezu s bazom podataka koju treba
	 * inicijalizirati. Metoda vraća {@link List} izgeneriranih identifikatora
	 * anketnih pitanja predstavljenih razredom {@link Long}
	 *
	 * @param con
	 *            primjerak sučelja {@link Connection} koji predstavlja vezu s
	 *            bazom podataka koju treba inicijalizirati.
	 * @return {@link List} izgeneriranih identifikatora anketnih pitanja
	 *         predstavljenih razredom {@link Long}
	 * @throws IOException
	 *             Ukoliko nije moguće inicijalizirati bazu podataka ili ukoliko
	 *             nije moguće seed datoteku otvoriti za čitanje
	 */
	public static List<Long> seedPolls(Connection con) throws IOException {
		Set<SeedEntry> seeds = parseSeedFiles(POLLS_FILE, POLLS_ENTRY_DELIMITER);

		List<Long> generatedKeys = new ArrayList<>();
		for (SeedEntry seed : seeds) {
			Long insertStatus = insertToPolls(con, seed);

			if (insertStatus != null) {
				generatedKeys.add(insertStatus);
			}
		}

		return generatedKeys;
	}

	/**
	 * Pomoćna metoda koja se koristi za umetanje jednog zapisa unutar tablice
	 * 'Polls' u bazi podataka prema kojoj je veza modelirana parametrom
	 * <b>con</b>.
	 *
	 * @param con
	 *            primjerak sučelja {@link Connection} koji predstavlja vezu s
	 *            bazom podataka koju treba inicijalizirati.
	 * @param seed
	 *            primjerak razeda {@link SeedEntry} koji modelira jedan unos
	 *            koji je potrebno unijeti u tablicu 'Polls'
	 * @return identifikator generiran prilikom unosa u tablicu 'Polls'
	 */
	private static Long insertToPolls(Connection con, SeedEntry seed) {
		try (PreparedStatement pst = con.prepareStatement("INSERT INTO Polls (title, message) VALUES (?, ?)",
				Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, seed.firstParam);
			pst.setString(2, seed.secondParam);

			if (pst.executeUpdate() == 0) {
				return null;
			}

			try (ResultSet rset = pst.getGeneratedKeys()) {
				if (rset != null && rset.next()) {
					return rset.getLong(1);
				}
			}
		} catch (SQLException ignorable) {
		}

		return null;
	}

	/**
	 * Statička metoda koja se koristi za inicijalizaciju svih odgovora na
	 * anketna pitanja unutar baze podataka. Konstanta kao parametar prima
	 * primjerak sučelja {@link Connection} koji predstavlja vezu s bazom
	 * podataka koju treba inicijalizirati. Metoda vraća broj unesenih podataka
	 * u tablicu 'PollOptions'.
	 *
	 * @param con
	 *            primjerak sučelja {@link Connection} koji predstavlja vezu s
	 *            bazom podataka koju treba inicijalizirati.
	 * @param foreignKeys
	 *            strani ključevi koji se unutar 'PollOptions' tablice unose pod
	 *            atribut 'pollID', a ovisno o tome iz koje se po redu datoteke
	 *            čita
	 * @return broj unesenih podataka u tablicu 'PollOptions'.
	 * @throws IOException
	 *             Ukoliko nije moguće inicijalizirati bazu podataka ili ukoliko
	 *             nije moguće seed datoteku otvoriti za čitanje
	 */
	public static long seedPollOptions(Connection con, List<Long> foreignKeys) throws IOException {
		String poll_options_dir = SeedUtil.class.getClassLoader().getResource(POLL_OPTIONS_DIRECTORY).getPath();
		int seed_files_count = new File(poll_options_dir).list().length;

		Random rand = new Random();
		long insertedCount = 0;
		for (int i = 1, len = Math.min(seed_files_count, foreignKeys.size()); i <= len; i++) {
			Set<SeedEntry> seeds = parseSeedFiles(POLL_OPTIONS_DIRECTORY + i + SEED_FILE_EXTENSION,
					POLL_OPTIONS_DELIMITER);
			Long currentKey = foreignKeys.get(i - 1);

			for (SeedEntry seed : seeds) {
				if (insertToPollOptions(con, seed, currentKey, rand)) {
					insertedCount++;
				}
			}
		}

		return insertedCount;
	}

	/**
	 * Pomoćna metoda koja se koristi za umetanje jednog zapisa unutar tablice
	 * 'PollOptions' u bazi podataka prema kojoj je veza modelirana parametrom
	 * <b>con</b>.
	 *
	 * @param con
	 *            primjerak sučelja {@link Connection} koji predstavlja vezu s
	 *            bazom podataka koju treba inicijalizirati.
	 * @param seed
	 *            primjerak razeda {@link SeedEntry} koji modelira jedan unos
	 *            koji je potrebno unijeti u tablicu 'PollOptions'
	 * @param currentKey
	 *            identifikator koji se koristi kao atribut 'pollID' za predani
	 *            <b>seed</b>
	 * @param rand
	 *            generator slučajnih brojeva koji se koristi za generiranje
	 *            slučajnog broja glasova koje je neki odgovor na anketno
	 *            pitanje skupio
	 * @return <code>true</code> u slučaju uspješnog unosa u tablicu
	 *         'PollOptions', <code>false</code> inače
	 */
	private static boolean insertToPollOptions(Connection con, SeedEntry seed, Long currentKey, Random rand) {
		try (PreparedStatement pst = con.prepareStatement(
				"INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES (?, ?, ?, ?)")) {
			pst.setString(1, seed.firstParam);
			pst.setString(2, seed.secondParam);
			pst.setLong(3, currentKey);
			pst.setLong(4, rand.nextInt(RANDOM_BOUND));

			return pst.executeUpdate() != 0;
		} catch (SQLException e) {
		}

		return false;
	}

	/**
	 * Pomoćna metoda koja se koristi za parsiranje seed datoteke. Metoda prvo
	 * pokušava pročitati datoteku sa inicijalnim vrijednostima, potom svaki
	 * redak te datoteke parsira u primjerak razreda {@link SeedEntry} te
	 * {@link Set} svih parsiranih primjeraka tog razreda vraća kao povratnu
	 * vrijednost.
	 *
	 * @param seedFileName
	 *            primjerak razreda {@link String} koji predstavlja putanju do
	 *            seed datoteke
	 * @param paramDelimiter
	 *            primjerak razreda {@link String} koji predstavlja niz znakova
	 *            kojim su parametri odvojeni unutar redka datoteke
	 * @return {@link Set} svih parsiranih primjeraka razreda {@link SeedEntry}
	 * @throws IOException
	 *             Ukoliko nije moguće seed datoteku otvoriti za čitanje
	 */
	private static Set<SeedEntry> parseSeedFiles(String seedFileName, String paramDelimiter) throws IOException {
		InputStream is = SeedUtil.class.getClassLoader().getResourceAsStream(seedFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		
		Set<SeedEntry> seeds = new LinkedHashSet<>();
		while(reader.ready()) {
			String[] splitted = reader.readLine().split(paramDelimiter);

			if (splitted.length != 2) {
				continue;
			}

			seeds.add(new SeedEntry(splitted[0].trim(), splitted[1].trim()));
		}

		return seeds;
	}

	/**
	 * Pomoćni statički razred koji modelira jedan unos u seed datoteci. Svaki
	 * primjerak ovog razreda sastoji se od točno dva parametra.
	 */
	private static class SeedEntry {

		/**
		 * Članska varijabla koja predstavlja prvi parametar unos u seed
		 * datoteci
		 */
		private String firstParam;

		/**
		 * Članska varijabla koja predstavlja drugi parametar unos u seed
		 * datoteci
		 */
		private String secondParam;

		/**
		 * Konstruktor koji incijalizira primjerak ovog razreda. Ovaj
		 * konstruktor sve predane parametre bez dodatnih provjera posprema u
		 * pripadne članske varijable.
		 *
		 * @param firstParam
		 *            prvi parametar unos u seed datoteci
		 * @param secondParam
		 *            drugi parametar unos u seed datoteci
		 */
		public SeedEntry(String firstParam, String secondParam) {
			this.firstParam = firstParam;
			this.secondParam = secondParam;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((firstParam == null) ? 0 : firstParam.hashCode());
			result = prime * result + ((secondParam == null) ? 0 : secondParam.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SeedEntry other = (SeedEntry) obj;
			if (firstParam == null) {
				if (other.firstParam != null)
					return false;
			} else if (!firstParam.equals(other.firstParam))
				return false;
			if (secondParam == null) {
				if (other.secondParam != null)
					return false;
			} else if (!secondParam.equals(other.secondParam))
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "SeedEntry [firstParam=" + firstParam + ", secondParam=" + secondParam + "]";
		}
	}
}
