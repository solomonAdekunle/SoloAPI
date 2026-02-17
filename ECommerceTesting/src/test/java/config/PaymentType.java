package config;

import pages.Page;

public enum PaymentType {

    VISA("Visa", "My Visa", "4004890306474240", "11/20", "101"),
    MASTERCARD("MasterCard", "My MasterCard", "5100009765143671", "11/20", "102"),
    AMEX_CORPORATE(new PropertiesReader().getProperty("amex.corporate.name"), "AmexPC", "374290482911992", "11/20", "200"),
    AMEX("American Express", "My Amex", "340000698274585", "11/20", "100"),
    VISA_CORPORATE(new PropertiesReader().getProperty("visa.corporate.name"), "VisaPC", "4715320629000001", "11/20", "201"),
    ALIPAY("Alipay", null, null, null, null),
    RS_ACCOUNT(Page.PROPS_READER.getProperty("rs.account.name"), null, null, null, "rs.gif"),
    DEFAULT_PAYMENT_METHOD("Default", null, null, null, "0");

    public static PaymentType currentPaymentType;
    private String name;
    private String chosenName;
    private String number;
    private String expiryDate;
    private String imageNumber;

    PaymentType(String name, String chosenName, String number, String expiryDate, String imageNumber) {
        this.name = name;
        this.chosenName = chosenName;
        this.number = number;
        this.expiryDate = expiryDate;
        this.imageNumber = imageNumber;
    }

    public static void setPaymentType(PaymentType type) {
        currentPaymentType = type;
    }

    public String getName() {
        return name;
    }

    public String getChosenName() {
        return chosenName;
    }

    public String getCardNumber() {
        return number;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getImageNumber() {
        return imageNumber;
    }
}
