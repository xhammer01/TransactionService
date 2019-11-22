package repository;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import model.Account;
import utils.DbConnector;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

public class AccountRepository {

    private Dao<Account, Long> accountDao;

    public AccountRepository(DbConnector connector) {
        try {
            accountDao = DaoManager.createDao(connector.connect(),Account.class);

            Account account1 = new Account();
            account1.setBalance(new BigDecimal(100));
            account1.setTransactions(new ArrayList<>());

            Account account2 = new Account();
            account2.setBalance(new BigDecimal(100));
            account2.setTransactions(new ArrayList<>());

            TableUtils.createTableIfNotExists(connector.connect(), Account.class);
            accountDao.create(account1);
            accountDao.create(account2);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Account> findById(Long id) {
        try {
            return Optional.ofNullable(accountDao.queryForId(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void save(Account account) {
        try {
            accountDao.createOrUpdate(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
