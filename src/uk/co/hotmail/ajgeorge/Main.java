package uk.co.hotmail.ajgeorge;

import uk.co.hotmail.ajgeorge.file.reader.LedgerReader;
import uk.co.hotmail.ajgeorge.file.writer.LedgerWriter;
import uk.co.hotmail.ajgeorge.model.*;
import uk.co.hotmail.ajgeorge.service.LedgerService;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Customer> customers;

        customers = new LedgerReader().getCustomersWithLedgers();
        customers = new LedgerService().processCustomerLedgers(customers);
        new LedgerWriter().outputToCsv(customers);
    }
}
