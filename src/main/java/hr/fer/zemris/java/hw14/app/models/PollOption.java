package hr.fer.zemris.java.hw14.app.models;

/**
 * Razred koji služi kao model podataka. Primjerci ovog razreda modeliraju jedan
 * odgovor unutar ankete modelirane razredom {@link Poll}, a koji se nalazi
 * unutar baze podataka. Primjerci ovog razreda sadrže sve atribute koje u bazi
 * podataka postoje za ovaj odgovor na anketu. Primjerci ovog razreda su
 * nepromijenjiv.
 * 
 * @see Poll
 * 
 * @author Davor Češljaš
 */
public class PollOption {

	/**
	 * Članska varijabla koja predstavlja identifikator odgovora na anketno
	 * pitanje
	 */
	private long id;

	/** Članska varijabla koja predstavlja naslov odgovora na anketno pitanje */
	private String optionTitle;

	/**
	 * Članska varijabla koja predstavlja poveznicu na internetu, a koja
	 * predstavlja ovaj odgovor na anketno pitanje. Ova poveznica nema
	 * definiranih ograničenja, te ona može biti bilo koji resurs na internetu
	 */
	private String optionLink;

	/**
	 * Članska varijabla koja predstavlja identifikator ankete unutar koje se
	 * ovaj odgovor nalazi
	 */
	private long pollID;

	/**
	 * Članska varijabla koja predstavlja broj glasova koje je ovaj odgovor na
	 * anketno pitanje skupio
	 */
	private long votesCount;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Ovaj konstruktor
	 * predane parametre interno posprema unutar za to predviđenih člasnkih
	 * varijabli
	 *
	 * @param id
	 *            identifikator odgovora na anketno pitanje
	 * @param optionTitle
	 *            naslov odgovora na anketno pitanje
	 * @param optionLink
	 *            poveznica na internetu, a koja predstavlja ovaj odgovor na
	 *            anketno pitanje. Ova poveznica nema definiranih ograničenja,
	 *            te ona može biti bilo koji resurs na internetu
	 * @param pollID
	 *            identifikator ankete unutar koje se ovaj odgovor nalazi
	 * @param votesCount
	 *            broj glasova koje je ovaj odgovor na anketno pitanje skupio
	 */
	public PollOption(long id, String optionTitle, String optionLink, long pollID, long votesCount) {
		this.id = id;
		this.optionTitle = optionTitle;
		this.optionLink = optionLink;
		this.pollID = pollID;
		this.votesCount = votesCount;
	}

	/**
	 * Metoda koja dohvaća identifikator odgovora na anketno pitanje
	 *
	 * @return identifikator odgovora na anketno pitanje
	 */
	public long getId() {
		return id;
	}

	/**
	 * Metoda koja dohvaća naslov odgovora na anketno pitanje
	 *
	 * @return naslov odgovora na anketno pitanje
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * Metoda koja dohvaća poveznicu na internetu, a koja predstavlja ovaj
	 * odgovor na anketno pitanje. Ova poveznica nema definiranih ograničenja,
	 * te ona može biti bilo koji resurs na internetu
	 *
	 * @return poveznicu na internetu, a koja predstavlja ovaj odgovor na
	 *         anketno pitanje
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * Metoda koja dohvaća identifikator ankete unutar koje se ovaj odgovor
	 * nalazi
	 *
	 * @return identifikator ankete unutar koje se ovaj odgovor nalazi
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * Metoda koja dohvaća broj glasova koje je ovaj odgovor na anketno pitanje
	 * skupio
	 *
	 * @return broj glasova koje je ovaj odgovor na anketno pitanje skupio
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * Metoda koja broj glasova koje je ovaj odgovor na anketno pitanje skupio
	 * za jedan glas
	 */
	public void incrementVotesCount() {
		votesCount++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		PollOption other = (PollOption) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("PollOption: id=%d, optionTitle='%s', optionLink='%s', pollID=%d, votes=%d", id,
				optionTitle, optionLink, pollID, votesCount);
	}

}
