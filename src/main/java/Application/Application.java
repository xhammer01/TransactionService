package Application;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import error.AccountNotFoundException;
import error.InsufficientBalanceException;
import error.TransactionFailedRollbackException;
import model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.TransactionService;

import javax.servlet.http.HttpServletResponse;

import static java.net.HttpURLConnection.*;
import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Application.class);

        TransactionService transactionService = new TransactionService();

        post("/transactions", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            Transaction transaction;
            try {
                transaction = mapper.readValue(request.body(), Transaction.class);
            } catch (JsonParseException | JsonMappingException ex) {
                ex.printStackTrace();
                logger.error("failed to parse Transaction JSON");
                response.status(HTTP_BAD_REQUEST);
                return "Error, Malformed request";
            }
            transaction = transactionService.commitTransaction(transaction);
            response.status(HTTP_CREATED);
            response.type("application/json");

            return transaction.getTransactionId();
        });

        exception(InsufficientBalanceException.class, (exception, request, response) -> {
            response.status(HttpServletResponse.SC_BAD_REQUEST);
            response.body("Insufficient Balance");
        });

        exception(TransactionFailedRollbackException.class, (exception, request, response) -> {
            response.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.body("Transaction failed");
        });

        exception(AccountNotFoundException.class, (exception, request, response) -> {
            response.status(HttpServletResponse.SC_NOT_FOUND);
            response.body("Account not found");
        });
    }
}
