package uk.co.hotmail.ajgeorge.file.reader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.co.hotmail.ajgeorge.model.AccountTypes;
import uk.co.hotmail.ajgeorge.model.Customer;
import uk.co.hotmail.ajgeorge.model.LedgerEntry;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LedgerReaderTest {

    private LedgerReader underTest = new LedgerReader();

    @Test
    public void getCustomersWithLedgers() {
        //given
        //two test files
        //customer-1234567-ledger.csv && customer-1234568-ledger.csv

        //when
        ArrayList<Customer> actual = underTest.getCustomersWithLedgers();

        //then
        assertEquals(2, actual.size());
        assertEquals(1234567, actual.get(0).getId());
        assertEquals(8, actual.get(0).getLedger().getLedgerEntries().size());
        assertEquals(1234568, actual.get(1).getId());
        assertEquals(8, actual.get(1).getLedger().getLedgerEntries().size());

    }

    @Test
    public void isValidNumber_withIncorrectData() {
        //given
        String data = "23243af33"; // Data we are testing
        final boolean expectedOutcome = false; // What we want to happen

        //when
        boolean actual = underTest.isValidNumber(data); // What actually happens when running method on tested data

        //then
        assertEquals(expectedOutcome, actual);
    }

    @Test
    public void isValidNumber_withCorrectData() {
        //given
        String data = "23243";
        final boolean expectedOutcome = true;

        //when
        boolean actual = underTest.isValidNumber(data);

        //then
        assertEquals(expectedOutcome, actual);
    }

    @Test
    public void extractCustomerIdFromFile_withCorrectData() throws Exception {

        //given
        String data = "customer-1234567-ledger.csv";
        int expected = 1234567;

        //when
        int actual = underTest.extractCustomerIdFromFile(data);

        //then
        assertEquals(expected, actual);
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void extractCustomerIdFromFile_withIncorrectData() throws Exception {

        //given
        String data = "customer-123bad4Data5Here67-ledger.csv";

        thrown.expect( Exception.class );
        thrown.expectMessage("Incorrect Account ID, must be all numbers: 123bad4Data5Here67");

        //when
        underTest.extractCustomerIdFromFile(data);
    }

    @Test
    public void createNewLedgerFromData() throws Exception {

        // given
        String[] entry = {"123", "SAVINGS", "ACCOUNT-HOLDER", "2018-12-30T22:10:00Z", "50.25"};
        LedgerEntry expected = new LedgerEntry(
                123,
                AccountTypes.SAVINGS,
                "ACCOUNT-HOLDER",
                "2018-12-30T22:10:00Z",
                50.25f);

        // when
        LedgerEntry actual = underTest.createNewLedgerEntryFromData(entry);

        // then
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getAccountType(), actual.getAccountType());
        assertEquals(expected.getInitiatorType(), actual.getInitiatorType());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getTransactionValue(), actual.getTransactionValue(), 0.01);

    }

    @Test
    public void dateIsIsoFormat_withCorrectDate() throws Exception {

        // given
        String data = "2018-12-30T22:10:00Z";
        //when
        boolean actual = underTest.dateIsIsoFormat(data);
        //then
        assertTrue(actual);
    }

    @Test
    public void dateIsIsoFormat_withIncorrectDate() throws Exception {

        // given
        String data = "2018-12-30T22:10:00";

        thrown.expect( Exception.class );
        thrown.expectMessage("Date not ISO-8601 compliant, must match format: 'yyyy-mm-ddThh:mm:ssZ");

        //when
        underTest.dateIsIsoFormat(data);
    }
}