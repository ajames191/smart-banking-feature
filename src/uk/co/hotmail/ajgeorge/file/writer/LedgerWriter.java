package uk.co.hotmail.ajgeorge.file.writer;

import uk.co.hotmail.ajgeorge.model.Customer;
import uk.co.hotmail.ajgeorge.model.LedgerEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LedgerWriter {

    public void outputToCsv(ArrayList<Customer> customers) {
        for (Customer customer : customers) {
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter("./customer-" + customer.getId() + "-ledger_updated.csv"));
                writer.write("AccountID,AccountType,InitiatorType,DateTime,TransactionValue\n");
                for (LedgerEntry entry : customer.getLedger().getLedgerEntries()) {
                    writer.write(entry.toCsvString());
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
