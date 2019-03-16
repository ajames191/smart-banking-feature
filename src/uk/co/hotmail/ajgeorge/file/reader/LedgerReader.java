package uk.co.hotmail.ajgeorge.file.reader;

import uk.co.hotmail.ajgeorge.model.AccountTypes;
import uk.co.hotmail.ajgeorge.model.Customer;
import uk.co.hotmail.ajgeorge.model.Ledger;
import uk.co.hotmail.ajgeorge.model.LedgerEntry;

import java.io.*;
import java.util.ArrayList;

public class LedgerReader {
    private int currentId = 0;
    private int savingId = 0;

    public ArrayList<Customer> getCustomersWithLedgers() {
        ArrayList<Customer> customers = new ArrayList<>();

        File currentDirectory = new File(".");
        String[] filesInCurrentDir = currentDirectory.list();

        if (filesInCurrentDir != null) {
            for (String file : filesInCurrentDir) {
                if (file.contains("-ledger.csv")) {

                    int id = 0;
                    ArrayList<LedgerEntry> ledgerEntries = new ArrayList<>();
                    try {
                        id = extractCustomerIdFromFile(file);
                        ledgerEntries = extractLedgerEntryFromCsv(new File(file));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    customers.add(new Customer(id, new Ledger(ledgerEntries), currentId, savingId));
                    currentId = 0;
                    savingId = 0;
                }
            }
        }

        return customers;
    }

    protected int extractCustomerIdFromFile(String file) throws Exception {
        String id = file.split("-")[1];
        if (isValidNumber(id)) {
            return Integer.parseInt(id);
        }

        throw new Exception("Incorrect Account ID, must be all numbers: " + id);
    }

    protected boolean isValidNumber(String number) {
        return number.matches("^[0-9]+$");
    }

    private ArrayList<LedgerEntry> extractLedgerEntryFromCsv(File file) throws Exception {
        ArrayList<LedgerEntry> ledgerEntries = new ArrayList<>();
        BufferedReader br = null;

        try {
            String line;
            br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {
                String[] entry = line.split(",");
                if (isValidNumber(entry[0])) { // this checks the first column is a number, so we can skip the title row
                    LedgerEntry ledgerEntry = createNewLedgerEntryFromData(entry);
                    ledgerEntries.add(ledgerEntry);

                    if (currentId == 0 && ledgerEntry.getAccountType() == AccountTypes.CURRENT) {
                        currentId = ledgerEntry.getAccountId();
                    } else if (savingId == 0 && ledgerEntry.getAccountType() == AccountTypes.SAVINGS) {
                        savingId = ledgerEntry.getAccountId();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ledgerEntries;
    }

    protected LedgerEntry createNewLedgerEntryFromData(String[] entry) throws Exception {
        if (dateIsIsoFormat(entry[3])) {
            return new LedgerEntry(
                    Integer.parseInt(entry[0]),
                    AccountTypes.valueOf(entry[1]),
                    entry[2],
                    entry[3],
                    Float.valueOf(entry[4])
            );
        }
        return null;
    }

    protected boolean dateIsIsoFormat(String s) throws Exception {
        if (s.matches("\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d[Z]")) {
            return true;
        }

        throw new Exception("Date not ISO-8601 compliant, must match format: 'yyyy-mm-ddThh:mm:ssZ");
    }
}
