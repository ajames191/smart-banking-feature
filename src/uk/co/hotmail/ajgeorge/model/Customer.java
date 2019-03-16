package uk.co.hotmail.ajgeorge.model;

import java.util.ArrayList;

public class Customer {
    private int id;
    private Ledger ledger;
    private CurrentAccount currentAccount;
    private SavingAccount savingAccount;

    public Customer(int id, Ledger ledger, int currentId, int savingsId) {
        this.id = id;
        this.ledger = ledger;
        this.currentAccount = new CurrentAccount(currentId);
        this.savingAccount = new SavingAccount(savingsId);
    }

    public Customer(Customer customer) {
        this.id = customer.getId();
        this.ledger = new Ledger(new ArrayList<>());
        this.currentAccount = customer.getCurrentAccount();
        this.savingAccount = customer.getSavingAccount();
    }

    public int getId() {
        return id;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public CurrentAccount getCurrentAccount() {
        return this.currentAccount;
    }

    public SavingAccount getSavingAccount() {
        return this.savingAccount;
    }

}
