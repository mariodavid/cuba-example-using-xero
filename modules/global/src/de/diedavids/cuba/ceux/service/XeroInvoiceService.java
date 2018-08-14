package de.diedavids.cuba.ceux.service;


public interface XeroInvoiceService {
    String NAME = "ceux_XeroInvoiceService";

    boolean importInvoices();
}