package repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import model.Transaction;
import utils.DbConnector;

import java.sql.SQLException;

public class TransactionRepository {

    private Dao<Transaction, String> transactionDao;

    public TransactionRepository(DbConnector connector) {
        try {
            transactionDao = DaoManager.createDao(connector.connect(), Transaction.class);
            TableUtils.createTableIfNotExists(connector.connect(), Transaction.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Transaction transaction) {
        try {
            transactionDao.create(transaction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
