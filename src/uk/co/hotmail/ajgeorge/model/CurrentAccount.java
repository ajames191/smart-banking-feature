package uk.co.hotmail.ajgeorge.model;

public class CurrentAccount implements Account {
    private int accountId;
    private AccountTypes accountType;
    private float balance;

    public CurrentAccount(int accountId) {
        this.accountId = accountId;
        this.accountType = AccountTypes.CURRENT;
        this.balance = 0.00f;
    }

    @Override
    public int getId() {
        return accountId;
    }

    @Override
    public AccountTypes getAccountType() {
        return accountType;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    @Override
    public void updateBalance(float modifyAmount) {
        this.balance += modifyAmount;
    }
}
