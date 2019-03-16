package uk.co.hotmail.ajgeorge.model;

public class LedgerEntry {
    private int accountId;
    private AccountTypes accountType;
    private String initiatorType;
    private String dateTime;
    private float transactionValue;

    public LedgerEntry(int accountId, AccountTypes accountType, String initiatorType, String dateTime, float transactionValue) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.initiatorType = initiatorType;
        this.dateTime = dateTime;
        this.transactionValue = transactionValue;
    }

    public LedgerEntry(LedgerEntry entry) {
        this.accountId = entry.getAccountId();
        this.accountType = entry.getAccountType();
        this.initiatorType = entry.getInitiatorType();
        this.dateTime = entry.getDateTime();
        this.transactionValue = entry.getTransactionValue();
    }

    public int getAccountId() {
        return accountId;
    }

    public AccountTypes getAccountType() {
        return accountType;
    }

    public String getInitiatorType() {
        return initiatorType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public float getTransactionValue() {
        return transactionValue;
    }

    public String toCsvString() {
        return String.valueOf(accountId) + "," +
                accountType + "," +
                initiatorType + "," +
                dateTime + "," +
                transactionValue + "\n";
    }
}
