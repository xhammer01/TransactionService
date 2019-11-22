package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@DatabaseTable(tableName = "transactions")
public class Transaction {

    public Transaction() {}

    @DatabaseField(generatedId = false)
    private UUID transactionId;

    @DatabaseField(columnName = "transactionType", canBeNull = false)
    private TransactionType transactionType;

    @DatabaseField(columnName = "targetAccountId", canBeNull = false)
    private Long targetAccountId;

    @DatabaseField(columnName = "amount", canBeNull = false)
    private BigDecimal amount;

    @DatabaseField(columnName = "sourceId", canBeNull = false)
    private Long sourceId;

    @DatabaseField(columnName = "resultingBalance")
    private BigDecimal resultingBalance;

    @DatabaseField(foreign = true)
    private Account account;

    public enum TransactionType {
        CREDIT,
        DEBIT
    }
}
