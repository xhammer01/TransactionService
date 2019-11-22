package Application;

import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;

public class ApplicationTest {

    public static class ApplicationTestSparkApplication implements SparkApplication {

        @Override
        public void init() {
            new Application();
            Application.main(null);
        }
    }

    @ClassRule
    public static SparkServer<ApplicationTestSparkApplication> testServer = new SparkServer<>(ApplicationTest.ApplicationTestSparkApplication.class, 4567);

    @Test
    public void getStatementForAccount() throws HttpClientException {

        String payload = "{\n" +
                "  \"accountId\": \"2\",\n" +
                "  \"amount\": \"-13.00\",\n" +
                "  \"targetAccountId\": \"1\",\n" +
                "  \"comment\": \"foo\"\n" +
                "}";

        PostMethod postMethod = testServer.post("/transactions", payload, false);
        postMethod.addHeader("Content-Type", "application/json");
        HttpResponse httpResponse = testServer.execute(postMethod);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, httpResponse.code());
    }

    @Test
    public void makeTransactionForAccount() throws Exception {
        String payload = "{\n" +
                "  \"sourceId\": \"2\",\n" +
                "  \"amount\": \"13.00\",\n" +
                "  \"targetAccountId\": \"1\"\n" +
                "}";

        PostMethod postMethod = testServer.post("/transactions", payload, false);
        postMethod.addHeader("Content-Type","application/json");
        HttpResponse httpResponse = testServer.execute(postMethod);

        assertEquals(HttpServletResponse.SC_CREATED, httpResponse.code());
    }

    @Test
    public void makeTransactionForAccountThatDoesNotExist() throws HttpClientException {
        String payload = "{\n" +
                "  \"sourceId\": \"99\",\n" +
                "  \"amount\": \"13.00\",\n" +
                "  \"targetAccountId\": \"1\"\n" +
                "}";

        PostMethod postMethod = testServer.post("/transactions", payload, false);
        postMethod.addHeader("Content-Type", "application/json");
        HttpResponse httpResponse = testServer.execute(postMethod);

        assertEquals(HttpServletResponse.SC_NOT_FOUND, httpResponse.code());
    }

    @Test
    public void makeInvalidTransactionForAccount() throws HttpClientException {
        String payload = "{\n" +
                "  \"sourceI\": \"2\",\n" +
                "  \"amount\": \"13.00\",\n" +
                "  \"targetAccountId\": \"1\"\n" +
                "}";

        PostMethod postMethod = testServer.post("/transactions", payload, false);
        postMethod.addHeader("Content-Type", "application/json");
        HttpResponse httpResponse = testServer.execute(postMethod);

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, httpResponse.code());
    }
}
