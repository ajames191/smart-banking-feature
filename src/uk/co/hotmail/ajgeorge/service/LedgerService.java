package uk.co.hotmail.ajgeorge.service;

import uk.co.hotmail.ajgeorge.model.*;

import java.util.ArrayList;

import static uk.co.hotmail.ajgeorge.utilities.DateUtils.generateTimestamp;

public class LedgerService {

    private ArrayList<Customer> tempCustomers = new ArrayList<>();
    private ArrayList<LedgerEntry> LedgerEntriestoAdd = new ArrayList<>();

    public ArrayList<Customer> processCustomerLedgers(ArrayList<Customer> customers) {
        for (Customer customer : customers) {
            tempCustomers.add(new Customer(customer));
            for (LedgerEntry entry : customer.getLedger().getLedgerEntries()) {
                updateAccountBalances(entry, customer);

                addLedgerEntriesToTemp(entry);
            }
        }
        return tempCustomers;
    }

    private void updateAccountBalances(LedgerEntry entry, Customer customer) {
        float savingsAccBal = customer.getSavingAccount().getBalance();
        float transactionValue = entry.getTransactionValue();

        switch (entry.getAccountType()) {
            case CURRENT:
                customer.getCurrentAccount().updateBalance(transactionValue);
                if (customer.getCurrentAccount().getBalance() < 0 && savingsAccBal > 0) {
                    transferFundsFromSavings(customer);
                }
                break;
            case SAVINGS:
                customer.getSavingAccount().updateBalance(transactionValue);
                if (customer.getSavingAccount().getBalance() < 0) {
                    transferFundsFromCurrentAccount(customer);
                }
                break;
        }
    }

    private void addLedgerEntriesToTemp(LedgerEntry entry) {
        Customer tempCustomer = tempCustomers.get(tempCustomers.size()-1);
        tempCustomer.getLedger().getLedgerEntries().add(new LedgerEntry(entry));
        tempCustomer.getLedger().getLedgerEntries().addAll(LedgerEntriestoAdd);
        LedgerEntriestoAdd = new ArrayList<>();
    }

    protected void transferFundsFromCurrentAccount(Customer customer) {
        float savingBalance = customer.getSavingAccount().getBalance();
        customer.getCurrentAccount().updateBalance(savingBalance);
        customer.getSavingAccount().updateBalance(-savingBalance);

        addSystemLedgers(savingBalance,
                customer.getSavingAccount(),
                customer.getCurrentAccount());
    }

    protected void transferFundsFromSavings(Customer customer) {
        CurrentAccount currentAccount = customer.getCurrentAccount();
        SavingAccount savingAccount = customer.getSavingAccount();

        float transferAmount = Math.abs(currentAccount.getBalance());

        if (savingAccount.getBalance() >= transferAmount) {

            savingAccount.updateBalance(-transferAmount);
            currentAccount.updateBalance(transferAmount);

            addSystemLedgers(transferAmount,
                    savingAccount,
                    currentAccount);
        } else {

            transferAmount = savingAccount.getBalance();
            savingAccount.updateBalance(-transferAmount);
            currentAccount.updateBalance(transferAmount);

            addSystemLedgers(transferAmount,
                    currentAccount,
                    savingAccount);
        }
    }

    protected void addSystemLedgers(float transferAmount, Account decrementAccount,
                                    Account incrementAccount) {

        String timeStamp = generateTimestamp();
        final String INITIATOR_SYSTEM = "SYSTEM";

        LedgerEntriestoAdd.add(new LedgerEntry(decrementAccount.getId(), decrementAccount.getAccountType(), INITIATOR_SYSTEM,
                timeStamp, -transferAmount));
        LedgerEntriestoAdd.add(new LedgerEntry(incrementAccount.getId(), incrementAccount.getAccountType(), INITIATOR_SYSTEM,
                timeStamp, transferAmount));
    }

}
