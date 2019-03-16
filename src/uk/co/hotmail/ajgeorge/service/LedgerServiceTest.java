package uk.co.hotmail.ajgeorge.service;

import org.junit.Assert;
import org.junit.Test;
import uk.co.hotmail.ajgeorge.model.*;
import uk.co.hotmail.ajgeorge.utilities.DateUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class LedgerServiceTest {

    private LedgerService underTest = new LedgerService();

    @Test
    public void transferFundsFromSavings_withExtraSavings() {

        // Given
        Customer customer = new Customer(123, new Ledger(new ArrayList<>()), 132, 321);

        CurrentAccount currentAccount = customer.getCurrentAccount();
        currentAccount.updateBalance(-50);
        SavingAccount savingAccount = customer.getSavingAccount();
        savingAccount.updateBalance(60);

        // When
        underTest.transferFundsFromSavings(customer);

        // Then
        Assert.assertEquals(10, savingAccount.getBalance(), 0.01);
        Assert.assertEquals(0, currentAccount.getBalance(), 0.01);
    }

    @Test
    public void transferFundsFromSavings_withLessSavings() {

        // Given
        Customer customer = new Customer(123, new Ledger(new ArrayList<>()), 132, 321);

        CurrentAccount currentAccount = customer.getCurrentAccount();
        currentAccount.updateBalance(-50);
        SavingAccount savingAccount = customer.getSavingAccount();
        savingAccount.updateBalance(40);

        // When
        underTest.transferFundsFromSavings(customer);

        // Then
        assertEquals(0, savingAccount.getBalance(), 0.01);
        assertEquals(-10, currentAccount.getBalance(), 0.01);
    }

    @Test
    public void transferFundsFromCurrentAccount_() {
        // Given
        Customer customer = new Customer(123, new Ledger(new ArrayList<>()), 132, 321);

        CurrentAccount currentAccount = customer.getCurrentAccount();
        currentAccount.updateBalance(100);
        SavingAccount savingAccount = customer.getSavingAccount();
        savingAccount.updateBalance(-20);

        // When
        underTest.transferFundsFromCurrentAccount(customer);

        // Then
        assertEquals(80, currentAccount.getBalance(), 0.01);
        assertEquals(0, savingAccount.getBalance(), 0.01);
    }

    @Test
    public void transferFundsFromCurrentAccount() {
        // Given
        Customer customer = new Customer(123, new Ledger(new ArrayList<>()), 132, 321);

        CurrentAccount currentAccount = customer.getCurrentAccount();
        currentAccount.updateBalance(-10);
        SavingAccount savingAccount = customer.getSavingAccount();
        savingAccount.updateBalance(-50);

        // When
        underTest.transferFundsFromCurrentAccount(customer);

        // Then
        assertEquals(-60, currentAccount.getBalance(), 0.01);
        assertEquals(0, savingAccount.getBalance(), 0.01);
    }

    @Test
    public void addSystemLedgers() {
        // given
        Customer customer = new Customer(123, new Ledger(new ArrayList<>()), 123, 321);

        //when
        int transferAmount = 100;
        underTest.addSystemLedgers(transferAmount, customer.getCurrentAccount(), customer.getSavingAccount());

        //then
        ArrayList<LedgerEntry> ledgerEntriestoAdd = underTest.getLedgerEntriestoAdd();
        assertEquals(2, ledgerEntriestoAdd.size());

        assertEquals(AccountTypes.CURRENT, ledgerEntriestoAdd.get(0).getAccountType());
        assertEquals("SYSTEM", ledgerEntriestoAdd.get(0).getInitiatorType());
        assertEquals(-transferAmount, ledgerEntriestoAdd.get(0).getTransactionValue(), 0.01);

        assertEquals(AccountTypes.SAVINGS, ledgerEntriestoAdd.get(1).getAccountType());
        assertEquals("SYSTEM", ledgerEntriestoAdd.get(1).getInitiatorType());
        assertEquals(transferAmount, ledgerEntriestoAdd.get(1).getTransactionValue(), 0.01);
    }

    @Test
    public void processCustomerLedgers() {
        String dateTime = DateUtils.generateTimestamp();
        // Given
        LedgerEntry savingEntry = new LedgerEntry(321, AccountTypes.SAVINGS, "SYSTEM", dateTime, 20.00f);
        LedgerEntry currentEntry = new LedgerEntry(123, AccountTypes.CURRENT, "SYSTEM", dateTime, -20.00f);

        Customer customer1 = new Customer(123, new Ledger(new ArrayList<>()), 123, 321);
        customer1.getLedger().getLedgerEntries().add(savingEntry);
        customer1.getLedger().getLedgerEntries().add(currentEntry);

        Customer customer2 = new Customer(124, new Ledger(new ArrayList<>()), 124, 322);
        customer2.getLedger().getLedgerEntries().add(savingEntry);
        customer2.getLedger().getLedgerEntries().add(currentEntry);


        ArrayList<Customer> customerArrayList = new ArrayList<>();
        customerArrayList.add(customer1);
        customerArrayList.add(customer2);
        // When
       ArrayList<Customer> actual = underTest.processCustomerLedgers(customerArrayList);

        // Then
        assertEquals(4, actual.get(0).getLedger().getLedgerEntries().size());
        assertEquals(4, actual.get(1).getLedger().getLedgerEntries().size());

    }

}