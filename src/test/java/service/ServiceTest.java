package service;

import error.AccountNotFoundException;
import error.TransactionFailedRollbackException;
import model.Transaction;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceTest {

    TransactionService transactionService;

    public ServiceTest() {
        this.transactionService = new TransactionService();
    }

    @Test
    public void commitTransactionThrowsAccountNotFoundException() {
        Transaction transaction = Transaction.builder()
                .targetAccountId(99L)
                .amount(new BigDecimal(10))
                .sourceId(1L)
                .build();

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.commitTransaction(transaction);
        });
    }

    @Test
    public void sourceAccountAndTargetAccountCannotBeTheSame() {
        Transaction transaction = Transaction.builder()
                .targetAccountId(1L)
                .amount(new BigDecimal(99))
                .sourceId(1L)
                .build();

        assertThrows(TransactionFailedRollbackException.class, () -> {
            transactionService.commitTransaction(transaction);
        });
    }
}
