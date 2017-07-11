package hr.fer.zemris.java.hw14.dao;

import hr.fer.zemris.java.hw14.dao.sql.SQLDAO;

/**
 * Razred koji se koristi za dohvat Data Source Objecta za ovu aplikaciju, a
 * koji je modeliran sučeljem {@link DAO}. Ovaj razred napisan je u duhu
 * oblikovnog obrasca
 * <a href = "https://en.wikipedia.org/wiki/Singleton_pattern">jedinstveni
 * objekt</a> (engl. Singleton). Razred prilikom učitavanja u memoriju postavlja
 * primjerak razreda koji implementira sučelje {@link DAO} unutar svoje privatne
 * statičke varijable.
 * 
 * @see DAO
 * 
 * @author Davor Češljaš
 */
public class DAOProvider {

	/**
	 * Konstanta koja predstavlja jedini primjerak razreda koji implementira
	 * sučelje {@link DAO} unutar ove aplikacije, a u duhu oblikovnog obrasca
	 * <a href = "https://en.wikipedia.org/wiki/Singleton_pattern">jedinstveni
	 * objekt</a>
	 */
	private static DAO dao = new SQLDAO();

	/**
	 * Metoda koja dohvaća jedini, statički primjerak razreda koji implementira
	 * sučelje {@link DAO} unutar ove aplikacije
	 *
	 * @return jedini, statički primjerak razreda koji implementira sučelje
	 *         {@link DAO} unutar ove aplikacije
	 */
	public static DAO getDao() {
		return dao;
	}

}