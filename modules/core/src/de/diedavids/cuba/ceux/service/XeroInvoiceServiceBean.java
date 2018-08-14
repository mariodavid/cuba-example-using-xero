package de.diedavids.cuba.ceux.service;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.xero.api.XeroClient;
import de.diedavids.cuba.ceux.entity.Invoice;
import de.diedavids.cuba.xero.core.XeroClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@Service(XeroInvoiceService.NAME)
public class XeroInvoiceServiceBean implements XeroInvoiceService {

    private static final Logger log = LoggerFactory.getLogger(XeroInvoiceServiceBean.class);

    @Inject
    XeroClientFactory xeroClientFactory;

    @Inject
    Metadata metadata;

    @Inject
    DataManager dataManager;

    @Override
    public boolean importInvoices() {


        XeroClient client = xeroClientFactory.createClient();

        try {
            List<com.xero.model.Invoice> xeroInvoiceList = client.getInvoices();

            persistXeroInvoices(xeroInvoiceList);
        } catch (IOException e) {
            log.error("Error while retrieving Invoices from Xero.", e);
        }


        return true;

    }


    private void persistXeroInvoices(List<com.xero.model.Invoice> xeroInvoiceList) {

        for (com.xero.model.Invoice xeroInvoice : xeroInvoiceList) {

            Invoice invoiceEntity = metadata.create(Invoice.class);

            invoiceEntity.setInvoiceNumber(xeroInvoice.getInvoiceNumber());
            invoiceEntity.setInvoiceDate(xeroInvoice.getDate().getTime());
            invoiceEntity.setAmountDue(xeroInvoice.getAmountDue());
            invoiceEntity.setAmountPaid(xeroInvoice.getAmountPaid());
            invoiceEntity.setTotal(xeroInvoice.getTotal());
            invoiceEntity.setSubTotal(xeroInvoice.getSubTotal());
            invoiceEntity.setTotalTax(xeroInvoice.getTotalTax());

            dataManager.commit(invoiceEntity);
        }

    }
}