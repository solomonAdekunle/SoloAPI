package entities;

import pages.Page;
import java.util.UUID;

public enum CustomerPartNumber {
    PRODUCT_A(Page.getProperty("product.with.cpn.a"),Page.getProperty("cpn.for.product.a")),
    PRODUCT_B(Page.getProperty("product.with.cpn.b"),Page.getProperty("cpn.for.product.b")),
    PRODUCT_C(Page.getProperty("product.with.cpn.c"),Page.getProperty("cpn.for.product.c")),
    PRODUCT_TO_ASSIGN_USED_CPNS(Page.getProperty("product.to.assign.used.cpns"),""),
    PRODUCT_WITHOUT_CPN(Page.getProperty("product.without.cpn"),""),
    CPN_NEW("",generateString()),
    PRODUCT_CASE_SENSITIVE_MISMATCH(Page.getProperty("product.case.sensitivity.test"),Page.getProperty("cpn.case.sensitivity.negative.test")),
    PRODUCT_CASE_SENSITIVE_MATCH(Page.getProperty("product.case.sensitivity.test"),Page.getProperty("cpn.case.sensitivity.positive.test")),
    INVALID("999999","999999"),
    EMPTY_VALUE("","");

    private final String rsStockNo;
    private final String customerPartNumber;

    CustomerPartNumber(String rsStockNo, String customerPartNumber) {
        this.rsStockNo = rsStockNo;
        this.customerPartNumber = customerPartNumber;
    }

    public String getCustomerPartNumber() {
        return customerPartNumber;
    }

    public String getRsStockNo() {
        return rsStockNo;
    }

    private static String generateString() {
        String uuid = UUID.randomUUID().toString();
        String shortenedUuid = uuid.replace("-", "");
        return shortenedUuid;
    }
}
