package hr.fer.zemris.java.hw14.app.models;

/**
 * Razred koji služi kao model podataka. Primjerci ovog razreda modeliraju
 * jedanu anketu koja se nalazi unutar baze podataka. Primjerci ovog razreda
 * sadrže sve atribute koje u bazi podataka postoje za ovu anketu. Primjerci
 * ovog razreda su nepromijenjiv.
 * 
 * @author Davor Češljaš
 */
public class Poll {

	/** Članska varijabla koja predstavlja identifikator ankete */
	private long id;

	/** Članska varijabla koja predstavlja naslov ankete */
	private String title;

	/**
	 * Članska varijabla koja predstavlja poruku korisniku, koji sudjeluje
	 * unutar ove ankete
	 */
	private String message;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Ovaj konstruktor
	 * predane parametre interno posprema unutar za to predviđenih člasnkih
	 * varijabli
	 *
	 * @param id
	 *            identifikator ankete
	 * @param title
	 *            naslov ankete
	 * @param message
	 *            poruka koja se ispisuje korisniku koji sudjeluje u anketi
	 */
	public Poll(long id, String title, String message) {
		this.id = id;
		this.title = title;
		this.message = message;
	}

	/**
	 * Metoda koja dohvaća identifikator ankete
	 *
	 * @return identifikator ankete
	 */
	public long getId() {
		return id;
	}

	/**
	 * Metoda koja dohvaća naslov ankete
	 *
	 * @return naslov ankete
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Metoda koja dohvaća poruku koja se ispisuje korisniku koji sudjeluje u
	 * anketi
	 *
	 * @return poruku koja se ispisuje korisniku koji sudjeluje u anketi
	 */
	public String getMessage() {
		return message;
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
		Poll other = (Poll) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Poll: id=%d, title='%s', message=%s", id, title, message);
	}

}
