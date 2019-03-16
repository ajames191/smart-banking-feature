package uk.co.hotmail.ajgeorge.service;

import org.junit.Assert;
import org.junit.Test;
import uk.co.hotmail.ajgeorge.model.*;

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
//        assertEquals(2, customer.getLedger().getLedgerEntries().size());
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
//        assertEquals(2, customer.getLedger().getLedgerEntries().size());
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

//    @Test
//    public void addSystemLedgers() {
//        // given
//        Customer customer = new Customer(123, new Ledger(new ArrayList<>()), 123, 321);
//
//        //when
//        int transferAmount = 100;
//        underTest.addSystemLedgers(transferAmount, customer.getCurrentAccount(), customer.getSavingAccount());
//
//        //then
//        ArrayList<LedgerEntry> ledgerEntries = customer.getLedger().getLedgerEntries();
//
////        assertEquals(2, ledgerEntries.size());
//
//        assertEquals(AccountTypes.CURRENT, ledgerEntries.get(0).getAccountType());
//        assertEquals("SYSTEM", ledgerEntries.get(0).getInitiatorType());
//        assertEquals(-transferAmount, ledgerEntries.get(0).getTransactionValue(), 0.01);
//
//        assertEquals(AccountTypes.SAVINGS, ledgerEntries.get(1).getAccountType());
//        assertEquals("SYSTEM", ledgerEntries.get(1).getInitiatorType());
//        assertEquals(transferAmount, ledgerEntries.get(1).getTransactionValue(), 0.01);
//    }
}