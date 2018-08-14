package de.diedavids.cuba.ceux.web.invoice;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import de.diedavids.cuba.ceux.entity.Invoice;
import de.diedavids.cuba.ceux.service.XeroInvoiceService;

import javax.inject.Inject;
import java.util.UUID;

public class InvoiceBrowse extends AbstractLookup {

    @Inject
    XeroInvoiceService xeroInvoiceService;

    @Inject
    CollectionDatasource<Invoice, UUID> invoicesDs;


    public void xeroActionHandler() {

        boolean downloadSuccessful = xeroInvoiceService.importInvoices();

        if (downloadSuccessful) {
            invoicesDs.refresh();
        }

    }
}