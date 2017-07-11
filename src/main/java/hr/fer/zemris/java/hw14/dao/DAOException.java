package hr.fer.zemris.java.hw14.dao;

/**
 * Razred koji nasljeđuje {@link RuntimeException}. Razred se koristi kao
 * iznimka koju bacaju metode sučelja {@link DAO} prilikom greške unutar sustava
 * na sloju perzistenciju. Ovaj razred je neprovjeravana iznimka.
 * 
 * @see RuntimeException
 * @see DAO
 * 
 * @author Davor Češljaš
 */
public class DAOException extends RuntimeException {

	/** Konstanta koja se koristi za serijalizaciju primjeraka ovog razreda. */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Korištenjem ovog
	 * konstruktora korisniku će prilikom pojave iznimke biti ispisan trag stoga
	 * bez ikakve dodatne poruke
	 */
	public DAOException() {
		super();
	}

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Korištenjem ovog
	 * konstruktora korisniku će prilikom pojave iznimke biti ispisan trag stoga
	 * uz dodatnu poruku
	 *
	 * @param message
	 *            poruka koju treba ispisati korisniku prilikom bacanja iznimke
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Korištenjem ovog
	 * konstruktora korisniku će prilikom pojave iznimke biti ispisan trag stoga
	 * uz dodatan uzrok
	 *
	 * @param cause
	 *            Uzrok bacanja iznimke
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}

	/**
	 * Konstruktor koji inicijalizira primjerak ovog razreda. Korištenjem ovog
	 * konstruktora korisniku će prilikom pojave iznimke biti ispisan trag stoga
	 * uz dodatan uzrok i poruku
	 *
	 * @param message
	 *            poruka koju treba ispisati korisniku prilikom bacanja iznimke
	 * @param cause
	 *            Uzrok bacanja iznimke
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new DAO exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 * @param enableSuppression
	 *            the enable suppression
	 * @param writableStackTrace
	 *            the writable stack trace
	 */
	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}