package uk.co.hotmail.ajgeorge.model;

public interface Account {
    int getId();
    float getBalance();
    void updateBalance(float modifyAmount);
    AccountTypes getAccountType();
}
