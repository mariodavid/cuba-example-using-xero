# cuba-example-using-xero
CUBA example that shows how to use the Xero app component.


With everything configured in order to connect to the Xero API, the full SDK of Xero can be used in order to interact with different aspects of the API.

Here's an example that shows how to retrieve all invoices from Xero. After that the Invoices are transformed to persistent entities of the application in order to store those information.


```
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
```


The result looks like this in the UI after a successful download:

![Xero invoice download](https://github.com/mariodavid/cuba-example-using-xero/blob/master/img/1-xero-invoices-download.png)

### Configure the integration
In order to configure the connection to Xero correctly, this youtube video shows how to setup the integration: [CUBA Xero Java SDK Integration - configuration](https://youtu.be/A5HZM-wlOJA)
