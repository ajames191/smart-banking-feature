package uk.co.hotmail.ajgeorge.model;

import java.util.ArrayList;

public class Ledger {
    private ArrayList<LedgerEntry> ledgerEntries = new ArrayList<>();

    public Ledger(ArrayList<LedgerEntry> entries) {
        ledgerEntries.addAll(entries);
    }

    public ArrayList<LedgerEntry> getLedgerEntries() {
        return ledgerEntries;
    }
}
