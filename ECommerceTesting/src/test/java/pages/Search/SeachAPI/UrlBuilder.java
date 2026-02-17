package pages.Search.SeachAPI;


import config.PropertiesReader;

import java.net.URI;
import java.util.Locale;

public class UrlBuilder {
    private static PropertiesReader props = new PropertiesReader();

    private static final String VERSION = System.getProperty("version");
    private static final String CLOUD = "cloud";
    private static final String V2 = "v2";
    private static final String LOCAL = "local";
    private static final String LEGACY = "legacy";

    public static URI getSearchBaseURI() {
        String url = "";
        String versionToTest = VERSION != null ? VERSION : "cloud";
        switch (versionToTest) {
            case V2:
                url = props.getProperty("search.base.url.v2");
                break;
            case LOCAL:
                url = props.getProperty("search.base.url.local");
                break;
            case LEGACY:
                url = props.getProperty("search.base.url");
                break;
            default:
                url = props.getProperty("search.base.url.cloud");
        }

        return URI.create(url);
    }

    public static URI getSearchAPIPSFSortBaseURI() {
        String version = System.getProperty("version");
        if (version != null && version.equalsIgnoreCase("v2")) {
            return URI.create(props.getProperty("l3.base.url.v2"));
        } else if (version != null && version.equalsIgnoreCase("cloud")) {
            return URI.create(props.getProperty("l3.base.url.cloud"));
        } else if (version != null && version.equalsIgnoreCase("local")) {
            return URI.create(props.getProperty("l3.base.url.local"));

        } else {
            return URI.create(props.getProperty("l3.base.url"));
        }
    }

    public static String getProductURI() {
        if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("v2")) {
            return props.getProperty("search.product.url.v2");
        } else if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("cloud")) {
            return props.getProperty("search.product.url.cloud");
        } else if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("local")) {
            return props.getProperty("search.product.url.local");
        } else {
            return props.getProperty("search.product.url");
        }
    }

    public static String getProductAlternativesURI() {
        if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("v2")) {
            return props.getProperty("product.alternatives.url.v2");
        } else if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("cloud")) {
            return props.getProperty("product.alternatives.url.cloud");
        } else if(System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("local")) {
            return props.getProperty("product.alternatives.url.local");
        } else {
            return props.getProperty("product.alternatives.url");
        }
    }

    public static URI getSearchAPIPSFAndPSSSHierarchyBaseURI() {
        if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("v2")) {
            return URI.create(props.getProperty("search.category.hierarchy.base.url.v2"));
        } else if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("cloud")) {
            return URI.create(props.getProperty("search.category.hierarchy.base.url.cloud"));
        } else if (System.getProperty("version") != null && System.getProperty("version").equalsIgnoreCase("local")) {
            return URI.create(props.getProperty("search.category.hierarchy.base.url.local"));
        } else {
                return URI.create(props.getProperty("search.category.hierarchy.base.url"));
        }

    }

      public static String getRequestQueryParam(String key) {
        return props.getProperty(key);
    }

    public static String getRequestPSFQueryParam() {
        return props.getProperty("search.psf.ancestors");
    }

    public static String getPSFQueryParamSeoUrl() {
        return props.getProperty("search.psf.ancestors.seourl");
    }

    public static String getNtxParam() {
        return props.getProperty("ntx.param");
    }

    public static String getSpellParam() {
        return props.getProperty("spell.param");
    }


    public static String getRequestPSSSQueryParam() {
        return props.getProperty("search.psss.child.hierarchy");
    }

    public static String getRequestAssemblerQueryParam() {
        return props.getProperty("assembler.request.param");
    }

    public static String getRequestPSSSAssemblerQueryParam() {
        return props.getProperty("assembler.psss.child.hierarchy.param");
    }

    public static URI getAssemblerBaseURI() {
        return URI.create(props.getProperty("assembler.base.url"));
    }

    public static URI getAssemblerSearchBaseURI() {
        return URI.create(props.getProperty("assembler.search.base.url"));
    }

    public static String getRequestTermNodeQueryParam() {
        return props.getProperty("locale.id");
    }

    public static String getAttributeFilterQueryParam() {
        return props.getProperty("applied.dimensions.param");
    }

    public static String getLevel3BaseUrl() {
        return props.getProperty("assembler.level3.base.url");
    }

    public static String getL3Param() {
        return props.getProperty("assembler.request.level3.param");
    }

    public static String getAssemblerPopularityParam() {
        return props.getProperty("assembler.popularity.param");
    }

    public static String getPopularityParam() {
        return props.getProperty("popularity.url.param");
    }

    public static String getInvalidPopularityParam() {
        return props.getProperty("invalid.popularity.param");
    }

    public static String getHighToLowParam() {
        return props.getProperty("highToLow.url.param");
    }

    public static String getAssemblerHighToLowParam() {
        return props.getProperty("assembler.HighToLow.Param");
    }

    public static String getLowToHighParam() {
        return props.getProperty("lowToHigh.url.param");
    }

    public static String getCapacityParam() {
        return props.getProperty("capacity.url.param");
    }

    public static String getCapacityOrder(String order) {
        return props.getProperty(order);
    }

    public static String getAssemblerLowToHighParam() {
        return props.getProperty("assembler.lowToHigh.param");
    }

    public static String getAssemblerCapacityParam() {
        return props.getProperty("assembler.capacity.param");
    }

    public static String getAssemblerRelevanceParam() {
        return props.getProperty("assembler.relevance.param");
    }

    public static String getl3CategorySearchParam() {
        return props.getProperty("l3.category.param");
    }

    public static String getLocaleSubelevelsParam() {
        return props.getProperty("locale.sublevels.param");
    }

    public static String getInvalidLimitParam() {
        return props.getProperty("limit.locale.param");
    }

    public static String getSeoLocaleParam() {
        return props.getProperty("seourl.locale.param");
    }

    public static String getInternalIdLocaleParam() {
        return props.getProperty("internalId.locale.param");
    }

    public static String getInvalidInternalIdLocaleParam() {
        return props.getProperty("invalid.internalid.param");
    }

    public static String getLimitParam() {
        return props.getProperty("limit.param");
    }

    public static String getBelowLimitParam() {
        return props.getProperty("below.limit.param");
    }

    public static String getPaginationPageParam() {
        return props.getProperty("pagination.param");
    }

    public static String getAssemblerPaginationParams() {
        return props.getProperty("assembler.paginationpage.2.params");
    }

    public static String getAssemblerFilterParam() {
        return props.getProperty("assembler.filter.param");
    }

    public static String getPaginationPageParams() {
        return props.getProperty("pagination.page.param");
    }

    public static String getPaginationLimitParams() {
        return props.getProperty("pagination.limit.param");
    }

    public static String getAssemblerFilterNrppParam() {
        return props.getProperty("pagination.filter.nrpp.param");
    }

    public static String getNrppParam() {
        return props.getProperty("nrpp.param");
    }

    public static String getInternalIdParam() {
        return props.getProperty("internal.id.param");
    }

    public static String getPsfHieranchyParams() {
        return props.getProperty("psf.hierarchy.param");
    }

    public static String getBooksList() {
        return props.getProperty("books.list");
    }

    public static String getSpecificationAttributesList() {
        return props.getProperty("specificationAttributes");
    }

    public static String getTargetStateSupressQueryParam() {
        return props.getProperty("targetState.collapse.param");
    }

    public static String getassemblerQueryConstantParam() {
        return props.getProperty("assembler.query.constant.param");
    }

    public static String getAssemblerSeachQueryParam() {
        return props.getProperty("assembler.search.query.param");
    }

    public static String getAssemblerSearchBaseUrl() {
        return props.getProperty("assembler.search.base.url");
    }

    public static String getDecodedAssemblerPopularityParam() {
        return props.getProperty("assembler.popularity.param.uncoded");
    }

    public static String getAssemblerNtkParamSAPI() {
        return props.getProperty("ntk.filter.param");
    }

    public static String getAssemblerNtkStockInterfaceParam() {
        return props.getProperty("ntk.stock.number.interface.filter.param");
    }

    public static String getAssemblerLocaleId() {
        return props.getProperty("assembler.locale.id");
    }

    public static String getLocaleSapi() {
        return props.getProperty("locale.id.sapi");
    }

    public static String getSearchTermQueryParam() {
        return props.getProperty("search.term.query.param");
    }

    public static String getStratifyParam() {
        return props.getProperty("stratify.param");
    }

    public static String getBoostAssemblerRelRankingParam() {
        return props.getProperty("boost.assembler.rel.ranking.param");
    }

    public static String getBuryAssemblerRelRankingParam() {
        return props.getProperty("bury.assembler.rel.ranking.param");
    }

    public static String getNrrpParam() {
        return props.getProperty("nrrp.assembler.param");
    }

    public static String getExpectedBuriedPath() {
        return props.getProperty("expected.buried.rule.path");
    }

    public static String getExpectedBoostedPath() {
        return props.getProperty("expected.boosted.rule.path");
    }

    public static String getNrrpParameter() {
        return props.getProperty("assembler.nrpp.parameter");
    }

    public static String getOffsetParam() {
        return props.getProperty("offset.param");
    }

    public static String getSearchQueryParam() {
        return props.getProperty("search.query");
    }

    public static String getSAPIDesktopLocalChannel() {
        return props.getProperty("locale.id.desktop.search");
    }

    public static String getSearchQueryParamWithSpecialCharacters() {
        return props.getProperty("search.query.with.special.characters");
    }

    public static String getAssemblerGenericInterfaceParam() {
        return props.getProperty("assembler.interface.param");
    }

    public static String getAssemblerSearchTermParam() {
        return props.getProperty("assembler.search.term.param");
    }

    public static String getAssemblerKeywordInterfaceParam() {
        return props.getProperty("assembler.keyword.search.param");
    }

    public static String getAssemblerCascadeInterfaceParam() {
        return props.getProperty("assembler.search.generic.cascade.param");
    }

    public static String getAssemblerCascadeInterfaceParamWithoutRecordFilter() {
        return props.getProperty("assembler.search.generic.cascade.param.without.filters");
    }

    public static String getAssemblerCatchAllInterfaceParam() {
        return props.getProperty("assembler.catch.all.param");
    }

    public static String getChannelIdParam() {
        return props.getProperty("channelId.param");
    }

    public static String getClientIdParam() {
        return props.getProperty("clientId.param");
    }

    public static String getLocaleIdParam() {
        return props.getProperty("localeId.param");
    }

    public static String getPartialAssemblerPopularityParam() {
        return props.getProperty("assembler.partial.popularity.param");
    }

    public static String getAscendingDescendingParam() {
        return props.getProperty("param.ascending.decsending");
    }

    public static String getNrFilterParam() {
        return props.getProperty("nr.filter.param");
    }

    public static String getNtkParam() {
        return props.getProperty("ntk.param");
    }

    public static String getSortParam() {
        return props.getProperty("sort.param");
    }

    public static String getDiscontinuedAndPackTypeRecordFields() {
        return props.getProperty("discon.and.pack.typ.record.field.param");
    }

    public static String getSearchResultsAttributesList() {
        return props.getProperty("search.results.specification.attributes");
    }

    public static String getRedirectParams() {
        return props.getProperty("redirect.param.sapi");
    }

    public static String getEnableRedirectParams() {
        return props.getProperty("enable.redirect.sapi");
    }

    public static String getSearchTerm(String searchTerm) {
        return props.getProperty(searchTerm);
    }

    public static String getClientChannelParams() {
        return props.getProperty("client.channel.interface.param");
    }

    public static String getExpectedBoostedRuleWithSearchTermPath() {
        return props.getProperty("boosted.path.with.search.term");
    }

    public static String getExpectedBuriedRuleWithSearchTermPath() {
        return props.getProperty("buried.path.with.search.term");
    }

    public static String getBoostAssemblerRelRankingParameter() {
        return props.getProperty("boost.assembler.rel.ranking.parameter");
    }

    public static String getBuriedtAssemblerRelRankingParameter() {
        return props.getProperty("bury.assembler.rel.ranking.parameter");
    }

    public static String getExpectedBoostedWithSearchTermPath() {
        return props.getProperty("boost.assembler.search.results.page.rel.ranking.path");
    }

    public static String getExpectedBuriedWithSearchTermPath() {
        return props.getProperty("bury.assembler.search.results.page.rel.ranking.path");
    }

    public static String getRelevanceRankingBoostSearchResultsParam() {
        return props.getProperty("boost.assembler.relevance.ranking.parameter");
    }

    public static String getExclusionsParams() {
        return props.getProperty("exclusions.param");
    }

    public static String getRelevanceRankingBurySearchResultsParam() {
        return props.getProperty("bury.assembler.relevance.ranking.parameter");
    }

    public static String getBuriedBrandPSFWithSearchTerm() {
        return props.getProperty("buried.brand.psf.with.search.term");
    }

    public static String getBoostedBrandPSFWithSearchTerm() {
        return props.getProperty("boosted.brand.psf.with.search.term");
    }

    public static String getBoostedBrandWithSearchTerm() {
        return props.getProperty("boosted.brand.with.search.term");
    }

    public static String getBuriedBrandWithSearchTerm() {
        return props.getProperty("buried.brand.with.search.term");
    }

    public static String getBoostedPsfId() {
        return props.getProperty("boosted.rule.psf.id");
    }

    public static String getBoostedTerm() {
        return props.getProperty("boosted.rule.term");
    }

    public static String getBuriedPsfId() {
        return props.getProperty("buried.rule.psf.id");
    }

    public static String getBuriedTerm() {
        return props.getProperty("buried.rule.term");
    }

    public static String getBoostedSearchResultsTerm() {
        return props.getProperty("term.boosted.search.results");
    }

    public static String getBuriedSearchResultsTerm() {
        return props.getProperty("term.buried.search.results");
    }

    public static String getSearchResultsTaggingList() {
        return props.getProperty("search.results.adobe.tags");
    }

    public static String getSortOptions() {
        return props.getProperty("sort.options");
    }

    public static String getLocaleDimensionParam() {
        return props.getProperty("locale.dimension.param");
    }

    public static String getExcludedBrands() {
        return props.getProperty("excluded.brands");
    }

    public static String getExclusionsRuleFired() {
        return props.getProperty("exclusions.rule.fired");
    }

    public static String getSearchAttributesList() {
        return props.getProperty("multiple.attribute.term.1");
    }

    public static String getDimensionsWhiteList() {
        return props.getProperty("dimensions.whitelist");
    }

    public static String getDimensionsBlackList() {
        return props.getProperty("dimensions.blacklist");
    }

    public static String getSearchTypeParam() {
        return props.getProperty("search.type.parameter");
    }

    public static String getRedirectSearchTerm() {
        return props.getProperty("redirect.search.term");
    }

    public static String getProductLimitParam() {
        return props.getProperty("product.limit.param");
    }

    public static String getAppliedDimensionsParam() {
        return props.getProperty("applied.dimensions");
    }

    public static String getSearchConfigParam() {
        return props.getProperty("search.config.param");
    }

    public static String getUserSegmentParam() {
        return props.getProperty("user.segment.param");
    }

    public static String getSiblingsParam() {
        return props.getProperty("siblings.param.sapi");
    }

    public static String getNonSpecificationAttrIsNew() {
        return props.getProperty("non.specification.attr.is.new");
    }

    public static String getFacetAppliedIsNewIds() {
        return props.getProperty("is.new.facets.applied.ids");
    }

    public static String getPSFidsForINew() {
        return props.getProperty("is.new.psf.id");
    }

    public static String getPSFidsForLeadTime() {
        return props.getProperty("lead.time.psf.id");
    }

    public static String getNonSpecificationAttrLeadTime() {
        return props.getProperty("non.specification.attr.is.lead.time");
    }

    public static String getFacetAppliedLeadTimeIds() {
        return props.getProperty("lead.time.facets.applied.ids");
    }

    public static String getNameSpaceParam() {
        return props.getProperty("namespace.param");
    }

    public static String getImplicitRefinements() {
        return props.getProperty("param.implicit.refinements");
    }

    public static String getUserSegmentParamNotDefault() {
        return props.getProperty("user.segment.not.default");
    }

    public static String getExpectedUrlPath() {
        return props.getProperty("sapi.expected.term");
    }

    public static String getMpnParams() {
        return props.getProperty("searchtype.mpn.param");
    }

    public static String getPaddedStockNumber() {
        return props.getProperty("padded.stock.number");
    }

    public static String getRedirectDoubleByteSearchTerm() {
        return props.getProperty("redirect.double.byte.search.term");
    }

    public static String getPopularitySortSpec() {
        return props.getProperty("sort.spec.popularity");
    }

    public static String getLowToHighSortSpec() {
        return props.getProperty("sort.spec.low.to.high");
    }

    public static String getHighToLowSortSpec() {
        return props.getProperty("sort.spec.high.to.low");
    }

    public static String getCustomerFilterId() {
        return props.getProperty("customer.filter.id");
    }

    public static String getCustomerFilter() {
        return props.getProperty("customer.filter");
    }

    public static String getNrParam() {
        return props.getProperty("nr.param");
    }

    public static String getDiscontinuedProduct() {
        return props.getProperty("search.discontinued.product");
    }

    public static String getProductionPackedProduct() {
        return props.getProperty("search.production.packed.product");
    }

    public static String getProductFieldsParam() {
        return props.getProperty("product.fields.param");
    }

    public static String getDeclarativeFetchingParams() {
        return props.getProperty("declarative.fetching.params");
    }

    public static String getExcludedRecords() {
        return props.getProperty("products.excluded.productRecord");
    }

    public static String getSearchTermCustomerFilter() { return props.getProperty("assembler.search.term.customerId.param"); }

    public static String getlocaleParamsWithClientIdTest() {
        return props.getProperty("local.with.clientId.params");
    }

    public static String getCustomerFilterParams() {
        return props.getProperty("assembler.customer.filter.parameters");
    }

    public static String getRecordLimitParamater() { return props.getProperty("search.api.alternative.record.limit.param"); }

}


