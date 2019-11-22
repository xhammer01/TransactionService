package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@DatabaseTable(tableName = "accounts")
public class Account {

    public Account() {}

    @DatabaseField(generatedId = true)
    private Long Id;

    @DatabaseField(columnName = "balance", canBeNull = false)
    private BigDecimal balance;

    @ForeignCollectionField(eager = false)
    private Collection<Transaction> transactions;

    public BigDecimal debit(BigDecimal amount) {
        return balance = balance.subtract(amount);
    }

    public BigDecimal credit(BigDecimal amount) {
        return balance = balance.add(amount);
    }
}
