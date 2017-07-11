package hr.fer.zemris.java.hw14.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.app.models.Poll;
import hr.fer.zemris.java.hw14.app.models.PollOption;

/**
 * Sučelje koje modelira Data Source Object za ovu aplikaciju. Ova strategija
 * sučelje prema bazi podataka ili bilo kojem drugom sustavu koji se nalazi na
 * sloju za perzistenciju podataka (npr. datotečni sustav). Ovaj DAO nudi
 * sljedeće metode:
 * <ul>
 * <li>{@link #getPollOptions(long)}</li>
 * <li>{@link #updateVotesCount(long, long)}</li>
 * <li>{@link #getPolls()}</li>
 * <li>{@link #getPoll(long)}</li>
 * </ul>
 * <p>
 * Za više informacija o ovom konceptu korisnika se navodi na sljedeću
 * <a href="https://en.wikipedia.org/wiki/Data_access_object">poveznicu</a>
 * </p>
 * 
 * @see Poll
 * @see PollOption
 * 
 * @author Davor Češljaš
 */
public interface DAO {

	/**
	 * Metoda koja dohvaća {@link List} svih mogućih odgovora na anketno pitanje
	 * sa identifikatorom <b>pollID</b> sa sloja za perzistenciju. Svaki od
	 * odgovora modeliran je primjerkom razreda {@link PollOption}
	 *
	 * @param pollID
	 *            identifikator anketnog pitanja za koje se dohvaćaju odgovori
	 * @return {@link List} svih mogućih odgovora na anketno pitanje sa
	 *         identifikatorom <b>pollID</b>
	 * @throws DAOException
	 *             prilikom greške unutar sustava na sloju perzistenciju
	 */
	public List<PollOption> getPollOptions(long pollID) throws DAOException;

	/**
	 * Metoda koja se koristi za podizanje broja glasova koje sadrži odgovor na
	 * anketno pitanje sa identifikatorom <b>id</b> za jedan. Uz predani
	 * parametar <b>id</b> predaje se još parametar <b>pollID</b> koji
	 * predstavlja identifikator samog anketnog pitanja,a koji se koristi radi
	 * provjere pripada li odgovor sa ovim <b>id</b>-jem pitanju sa predanim
	 * identifikatorom
	 *
	 * @param id
	 *            identifikator odgovora na anketno pitanje kojemu se povećava
	 *            broj glasova
	 * @param pollID
	 *            identifikator aknetnog pitanja koji služi za gore objašnjenu
	 *            provjeru
	 * @return <code>true</code> ukoliko se broj glasova uspješno podigao,
	 *         <code>false</code> inače
	 * @throws DAOException
	 *             prilikom greške unutar sustava na sloju perzistenciju
	 */
	public boolean updateVotesCount(long id, long pollID) throws DAOException;

	/**
	 * Metoda koja dohvaća {@link List} svih mogućih anketnih pitanja
	 * modeliranih razredom {@link Poll} , a koji postoje na sloju za
	 * perzistenciju podataka
	 *
	 * @return {@link List} svih mogućih anketnih pitanja sa sloja za
	 *         prezistenciju podataka
	 * @throws DAOException
	 *             prilikom greške unutar sustava na sloju perzistenciju
	 */
	public List<Poll> getPolls() throws DAOException;

	/**
	 * Metoda koja dohvaća anketno pitanje modelirano razredom {@link Poll} sa
	 * sustava za perzistenciju podataka. Dohvaćeni razred predstavlja anketno
	 * pitanje sa identifikatorom <b>pollID</b> ili <code>null</code> ukoliko
	 * takva anketa ne postoji
	 *
	 * @param pollID
	 *            identifikator traženog primjerka razreda {@link Poll}
	 * @return primjerak razreda {@link Poll} koji modelira anketno pitanje sa
	 *         identifikatorom <b>pollID</b>
	 * @throws DAOException
	 *             prilikom greške unutar sustava na sloju perzistenciju
	 */
	public Poll getPoll(long pollID) throws DAOException;
}