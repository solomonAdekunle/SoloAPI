package config;


public enum Customer {

    PM3_PSD2_APPROVER("","","","","anhapprover","password","","",""),
    CUSTOMER_WITH_ACCOUNT("ABB LTD", "RUDI", "HERMANN", "Mr", "CUSTOMER_WITH_ACCOUNT", "Password", "0800123456", "recommercetestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_ACCOUNT_PREP_JP("ABB LTD", "Agile", "User1", "Mr", "Robot", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_PM("ABB LTD", "CUSTOMER", "WITHPM", "Mr", "CUSTOMER_WITH_PM", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_PM_TEAM4_DEVCI("ABB LTD", "NIGEL", "DAWES", "Mr", "nigeld2", "password", "01327 359444", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_PM_ST2("ABB LTD", "Agile", "User1", "Mr", "Adapt", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_EXPRESS_CHECKOUT("Test Company", "CUSTOMER ", "WITHEXPRESS", "Mr", "CUSTOMER_WITH_EXPRESS_CHECKOUT", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_CONTACT_ID("Test Company", "CUSTOMER", "WITHCONTACT", "Miss", "CUSTOMER_WITH_CONTACT_ID", "Password", "0161 941 2553", "emailmichelle.simpson@nccgroup.com", "BJH ELECTRICAL CONTRACTORS LTD, 22 BONVILLE CHASE, DUNHAM PARK, ALTRINCHAM, CHESHIRE, WA14 4QA, UK"),
    CUSTOMER_WITHOUT_CONTACT_ID("Test Company", "CUSTOMER", "WITHOUTCONTACTID", "Mr.", "CUSTOMER_WITHOUT_CONTACT_ID", "Password", "0161 941 2553", "emailmichelle.simpson@nccgroup.com", "BJH ELECTRICAL CONTRACTORS LTD, 22 BONVILLE CHASE, DUNHAM PARK, ALTRINCHAM, CHESHIRE, WA14 4QA, UK"),
    CUSTOMER_WITH_ORDER_HISTORY("Test Company", "CUSTOMER", "WITHORDERHISTORY", "Miss", "CUSTOMER_WITH_ORDER_HISTORY", "Password", "0161 941 2553", "emailmichelle.simpson@nccgroup.com", "BJH ELECTRICAL CONTRACTORS LTD, 22 BONVILLE CHASE, DUNHAM PARK, ALTRINCHAM, CHESHIRE, WA14 4QA, UK"),
    CUSTOMER_WITH_500_ITEMS_IN_BASKET("ABB LTD", "CUSTOMER ", "WITH500", "Mr", "CUSTOMER_WITH_500_ITEMS_IN_BASKET", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_TEN_PERCENT_DISCOUNT("ABB LTD", "CUSTOMER ", "WITH10PERCENT", "Mr", "CUSTOMER_WITH_TEN_PERCENT_DISCOUNT", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_PARTS_LIST("ABB LTD", "CUSTOMER ", "WITHPARTSLIST", "Mr", "CUSTOMER_WITH_PARTS_LIST", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    CUSTOMER_WITH_PART_NUMBERS("ABB LTD", "CUSTOMER ", "WITHPARTNUMBERS", "Mr", "CUSTOMER_WITH_PART_NUMBERS", "Password", "0800123456", "rcentraltestteam@rs-components.com", "Acme, 1 Something Street, TestTown, NW11WN, UK"),
    PM3_APPROVER_100_LIMIT("", "APPROVER", "LIMIT100", "", "Approval Limit 100", "password", "", "", ""),
    PM3_APPROVER_200_LIMIT("", "", "", "", "Approval Limit 200", "password", "", "", ""),
    PM3_APPROVER_500_LIMIT("", "", "", "", "Approval Limit 500", "password", "", "", ""),
    PM3_APPROVER_LIMIT_UNLIMITED("", "", "", "", "Approval Limit Unlimited", "password", "", "", ""),
    PM3_ORDER_PLACER_SINGLE_ORDER_LIMIT_DISABLED("", "", "", "", "SpendControlSingleOrderLimitNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_MONTHLY_ORDER_LIMIT_DISABLED("", "", "", "", "SpendControlMonthlyOrderLimitNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_DEPT_BUDGET_DISABLED("", "", "", "", "SpendControlBudgetLimitNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_PERSONAL_BUDGET_DISABLED("", "", "", "", "SpendControlPersonalBudgetLimitNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_REMAINING_DEPT_BUDGET_DISABLED("", "SpendControl", "RemainingBudgetNotVisible", "", "SpendControlRemainingBudgetNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_PERSONAL_BUDGET_START_DATE_DISABLED("", "", "", "", " ", "password", "", "", ""),
    PM3_ORDER_PLACER_MANDATORY_BLANKET_ORDER("", "BlanketOrder", "Mandatory", "", "BlanketOrderMandatory", "password", "", "", ""),
    PM3_ORDER_PLACER_MANDATORY_COST_CENTRE_LINE_LEVEL("", "CostCentre", "UsedLineLevelMandatory", "", "CostCentreUsedLineLevelMandatory", "password", "", "", ""),
    PM3_SUPER_ADMIN_UK("", "RSInternal", "TestSuperAdminUK1", "", "RSInternalTestSuperAdminUK1", "password", "", "", ""),
    PM3_ADMIN_UK("", "RSInternal", "TestAdminUK1", "", "RSInternalTestAdminUK1", "password", "", "", ""),
    PM3_REPORTER_UK("", "RSInternal", "TestReporterUK1", "", "RSInternalTestReporterUK1", "password", "", "", ""),
    PM3_ADMIN_AU("", "", "", "", "rsadmabb", "password", "", "", ""),
    PM3_REPORTER_AU("", "", "", "", "david.bailey@au.abb.com", "password", "", "", ""),
    PM3_APPROVER_AU("", "", "", "", "JohnBest", "password", "", "", ""),
    PM3_ST2_REPORTER_AU("", "", "", "", "peter_smith", "password", "", "", ""),
    PM3_ST2_APPROVER_AU("", "", "", "", "PhilGoodger", "password", "", "", ""),
    PM3_VIEWER_UK("", "RSInternal", "TestViewerUK1", "", "RSInternalTestViewerUK1", "password", "", "", ""),
    PM3_ORDER_PLACER_SINGLE_LIMIT_ENABLED("", "SpendControl", "SingleOrderLimitVisible", "", "SpendControlSingleOrderLimitVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_MONTHLY_LIMIT_ENABLED("", "SpendControl", "MonthlyOrderLimitVisible", "", "SpendControlMonthlyOrderLimitVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_CUSTOM_DELIVERY_ADDRESS("", "DeliveryAddress", "CustomTerms", "", "DeliveryAddressCustomTerms", "password", "", "", ""),
    PM3_ORDER_DEPT_BUDGET_ENABLED("", "SpendControl", "RemainingBudgetVisible", "", "SpendControlRemainingBudgetVisible", "password", "", "", ""),
    PM3_PERSONAL_BUDGET_ENABLED("", "SpendControl", "", "", "SpendControlSingleOrderUniqueLimit", "password", "", "", ""),
    PM3_PERSONAL_BUDGET_START_DATE_ENABLED("", "SpendControl", "SingleOrderUniqueLimit", "", " ", "password", "", "", ""),
    PM3_PERSONAL_BUDGET_END_DATE_ENABLED("", "SpendControl", "", "", " ", "password", "", "", ""),
    PM3_ORDER_PLACER_CREDIT_LIMIT_ENABLED("", "SpendControl", "", "", " ", "password", "", "", ""),
    PM3_ORDER_PLACER_SINGLE_ORDER_LIMIT_NOT_VISIBLE("", "SpendControl", "", "", " ", "password", "", "", ""),
    PM3_ORDER_PLACER_MONTHLY_ORDER_LIMIT_NOT_VISIBLE("", "SpendControl", "", "", " ", "password", "", "", ""),
    PM3_ORDER_PLACER_DEPT_BUDGET_LIMIT_NOT_VISIBLE("", "SpendControl", "", "", " ", "password", "", "", ""),
    PM3_ORDER_PLACER_PERSONAL_BUDGET_LIMIT_NOT_VISIBLE("", "SpendControl", "PersonalBudgetLimitNotVisible", "", "SpendControlPersonalBudgetLimitNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_REMAINING_PERSONAL_BUDGET_NOT_VISIBLE("", "SpendControl", "RemainingPersonalBudgetNotVisible", "", "SpendControlRemainingPersonalBudgetNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_PERSONAL_BUDGET_START_DATE_NOT_VISIBLE("SpendControl", "PersonalBudgetLimitNotVisible", "", "", "SpendControlPersonalBudgetLimitNotVisible", "password", "", "", ""),
    PM3_ORDER_PLACER_COST_CENTRE_USED_CUSTOM_TERMS("", "CostCentre", "UsedCustomTerms", "", "CostCentreUsedCustomTerms", "password", "", "", ""),
    PM3_ORDER_PLACER_GL_CODE_USED_CUSTOM_TERMS("", "GLCode", "UsedCustomTerms", "", "GLCodeUsedCustomTerms", "password", "", "", ""),
    PM3_ORDER_PLACER_BLANKET_ORDER_CUSTOM_TERMS("", "BlanketOrder", "CustomTerms", "", "BlanketOrderCustomTerms", "password", "", "", ""),
    PM3_ORDER_PLACER_PAYMENT_TYPE_CUSTOM_TERMS("", "PaymentType", "CustomTerms", "", "PaymentTypeCustomTerms", "password", "", "", ""),
    PM3_ORDER_PLACER_PAYMENT_TYPE_CUSTOM_REFERENCE_MANDATORY("", "PaymentType", "CustomerReferenceFieldMandatory", "", "PaymentTypeCustomerReferenceFieldMandatory", "password", "", "", ""),
    PM3_RS_INTERNAL_TEST_ORDER_PLACER("", "RSINTERNAL", "TESTORDERPLACERUK1", "", "RSInternalTestOrderPlacerUK1", "password", "", "", ""),
    PM_SUPER_ADMIN_UK("", "RSInternal", "TestSuperAdminUK1", "", "RSInternalTestSuperAdminUK1", "password", "", "", ""),
    PM3_STATICDEV_MEGA_ADMIN_UK("", "RSHQUSER", "UK", "", "OPTIPLEX755", "TestPassword", "", "", ""),
    PM3_RSHQ_UK("", "RSHQUSER", "UK", "", "OPTIPLEX755", "BTConverse125", "", "", ""),
    PM_ADMIN("", "AutoHQ", "User", "", "AutoHQUser", "password", "", "", ""),
    PM_DOUBLE_BYTE_USER("", "DOUBLE BYTE", "User", "", "这是一个双字节用户", "password", "", "", ""),
    CN_PM_DOUBLE_BYTE_USER("", "DOUBLE BYTE", "User", "", "登录CN_PM3", "password", "", "", ""),
    DE_PM_DOUBLE_BYTE_USER("", "DOUBLE BYTE", "User", "", "über_können", "password", "", "", ""),
    CUSTOMER_WITH_RESTRICTED_PRODUCTS("", "RSInternalTest ", "RestrictedProducts", "", "RSInternalTestRestrictedProducts", "password", "", "", ""),
    CUSTOMER_WITH_BARRED_PRODUCTS("", "RSInternalTest ", "BarredProducts", "", "RSInternalTestBarredProducts", "password", "", "", ""),
    RS_INTERNAL_TEST_RS_ADMIN_UK2("", "RS_ADMIN_UK2", "RS_INTERNAL_TEST", "", "RS_INTERNAL_TEST_RS_ADMIN_UK2", "password", "", "", ""),
    RS_INTERNAL_TEST_ASSIGN_USER_ROLE("", "ASSIGN_USER_ROLE", "RS_INTERNAL_TEST", "", "RS_INTERNAL_TEST_ASSIGN_USER_ROLE", "password", "", "", ""),
    CUSTOMER_ON_PROD("", "TEST", "PROD", "Mr", "CUSTOMER_ON_PROD", "Password", "0800123456", "recommercetestteam@rs-components.com", ""),
    CUSTOMER_WITHOUT_CUSTOM_PRICING("", "", "", "", "testmania", "password", "", "", ""),
    CUSTOMER_WITH_CUSTOM_PRICING("", "", "", "", "UK316145", "12345", "", "", ""),
    CUSTOMER_WITH_CUSTOM_PRICING_FOR_SPECIFIC_CATEGORIES("", "", "", "", "CUSTOMER_WITH_2718_SCHNEIDER_ELECTRIC_ENCLOSURES_DISCOUNT", "password", "", "", ""),
    EPROC_CUSTOMER("", "", "", "", "", "", "", "", ""),
    CUSTOMER_WHO_IS_IDENTIFIED("", "", "", "", "Testman", "password", "", "", ""),
    CUSTOMER_WITH_SOLD_TO("", "", "", "", "CUSTOMER_WHO_IS_IDENTIFIED_WITH_A_SOLD_TO", "password", "", "", ""),
    CUSTOMER_WITHOUT_SOLD_TO("", "", "", "", "Testman", "password", "", "", ""),
    CUSTOMER_WHO_IS_IDENTIFIED_WITH_A_SOLD_TO("", "", "", "", "CUSTOMER_WHO_IS_IDENTIFIED_WITH_A_SOLD_TO", "password", "", "", ""),
    CUSTOMER_WITH_PM_AND_CSP("", "", "", "", "CustomerSpecificPricing", "Password", "", "", ""),
    PM3_SUPER_ADMIN_AU("", "", "", "", "RSInternalTestSuperAdminAU1", "password", "", "", ""),
    PM3_SUPER_ADMIN_DE("", "", "", "", "RSInternalTestSuperAdminDE1", "password", "", "", ""),
    PM3_SUPER_ADMIN_F1("", "RSInternal", "TestSuperAdminF1", "", "RSInternalTestSuperAdminF1", "password", "", "", ""),
    CUSTOMER_WITH_PM_PROD_UK("", "", "", "", "ELS_DEPLOYMENT_2", "password", "", "", ""),
    PM3_APPROVER_PROD_UK("", "", "", "", "Test_UK", "password", "", "", ""),
    CUSTOMER_ON_PROD_JP("", "", "", "", "Tester", "password", "", "", ""),
    DISCOVERY_DEV_DE_PM_USER_WITH_BARRED_PRODUCTS("", "", "", "", "KarinKoch", "Password", "", "", ""),
    DISCOVERY_DEV_JP_PM_USER_WITH_BARRED_PRODUCTS("", "", "", "", "tdwyama", "Password", "", "", ""),
    DISCOVERY_DEV_SG_PM_USER_WITH_BARRED_PRODUCTS("", "", "", "", "E5110311", "Password", "", "", ""),
    DISCOVERY_DEV_AU_PM_USER_WITH_BARRED_PRODUCTS("", "", "", "", "psimcik01", "Password", "", "", ""),
    DISCOVERY_DEV_UK_PM_USER_WITH_BARRED_PRODUCTS("", "", "", "", "neilhardstaff", "Password", "", "", ""),
    DISCOVERY_DEV_UK_PM_USER_WITH_BARRED_CATEGORIES("", "", "", "", "t.innes", "Password", "", "", ""),
    DISCOVERY_DEV_JP_PM_USER_WITH_BARRED_CATEGORIES("", "", "", "", "tkamashi", "Password", "", "", ""),
    ST2_JP_PM_USER_WITH_BARRED_PRODUCTS("", "", "", "", "myway", "Password", "", "", ""),
    ST2_UK_CUSTOMER_WITH_PM("", "", "", "", "wandareporter", "Password", "", "", ""),
    PM_CUSTOMER_ADD_OWN_CARD("", "", "", "", "PMCUSTOMER_ADDOWNCARD", "password", "", "", ""),
    INVALID_CUSTOMER("", "", "", "", "customer_with_account", "password", "", "", ""),
    PM3_PSD2_ORDERPLACER("", "PSD2", "ORDERPLACER" , "", "PSD2_ORDERPLACER", "password", "", "", ""),
    PM3_PSD2_APPROVER_UNCHANGED("", "PSD2", "APPROVER" , "", "PSD2_APPROVER", "password", "", "", ""),
    PM3_APPROVER_EDIT_PURCHASE_ORDER_FIELD( "", "PM3EDIT" , "PURCHASE" , "", "PM3EDIT_PURCHASE", "password", "", "", ""),
    PM3_GOODS_RECEIPTING_ORDERER( "", "GOODS" , "RECEIPTING" , "", "GOODS_RECEIPTING", "password", "", "", ""),
    PM3_GOODS_RECEIPTING_APPROVER("", "RECEIPTING", "APPROVER", "", "RECEIPTING_APPROVER", "password", "", "", "");

    public static Customer currentCustomer;
    private String company_name;
    private String first_name;
    private String last_name;
    private String title;
    private String user_name;
    private String password;
    private String contact_number;
    private String email_address;
    private String address;

    Customer(String company_name, String first_name, String last_name, String title, String user_name, String password, String contact_number, String email_address, String address) {
        this.company_name = company_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.title = title;
        this.user_name = user_name;
        this.password = password;
        this.contact_number = contact_number;
        this.email_address = email_address;
        this.address = address;
    }

    public static void setCustomer(Customer cust) {
        currentCustomer = cust;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getTitle() {
        return title;
    }

    public String getEmailAddress() {
        return email_address;
    }

    public String getUserName() {
        return user_name;
    }

    public String getFullName() {
        return first_name + " " + last_name;
    }

    public String getFullNameWithTitle() {
        return title + " " + first_name + " " + last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getContactNumber() {
        return contact_number;
    }

    public String getAddress() {
        return address;
    }


}