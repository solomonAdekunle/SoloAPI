package config;

import pages.Page;


public enum ProductType {

    PRODUCT_WITH_BRAND_LOGO(Page.getProperty("product.with.brand.logo")),
    DEFAULT(Page.getProperty("product.default")),
    SECOND_DEFAULT(Page.getProperty("product.default.second")),
    DISCONTINUED(Page.getProperty("discontinued.product")),
    NO_REVIEW(Page.getProperty("product.without.review")),
    P_VARIANT(Page.getProperty("ngsp.product.with.p.pack")),
    IN_STOCK(Page.getProperty("product.in.stock")),
    QUOTES(Page.getProperty("product.quotes")),
    OUT_OF_STOCK(Page.getProperty("product.out.of.stock")),
    CALIBRATED_OPTION(Page.getProperty("product.calibrated.option")),
    WITH_ALTERNATIVE(Page.getProperty("product.with.alternative")),
    OUT_OF_STOCK_NO_ALTERNATIVE(Page.getProperty("product.out.of.stock.without.alternative")),
    WITH_SIMILAR(Page.getProperty("product.with.similar.products")),
    WITH_SIMILAR_IN_1_CATEGORY(Page.getProperty("similar.products.in.1.category")),
    WITH_SIMILAR_IN_MORE_THAN_1_CATEGORY(Page.getProperty("similar.products.in.more.than.1.category")),
    WITHOUT_SIMILAR(Page.getProperty("product.without.similar.products")),
    WITH_ESSENTIAL_EXTRAS(Page.getProperty("product.with.essential.extras")),
    THREED_IMAGE(Page.getProperty("product.with.3d")),
    WITH_MULTIPLE_IMAGES(Page.getProperty("product.with.multiple.images")),
    CUSTOMERS_ALSO_VIEWED(Page.getProperty("product.with.also.viewed")),
    WEE_TAX(Page.getProperty("product.with.weee.tax")),
    WITH_WARRANTY(Page.getProperty("product.with.warranty")),
    MAX_YOU_MAY_ALSO_LIKE(Page.getProperty("8-you-may-also-like")),
    DISCONTINUED_PRODUCT_NO_ALTERNATIVES_MPN(Page.getProperty("discontinued-no-alternatives-mpn")),
    DISCONTINUED_PRODUCT_NO_ALTERNATIVES_STOCKNUM(Page.getProperty("discontinued-no-alternatives-stocknum")),
    INVALID_STOCK_NUMBER("999999"),
    PRODUCTION_PACKAGING_KEY_PACK_BX_PR(Page.getProperty("production.packaging.key.PACK_BX_PR")),
    PRODUCTION_PACKAGING_KEY_UNIT_PK_EA(Page.getProperty("production.packaging.key.UNIT_PK_EA")),
    PRODUCTION_PACKAGING_KEY_PACK_EA_EA(Page.getProperty("production.packaging.key.PACK_EA_EA")),
    PRODUCTION_PACKAGING_KEY_UNIT_BX_EA(Page.getProperty("production.packaging.key.UNIT_BX_EA")),
    PRODUCTION_PACKAGING_KEY_PACK_BX_EA(Page.getProperty("production.packaging.key.PACK_BX_EA")),
    PRODUCTION_PACKAGING_KEY_PACK_PU_EA(Page.getProperty("production.packaging.key.PACK_PU_EA")),
    PRODUCTION_PACKAGING_KEY_UNIT_TU_EA(Page.getProperty("production.packaging.key.UNIT_TU_EA")),
    PRODUCTION_PACKAGING_KEY_UNIT_PU_EA(Page.getProperty("production.packaging.key.UNIT_PU_EA")),
    PRODUCTION_PACKAGING_KEY_PACK_BG_EA(Page.getProperty("production.packaging.key.PACK_BG_EA")),
    PRODUCTION_PACKAGING_KEY_PACK_RL_EA(Page.getProperty("production.packaging.key.PACK_RL_EA")),
    PRODUCTION_PACKAGING_KEY_PACK_TU_EA(Page.getProperty("production.packaging.key.PACK_TU_EA")),
    PRODUCTION_PACKAGING_KEY_UNIT_BG_EA(Page.getProperty("production.packaging.key.UNIT_BG_EA")),
    PRODUCTION_PACKAGING_KEY_UNIT_EA_EA(Page.getProperty("production.packaging.key.UNIT_EA_EA")),
    PRODUCTION_PACKAGING_KEY_UNIT_RL_EA(Page.getProperty("production.packaging.key.UNIT_RL_EA")),
    PRODUCTION_PACKAGING_KEY_PACK_TA_EA(Page.getProperty("production.packaging.key.PACK_TA_EA")),
    PRODUCTION_PACKAGING_KEY_UNIT_TA_EA(Page.getProperty("production.packaging.key.UNIT_TA_EA")),
    STANDARD_PACKAGING_KEY_PACK_BX_PR(Page.getProperty("standard.packaging.key.PACK_BX_PR")),
    STANDARD_PACKAGING_KEY_UNIT_PK_EA(Page.getProperty("standard.packaging.key.UNIT_PK_EA")),
    STANDARD_PACKAGING_KEY_PACK_EA_EA(Page.getProperty("standard.packaging.key.PACK_EA_EA")),
    STANDARD_PACKAGING_KEY_UNIT_BX_EA(Page.getProperty("standard.packaging.key.UNIT_BX_EA")),
    STANDARD_PACKAGING_KEY_PACK_BX_EA(Page.getProperty("standard.packaging.key.PACK_BX_EA")),
    STANDARD_PACKAGING_KEY_PACK_PU_EA(Page.getProperty("standard.packaging.key.PACK_PU_EA")),
    STANDARD_PACKAGING_KEY_UNIT_TU_EA(Page.getProperty("standard.packaging.key.UNIT_TU_EA")),
    STANDARD_PACKAGING_KEY_UNIT_PU_EA(Page.getProperty("standard.packaging.key.UNIT_PU_EA")),
    STANDARD_PACKAGING_KEY_PACK_BG_EA(Page.getProperty("standard.packaging.key.PACK_BG_EA")),
    STANDARD_PACKAGING_KEY_PACK_RL_EA(Page.getProperty("standard.packaging.key.PACK_RL_EA")),
    STANDARD_PACKAGING_KEY_PACK_TU_EA(Page.getProperty("standard.packaging.key.PACK_TU_EA")),
    STANDARD_PACKAGING_KEY_UNIT_BG_EA(Page.getProperty("standard.packaging.key.UNIT_BG_EA")),
    STANDARD_PACKAGING_KEY_UNIT_EA_EA(Page.getProperty("standard.packaging.key.UNIT_EA_EA")),
    STANDARD_PACKAGING_KEY_UNIT_RL_EA(Page.getProperty("standard.packaging.key.UNIT_RL_EA")),
    STANDARD_PACKAGING_KEY_PACK_TA_EA(Page.getProperty("standard.packaging.key.PACK_TA_EA")),
    STANDARD_PACKAGING_KEY_UNIT_TA_EA(Page.getProperty("standard.packaging.key.UNIT_TA_EA")),
    STANDARD_PACKAGING_REEL_OF_X(Page.getProperty("standard.packaging.reel.of.x")),
    STANDARD_PACKAGING_BAG_OF_X(Page.getProperty("standard.packaging.bag.of.x")),
    STANDARD_PACKAGING_TUBE_OF_X(Page.getProperty("standard.packaging.tube.of.x"));

    String stockNumber;

    ProductType(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getStockNumber() {
        return stockNumber;
    }

}
