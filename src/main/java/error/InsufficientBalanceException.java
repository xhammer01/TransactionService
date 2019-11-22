package error;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(Long id) {super("Insufficient balance, Account: " + id);}
}
