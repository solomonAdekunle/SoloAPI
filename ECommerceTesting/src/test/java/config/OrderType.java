package config;


public enum OrderType {

    NORMAL(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
    GUEST(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false),
    TRADE_COUNTER(false, false, true, false, false, false, false, false, false, false, false, false, false, false, false),
    FAO(false, false, false, true, false, false, false, false, false, false, false, false, false, false, false),
    BLANKET_ORDER_NUMBER(false, false, false, false, true, false, false, false, false, false, false, false, false, false, false),
    FORWARD_ORDER_DATE(false, false, false, false, false, true, false, false, false, false, false, false, false, false, false),
    SCHEDULED(false, false, false, false, false, false, true, false, false, false, false, false, false, false, false),
    ALIPAY(false, false, false, false, false, false, false, true, false, false, false, false, false, false, false),
    PREMIUM_DELIVERY(false, false, false, false, false, false, false, false, true, false, false, false, false, false, false),
    PROMO_CODE(false, false, false, false, false, false, false, false, false, true, false, false, false, false, false),
    BASKET_LOG_IN(false, false, false, false, false, false, false, false, false, false, true, false, false, false, false),
    COST_CENTRE(false, false, false, false, false, false, false, false, false, false, false, true, false, false, false),
    EXPRESS_CHECKOUT(false, false, false, false, false, false, false, false, false, false, false, false, true, false, false),
    NEW_ADDRESS(false, true, false, false, false, false, false, false, false, false, false, false, false, false, false),
    GUEST_TC(false, false, false, false, false, false, false, false, false, false, false, false, false, true, false),
    PM(false, false, false, false, false, false, false, false, false, false, false, false, false, false, true);

    private boolean guest, newAddress, tradeCounter, fao, blanketOrderNumber, forwardOrderDate, scheduled, alipay, premiumDelivery, promoCode, basketLogIn, costCentre, expressCheckout, guestTC, pm;

    OrderType(boolean guest, boolean newAddress, boolean tradeCounter, boolean fao, boolean blanketOrderNumber,
              boolean forwardOrderDate, boolean scheduled, boolean alipay, boolean premiumDelivery, boolean promoCode, boolean basketLogIn, boolean costCentre, boolean expressCheckout, boolean guestTC, boolean pm) {
        this.guest = guest;
        this.newAddress = newAddress;
        this.tradeCounter = tradeCounter;
        this.fao = fao;
        this.blanketOrderNumber = blanketOrderNumber;
        this.forwardOrderDate = forwardOrderDate;
        this.scheduled = scheduled;
        this.alipay = alipay;
        this.premiumDelivery = premiumDelivery;
        this.promoCode = promoCode;
        this.basketLogIn = basketLogIn;
        this.costCentre = costCentre;
        this.expressCheckout = expressCheckout;
        this.guestTC = guestTC;
        this.pm = pm;
    }


    public boolean isGuest() {
        return guest;
    }

    public boolean isNewAddress() {
        return newAddress;
    }

    public boolean isTradeCounter() {
        return tradeCounter;
    }

    public boolean isFao() {
        return fao;
    }

    public boolean isBlanketOrderNumber() {
        return blanketOrderNumber;
    }

    public boolean isForwardOrderDate() {
        return forwardOrderDate;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public boolean isAlipay() {
        return alipay;
    }

    public boolean isPremiumDelivery() {
        return premiumDelivery;
    }

    public boolean isPromoCode() {
        return promoCode;
    }

    public boolean isBasketLogIn() {
        return basketLogIn;
    }

    public boolean isCostCentre() {
        return costCentre;
    }

    public boolean isExpressCheckout() {
        return expressCheckout;
    }

    public boolean isGuestTC() {
        return guestTC;
    }

    public boolean isPM() {
        return pm;
    }
}
