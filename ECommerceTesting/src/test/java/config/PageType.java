package config;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public enum PageType {

    OUR_BRANDS("\"ecSystemId\":\"WEB\"", "\"page_type\":\"our brands\""),
    BRAND_L0("\"ecSystemId\":\"WEB\"", "\"page_type\":\"\"", "\"site_section\":\"Brand_"),
    BRAND_L1("\"ecSystemId\":\"WEB\"", "\"page_type\":\"Brand l1\""),
    BRAND_L2("\"ecSystemId\":\"WEB\"", "\"page_type\":\"Brand l2\""),
    WEB_SEARCH("\"ecSystemId\":\"WEB\"", "\"page_type\":\"new sr\""),
    WEB_HOME("\"ecSystemId\":\"WEB\"", "\"page_type\":\"home page\""),
    WEB_L1("\"ecSystemId\":\"WEB\"", "\"page_type\":\"new l1\""),
    WEB_L2("\"ecSystemId\":\"WEB\"", "\"page_type\":\"new l2\""),
    WEB_TN("\"site_type\":\"desktop\"", "\"page_type\":\"new tn\""),
    WEB_PRODUCT("\"ecSystemId\":\"WEB\"", "\"page_type\":\"new product\""),
    NA();

    private final Consumer<String> validator;

    PageType(String... expectedContents) {
        this.validator = body -> Arrays.stream(expectedContents).forEach(contents -> assertContains(body, contents));
    }

    private void assertContains(String actual, String expectedContents) {
        assertThat(actual).contains(expectedContents);
    }

    public Consumer<String> getValidator() {
        return validator;
    }

}
