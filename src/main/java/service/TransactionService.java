package service;

import error.AccountNotFoundException;
import error.InsufficientBalanceException;
import error.TransactionFailedRollbackException;
import model.Account;
import model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AccountRepository;
import repository.TransactionRepository;
import utils.DbConnector;

import javax.transaction.Transactional;
import java.util.UUID;

public class TransactionService {

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionService() {
        DbConnector dbConnector = new DbConnector();
        this.transactionRepository = new TransactionRepository(dbConnector);
        this.accountRepository = new AccountRepository(dbConnector);
    }

    @Transactional
    public Transaction commitTransaction(Transaction transaction) {

        logger.info("Enter commit transaction");

        if (transaction.getTransactionId() == null) {
            transaction.setTransactionId(UUID.randomUUID());
        }

        if (transaction.getTargetAccountId().equals(transaction.getSourceId())) {
            throw new TransactionFailedRollbackException("Origin account and destination account cannot be the same");
        }

        Account sourceAccount = accountRepository.findById(transaction.getSourceId()).orElseThrow(() -> new AccountNotFoundException(transaction.getSourceId()));

        if (sourceAccount.getBalance().compareTo(transaction.getAmount()) <0) {
            throw new InsufficientBalanceException(sourceAccount.getId());

        } else {
            Account targetAccount = accountRepository.findById(transaction.getTargetAccountId()).orElseThrow(() ->  new AccountNotFoundException(transaction.getTargetAccountId()));

            transaction.setResultingBalance(sourceAccount.debit(transaction.getAmount()));
            transaction.setTransactionType(Transaction.TransactionType.DEBIT);
            sourceAccount.getTransactions().add(transaction);

            transaction.setTransactionType(Transaction.TransactionType.CREDIT);
            transaction.setResultingBalance(targetAccount.credit(transaction.getAmount()));
            sourceAccount.getTransactions().add(transaction);

            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);
        }
        logger.info("Exit commit transaction");
        return transaction;
    }
}
