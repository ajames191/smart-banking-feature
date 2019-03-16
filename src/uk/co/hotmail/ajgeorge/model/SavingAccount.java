package uk.co.hotmail.ajgeorge.model;

public class SavingAccount implements Account {
    private int accountId;
    private AccountTypes accountType;
    private float balance;

    public SavingAccount(int accountId) {
        this.accountId = accountId;
        this.accountType = AccountTypes.SAVINGS;
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
