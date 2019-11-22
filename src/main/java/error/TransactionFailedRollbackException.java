package error;

public class TransactionFailedRollbackException extends RuntimeException {

    public TransactionFailedRollbackException(String reason) {super("Transaction failed, reason: " + reason);}
}
