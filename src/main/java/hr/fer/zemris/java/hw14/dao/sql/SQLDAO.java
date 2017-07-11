package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw14.app.models.Poll;
import hr.fer.zemris.java.hw14.app.models.PollOption;
import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;

/**
 * Razred koji implementira sučelje {@link DAO}. Ovaj razred koristi se kao Data
 * Source Object koji za izvor podataka ima bazu podataka. Drugim riječima svi
 * podaci koje metode ovog razreda (odnosno sučelja {@link DAO}) dolaze iz baze
 * podataka.
 * 
 * @see DAO
 * 
 * @author Davor Češljaš
 */
public class SQLDAO implements DAO {

	@Override
	public List<PollOption> getPollOptions(long pollID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		List<PollOption> pollOptions = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement("select * from PollOptions where pollID=?")) {
			pst.setLong(1, pollID);

			try (ResultSet rset = pst.executeQuery()) {
				while (rset != null && rset.next()) {
					pollOptions.add(new PollOption(rset.getLong(1), rset.getString(2), rset.getString(3),
							rset.getLong(4), rset.getLong(5)));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Pogreška prilikom dohvata svih opcija ankete za pollID=" + pollID, e);
		}

		return pollOptions;
	}

	@Override
	public boolean updateVotesCount(long id, long pollID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = con
				.prepareStatement("update PollOptions set votesCount=votesCount + 1 where id=? and pollID=?")) {
			pst.setLong(1, id);
			pst.setLong(2, pollID);

			return pst.executeUpdate() != 0;
		} catch (SQLException e) {
			throw new DAOException("Pogreška prilikom izmjene odgovora sa id: " + id);
		}
	}

	@Override
	public List<Poll> getPolls() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		List<Poll> polls = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement("select * from Polls")) {
			try (ResultSet rset = pst.executeQuery()) {
				while (rset != null && rset.next()) {
					polls.add(new Poll(rset.getLong(1), rset.getString(2), rset.getString(3)));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Greška prilikom čitanja iz tablice 'Polls'");
		}

		return polls;
	}

	@Override
	public Poll getPoll(long pollID) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement("select * from Polls where id = ?")) {
			pst.setLong(1, pollID);

			try (ResultSet rset = pst.executeQuery()) {
				if (rset != null && rset.next()) {
					return new Poll(rset.getLong(1), rset.getString(2), rset.getString(3));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Greška prilikom čitanja iz tablice 'Polls'");
		}

		return null;
	}
}